package GUI;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class GUI extends Application {

    private static final int COLUMNS = 3;
    private static final int PROMPTCOLUMN = 0;
    private static final int INPUTCOLUMN = 1;
    private static final int BUTTONCOLUMN = 2;

    private static boolean debug = false;

    private static String title;
    private static String iconURI;
    private static String backgroundColor;
    //Grid for UI
    private HBox hBox = new HBox();
    private VBox vbox = new VBox();
    //Scene of application
    private Scene scene = new Scene(vbox, 670, 640);

    private static HashMap<String, Input> inputs = new HashMap<>();
    private static HashMap<String, Prompt> prompts = new HashMap<>();
    private static HashMap<String, PrintWindow> printWindows = new HashMap<>();
    private static HashMap<String, FileSelect> fileSelections = new HashMap<>();
    private static HashMap<String, Function> functions = new HashMap<>();
    private static int[] colRowCount = new int[COLUMNS];

    /**
     * Returns a list of labels added in bottom-down order in code (chronological).
     *
     * @return keys in the HashMap inputs sorted according to the index value of the Inputs
     */
    private List<String> getSortedLabels() {
        return inputs.keySet().stream()
            .sorted(Comparator.comparingInt(x -> inputs.get(x).getRow())).collect(
                Collectors.toList());
    }

    /**
     * Refreshes the input for all the variables stored from the data on the GUI
     */
    private void refreshInput() {
        List<String> sortedLabels = getSortedLabels();
        for (String label : sortedLabels) {
            Input input = inputs.get(label);
            if (input.getType() == FIELD.READSTRING) {
            } else if (input.getType() == FIELD.COMBO) {
                input.setValue(((ComboBox<String>) (input.getEntry())).getValue());
            } else {
                String text = ((TextField) input.getEntry()).getText();
                if (input.getType() == FIELD.FLOAT) {
                    try {
                        Double.parseDouble(text);
                    } catch (NumberFormatException e) {
                        text = "0";
                    }
                } else if (input.getType() == FIELD.INT) {
                    try {
                        Integer.parseInt(text);
                    } catch (NumberFormatException e) {
                        text = "0";
                    }
                }
                input.setValue(text);
            }
        }
    }

    /**
     * Handles button press
     *
     * @param userFunction is the function the user writes to be called back upon.
     */
    private void buttonPressed(Consumer<GUI> userFunction) {
        refreshInput();
        userFunction.accept(this);
    }

    /**
     * Setup for the entire window
     * @param stage is the window
     */
    private void setup(Stage stage) {
        if (title != null) {
            stage.setTitle(title);
        }
        if (iconURI != null) {
            stage.getIcons().add(new Image(iconURI));
        }

        vbox.setBackground(new Background(new BackgroundFill(backgroundColor == null ? null : Paint.valueOf(backgroundColor), null, null)));

        VBox[] vBoxes = new VBox[COLUMNS];
        hBox.setSpacing(10);

        for (int i = 0; i < COLUMNS; i++) {
            VBox vBox = new VBox();
            vBox.setPadding(new Insets(5, 0, 5, 0));
            hBox.getChildren().add(vBox);
            HBox.setHgrow(vBox, Priority.ALWAYS);
            vBoxes[i] = vBox;
        }

        vbox.getChildren().add(hBox);
        vbox.setPadding(new Insets(5, 5, 5, 5));
        vbox.setAlignment(Pos.TOP_CENTER);

        for (String label : printWindows.keySet()) {
            TextArea printWindow = new TextArea();
            printWindow.setWrapText(false);
            printWindow.setEditable(false);
            VBox.setVgrow(printWindow, Priority.ALWAYS);
            vbox.getChildren().add(printWindow);
            printWindows.get(label).setEntry(printWindow);
        }

        int row = 0;
        for (String p : prompts.keySet().stream()
            .sorted(Comparator.comparingInt(x -> prompts.get(x).getRow())).collect(
                Collectors.toList())) {
            while (prompts.get(p).getRow() > row) {
                Label spacer = new Label();
                vBoxes[PROMPTCOLUMN].getChildren().add(spacer);
                VBox.setMargin(spacer, new Insets(6, 2, 6, 2));
                row++;
            }
            Label label = new Label(prompts.get(p).getPrompt());
            if(!prompts.get(p).isAlignLeft()){
                label.setAlignment( Pos.CENTER_RIGHT);
            }
            label.setPrefWidth(200);
            vBoxes[PROMPTCOLUMN].getChildren().add(label);
            VBox.setMargin(label, new Insets(6, 2, 6, 2));
            prompts.get(p).setEntry(label);
            row++;
        }

        row = 0;
        for (String sortedLabel : getSortedLabels()) {
            Input label = inputs.get(sortedLabel);
            while (label.getRow() > row) {
                Label spacer = new Label();
                vBoxes[INPUTCOLUMN].getChildren().add(spacer);
                VBox.setMargin(spacer, new Insets(6, 2, 6, 2));
                row++;
            }
            if (label.getType() == FIELD.COMBO) {
                ComboBox<String> comboBox = new ComboBox<>(FXCollections
                    .observableList(
                        ((List<Object>) (label.getInitValue())).stream().map(Object::toString)
                            .collect(Collectors.toList())));
                comboBox.getSelectionModel().selectFirst();
                label.setEntry(comboBox);
                vBoxes[INPUTCOLUMN].getChildren().add(comboBox);
                VBox.setMargin(comboBox, new Insets(2, 2, 2, 2));
                inputs.get(sortedLabel).setEntry(comboBox);
            } else if (label.getType() == FIELD.READSTRING) {
                TextField field = new TextField();
                field.setPrefColumnCount(23);
                field.setText("No File Selected");
                field.setEditable(false);
                vBoxes[INPUTCOLUMN].getChildren().add(field);
                VBox.setMargin(field, new Insets(2, 2, 2, 2));
                fileSelections.get("##" + label.getValue() + "##").setTextField(field);
                
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle(label.getValue());
                fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
                Button button = new Button(label.getValue());
                button.setOnAction(e -> {
                    File file = fileChooser.showOpenDialog(stage);
                    fileSelections.get("##" + label.getValue() + "##").setFile(file);
                    fileSelections.get("##" + label.getValue() + "##").getTextField()
                        .setText(file != null ? file.getAbsolutePath() : "No File Selected");
                });
                functions.put("##" + label.getValue() + "##", new Function(null, label.getRow()));
                functions.get("##" + label.getValue() + "##").setEntry(button);

            } else {
                TextField field = new TextField();
                field.setText(label.getValue());
                field.setPromptText(label.getPlaceholder());
                field.setPrefColumnCount(23);
                switch (label.getType()) {
                    case FLOAT:
                        field.textProperty().addListener(
                            (observable, oldValue, newValue) ->
                            {
                                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                                    field.setText(oldValue);
                                }
                            });
                        break;
                    case INT:
                        field.textProperty().addListener(
                            (observable, oldValue, newValue) ->
                            {
                                if (!newValue.matches("\\d{0,10}")) {
                                    field.setText(oldValue);
                                }
                            });
                        break;
                }
                vBoxes[INPUTCOLUMN].getChildren().add(field);
                VBox.setMargin(field, new Insets(2, 2, 2, 2));
                inputs.get(sortedLabel).setEntry(field);
            }
            row++;
        }

        row = 0;

        for (String funcLabel : functions.keySet().stream()
            .sorted(Comparator.comparingInt(x -> functions.get(x).getRow())).collect(
                Collectors.toList())) {

            while (functions.get(funcLabel).getRow() > row) {
                Label spacer = new Label();
                vBoxes[BUTTONCOLUMN].getChildren().add(spacer);
                VBox.setMargin(spacer, new Insets(6, 2, 6, 2));
                row++;
            }
            if(functions.get(funcLabel).getEntry() != null){
                vBoxes[BUTTONCOLUMN].getChildren().add(functions.get(funcLabel).getEntry());
                VBox.setMargin(functions.get(funcLabel).getEntry(), new Insets(2, 2, 2, 2));
                continue;
            }

            Function function = functions.get(funcLabel);
            while (function.getRow() > row) {
                Label spacer = new Label();
                vBoxes[BUTTONCOLUMN].getChildren().add(spacer);
                VBox.setMargin(spacer, new Insets(6, 2, 6, 2));
                row++;
            }
            Button button = new Button(funcLabel);
            vBoxes[BUTTONCOLUMN].getChildren().add(button);
            VBox.setMargin(button, new Insets(2, 2, 2, 2));
            button.setOnAction(e -> buttonPressed(function.getFunction()));
            function.setEntry(button);
            row++;
        }

        if(vBoxes[PROMPTCOLUMN].getChildren().get(0) != null){
            vBoxes[PROMPTCOLUMN].getChildren().get(0).requestFocus();
        }

        stage.setOnCloseRequest(e -> refreshInput());
    }

    /**
     * Adds a print window to the GUI.
     * @param identifier is the identifier for the print window.
     */
    public void addPrintWindow(String identifier) {
        PrintWindow printWindow = new PrintWindow();
        printWindows.put(identifier, printWindow);
    }

    /**
     * Adds a button to the GUI.
     * @param label is the identifier and button text.
     * @param function is a function to be called when the button is clicked.
     */
    public void addButton(String label, Consumer<GUI> function) {
        Function f = new Function(function, colRowCount[BUTTONCOLUMN]++);
        functions.put(label, f);

    }

    /**
     * Adds text to the GUI.
     * @param identifier is the identifier for the text element.
     * @param text is the text to add to the GUI.
     */
    public void addText(String identifier, String text) {
        addText(identifier, text, true);
    }

    /**
     * Adds text to the GUI.
     * @param identifier is the identifier for the text element/
     * @param text is the text to add to the GUI.
     * @param alignLeft determines whether the text will be aligned left, or right if false.
     */
    public void addText(String identifier, String text, boolean alignLeft) {
        Prompt p = new Prompt(text, alignLeft, colRowCount[PROMPTCOLUMN]++, PROMPTCOLUMN);
        prompts.put(identifier, p);
    }

    /**
     * Adds a spacer element to the GUI.
     * @param col specifies the column to add the spacer to.  0 based indexing from the left.
     * Should be in the range of [0, 2]
     */
    public void addSpacer(int col) {
        colRowCount[col]++;
    }

    /**
     * Helper function to add a field input.
     * @param label is the text accompanying the input field.
     * @param row is the row to be added to.
     * @param defValue is the default value for the input field.
     * @param typeOfInput defines the type of input the to create.
     */
    private void inputHelper(String label, int row, Object defValue, FIELD typeOfInput) {
        inputs.put(label, new Input(row, defValue, typeOfInput));
    }

    /**
     * Helper function to add a field input.
     * @param label is the text and identifying string for the input field.
     * @param row is the row to be added to.
     * @param defValue is the default value for the input field.
     * @param typeOfInput defines the type of input the to create.
     * @param hint is the hint text (what is displayed when the field is empty).
     */
    private void inputHelper(String label, int row, Object defValue, FIELD typeOfInput,
        String hint) {
        inputs.put(label, new Input(row, defValue, typeOfInput, hint));
    }

    /**
     * Adds an int input field.
     * @param label is the text and identifying string for the input field.
     */
    public void addIntInput(String label) {
        addIntInput(label, "");
    }

    /**
     * Adds an int input field.
     * @param label is the text and identifying string for the input field.
     * @param hint is the hint text (what is displayed when the field is empty).
     */
    public void addIntInput(String label, String hint) {
        addIntInput(label, hint, 0);
    }

    /**
     * Adds an int input field.
     * @param label is the text and identifying string for the input field.
     * @param hint is the hint text (what is displayed when the field is empty).
     * @param defValue is the default value for the field.
     */
    public void addIntInput(String label, String hint, int defValue) {
        inputHelper(label, colRowCount[INPUTCOLUMN]++, defValue, FIELD.INT, hint);
    }

    /**
     * Adds a string input field.
     * @param label is the text and identifying string for the input field.
     */
    public void addStringInput(String label) {
        addStringInput(label, "", "");
    }

    /**
     * Adds a string input field.
     * @param label is the text and identifying string for the input field.
     * @param hint is the hint text (what is displayed when the field is empty).
     */
    public void addStringInput(String label, String hint) {
        addStringInput(label, "", hint);
    }

    /**
     * Adds a string input field.
     * @param label is the text and identifying string for the input field.
     * @param hint is the hint text (what is displayed when the field is empty).
     * @param defValue is the default value for the field.
     */
    public void addStringInput(String label, String hint, String defValue) {
        inputHelper(label, colRowCount[INPUTCOLUMN]++, defValue, FIELD.STRING, hint);
    }

    /**
     * Adds a float input field.
     * @param label is the text and identifying string for the input field.
     */
    public void addFloatInput(String label) {
        addFloatInput(label, "");
    }

    /**
     * Adds a float input field.
     * @param label is the text and identifying string for the input field.
     * @param hint is the hint text (what is displayed when the field is empty).
     */
    public void addFloatInput(String label, String hint) {
        addFloatInput(label, hint, 0d);
    }

    /**
     * Adds a float input field.
     * @param label is the text and identifying string for the input field.
     * @param hint is the hint text (what is displayed when the field is empty).
     * @param defValue is the default value for the field.
     */
    public void addFloatInput(String label, String hint, double defValue) {
        inputHelper(label, colRowCount[INPUTCOLUMN]++, defValue, FIELD.FLOAT, hint);
    }

    /**
     * Adds a combo box input field.
     * @param prompt is the prompt and identifying string for the combo box field.
     * @param choices is the list of choice for the combo box.
     */
    public void addComboInput(String prompt, List choices) {
        int row = IntStream
            .of(colRowCount[PROMPTCOLUMN], colRowCount[INPUTCOLUMN]).max().getAsInt();
        Prompt p = new Prompt(prompt, true, row, PROMPTCOLUMN);
        prompts.put(prompt, p);
        inputHelper("__" + prompt + "__", row, choices, FIELD.COMBO);
        colRowCount[PROMPTCOLUMN] = row + 1;
        colRowCount[INPUTCOLUMN] = row + 1;
    }

    /**
     * Adds a file input row of elements (prompt, text field displaying the file chosen, and button
     * to select a file.
     * @param prompt is the prompt and identifying string for the file input field.
     */
    public void addFileInput(String prompt) {
        int row = IntStream
            .of(colRowCount[PROMPTCOLUMN], colRowCount[INPUTCOLUMN], colRowCount[BUTTONCOLUMN])
            .max().getAsInt();
        Prompt p = new Prompt(prompt, true, row, PROMPTCOLUMN);
        prompts.put(prompt, p);
        inputHelper("##" + prompt + "##", row, prompt, FIELD.READSTRING, "");
        colRowCount[PROMPTCOLUMN] = row + 1;
        colRowCount[INPUTCOLUMN] = row + 1;
        colRowCount[BUTTONCOLUMN] = row + 1;
        fileSelections.put("##" + prompt + "##", new FileSelect(row, prompt));
    }

    /**
     * Sets the title for the GUI window.
     * @param title is the title to set to the GUI window.
     */
    public void setTitle(String title) {
        GUI.title = title;
    }

    /**
     * Sets the icon for the GUI window.
     * @param icon is a uri for the icon file.
     */
    public void setIcon(String icon) {
        GUI.iconURI = icon;
    }

    /**
     * Sets the background color for the GUI window.
     * @param hexColor is a RGB hex color string.
     */
    public void setBackgroundColor(String hexColor){
        backgroundColor = hexColor;
    }

    /**
     * Starts the GUI program.
     * @param stage is the stage for the window.
     */
    @Override
    public void start(Stage stage) {
        if (debug) {
            System.out.println(
                "JavaFX Version: " + System.getProperty("java.version") + "\nJava Version: "
                    + System.getProperty("javafx.version"));
            scene.widthProperty().addListener(
                (observableValue, oldSceneWidth, newSceneWidth) -> System.out
                    .println("Width: " + newSceneWidth));
            scene.heightProperty().addListener(
                (observableValue, oldSceneHeight, newSceneHeight) -> System.out
                    .println("Height: " + newSceneHeight));
        }

        setup(stage);

        //resizable
        stage.setResizable(true);
        //adds scene to stage
        stage.setScene(scene);

        stage.show();
    }

    /**
     * Starts the GUI program.
     */
    public void startGUI() {
        launch();
    }

    /**
     * Retrieves a string from the GUI.
     * @param id is the identifier for the GUI element.
     * @return the string assigned to the GUI element.
     */
    public String getStr(String id) {
        if (printWindows.containsKey(id)) {
            return printWindows.get(id).getEntry().getText();
        } else if (inputs.containsKey("__" + id + "__")) {
            return inputs.get("__" + id + "__").getValue();
        } else if (inputs.containsKey(id)) {
            return inputs.get(id).getValue();
        } else if (prompts.containsKey(id)) {
            return prompts.get(id).getEntry().getText();
        } else {
            throw new UnreferencedIDException(id);
        }
    }

    /**
     * Retrieves an integer from the GUI.
     * @param id is the identifier for the GUI element.
     * @return the int assigned to the GUI element.
     */
    public int getInt(String id) {
        if (printWindows.containsKey(id)) {
            return Integer.parseInt(printWindows.get(id).getEntry().getText());
        } else if (inputs.containsKey("__" + id + "__")) {
            return Integer.parseInt(inputs.get("__" + id + "__").getValue());
        } else if (inputs.containsKey(id)) {
            return Integer.parseInt(inputs.get(id).getValue());
        } else if (prompts.containsKey(id)) {
            return Integer.parseInt(prompts.get(id).getEntry().getText());
        } else {
            throw new UnreferencedIDException(id);
        }
    }

    /**
     * Retrieves a float from the GUI.
     * @param id is the identifier for the GUI element.
     * @return the float assigned to the GUI element.
     */
    public double getFloat(String id) {
        if (printWindows.containsKey(id)) {
            return Double.parseDouble(printWindows.get(id).getEntry().getText());
        } else if (inputs.containsKey("__" + id + "__")) {
            return Double.parseDouble(inputs.get("__" + id + "__").getValue());
        } else if (inputs.containsKey(id)) {
            return Double.parseDouble(inputs.get(id).getValue());
        } else if (prompts.containsKey(id)) {
            return Double.parseDouble(prompts.get(id).getEntry().getText());
        } else {
            throw new UnreferencedIDException(id);
        }
    }

    /**
     * Retrieves a file from the GUI.
     * @param id is the identifier for the GUI element.
     * @return the file assigned to the GUI element.
     */
    public File getFile(String id) {
        id = "##" + id + "##";
        if(fileSelections.containsKey(id))
            return fileSelections.get(id).getFile();
        throw new UnreferencedIDException(id);
    }

    /**
     * Sets the value for an element.
     * @param id is the identifier for the GUI element.
     * @param value is the value to set.
     */
    public void set(String id, Object value) {
        set(id, value, false);
    }

    /**
     * Sets the value for an element.
     * @param id is the identifier for the GUI element.
     * @param value is the value to set.
     * @param append determines whether to append or replace the value.
     */
    public void set(String id, Object value, boolean append) {
        if (printWindows.containsKey(id)) {
            printWindows.get(id).getEntry().setText(value.toString());
        } else if (prompts.containsKey(id)) {
            if (append) {
                prompts.get(id).getEntry()
                    .setText(prompts.get(id).getEntry().getText() + value.toString());
            } else {
                prompts.get(id).getEntry().setText(value.toString());
            }
        } else if (inputs.containsKey(id)) {
            Control object = inputs.get(id).getEntry();
            if (inputs.get(id).getType() == FIELD.COMBO) {
                ((ComboBox<String>) object).setPlaceholder((Node) value);
            } else {
                if (append) {
                    ((TextField) object).setText(((TextField) object).getText() + value.toString());
                } else {
                    ((TextField) object).setText(value.toString());
                }
            }
        }
    }

    /**
     * Sets the debug flag for the GUI.
     * @param debug is true for turning the flag on, false to turn off.
     */
    public void setDebug(boolean debug) {
        GUI.debug = debug;
    }

}
