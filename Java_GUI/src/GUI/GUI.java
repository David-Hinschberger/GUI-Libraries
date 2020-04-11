package GUI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application {

    private static final int COLUMNS = 3;
    private static final int PROMPTCOLUMN = 0;
    private static final int INPUTCOLUMN = 1;
    private static final int BUTTONCOLUMN = 2;
    private static List<Prompt> promptList = new ArrayList<>();
    private static List<Input> inputList = new ArrayList<>();
    private static List<Function> functionList = new ArrayList<>();

    private static boolean debug = false;

    private static String title;
    private static String iconURL;

    //Grid for UI
    private HBox hBox = new HBox();
    private VBox vbox = new VBox();
    //Scene of application
    private Scene scene = new Scene(vbox, 670, 640);

    private static HashMap<String, Input> inputs = new HashMap<>();
    private static HashMap<String, Prompt> prompts = new HashMap<>();
    private static HashMap<String, PrintWindow> printWindows = new HashMap<>();
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
            if (input.getType() == FIELD.COMBO) {
                input.setValue(((ComboBox<String>) (input.getEntry())).getValue());
            } else{
                String text = ((TextField) input.getEntry()).getText();
                if(input.getType() == FIELD.FLOAT) {
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

    private void setup(Stage stage) {
        if (title != null) {
            stage.setTitle(title);
        }
        if (iconURL != null) {
            stage.getIcons().add(new Image(iconURL));
        }

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

        stage.setOnCloseRequest(e -> refreshInput());
    }

    public void addPrintWindow(String label) {
        PrintWindow printWindow = new PrintWindow();
        printWindows.put(label, printWindow);
    }

    public void addButton(String label, Consumer<GUI> function) {
        Function f = new Function(function, colRowCount[BUTTONCOLUMN]++);
        functions.put(label, f);

    }

    public void addText(String identifier, String prompt) {
        addText(identifier, prompt, true);
    }

    public void addText(String identifier, String prompt, boolean alignLeft) {
        Prompt p = new Prompt(prompt, alignLeft, colRowCount[PROMPTCOLUMN]++, PROMPTCOLUMN);
        prompts.put(identifier, p);
    }

    public void addSpacer(int col) {
        colRowCount[col]++;
    }

    /**
     * Adds a field input
     */
    private void inputHelper(String label, int row, Object defValue, FIELD typeOfInput) {
        inputs.put(label, new Input(row, defValue, typeOfInput));
    }

    /**
     * Adds a field input
     */
    private void inputHelper(String label, int row, Object defValue, FIELD typeOfInput, String placeholder) {
        inputs.put(label, new Input(row, defValue, typeOfInput, placeholder));
    }

    public void addIntInput(String label) {
        addIntInput(label, "");
    }

    public void addIntInput(String label, String placeholder){
        addIntInput(label, placeholder, 0);
    }

    public void addIntInput(String label, String placeholder, int defValue) {
        inputHelper(label, colRowCount[INPUTCOLUMN]++, defValue, FIELD.INT, placeholder);
    }

    public void addStringInput(String label) {
        addStringInput(label, "", "");
    }

    public void addStringInput(String label, String placeholder) {
        addStringInput(label, "", placeholder);
    }

    public void addStringInput(String label, String placeholder, String defValue) {
        inputHelper(label, colRowCount[INPUTCOLUMN]++, defValue, FIELD.STRING, placeholder);
    }

    public void addFloatInput(String label) {
        addFloatInput(label, "");
    }

    public void addFloatInput(String label, String placeholder){
        addFloatInput(label, placeholder, 0d);
    }

    public void addFloatInput(String label, String placeholder, double defValue){
        inputHelper(label, colRowCount[INPUTCOLUMN]++, defValue, FIELD.FLOAT, placeholder);
    }

    public void addComboInput(String prompt, List choices) {
        int row = Math.max(colRowCount[PROMPTCOLUMN], colRowCount[INPUTCOLUMN]);
        Prompt p = new Prompt(prompt, true, row, PROMPTCOLUMN);
        prompts.put(prompt, p);
        inputHelper("__" + prompt + "__", row, choices, FIELD.COMBO);
        colRowCount[PROMPTCOLUMN] = row + 1;
        colRowCount[INPUTCOLUMN] = row + 1;
    }

    public void setTitle(String title) {
        GUI.title = title;
    }

    public void setIcon(String icon) {
        GUI.iconURL = icon;
    }

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

    public void startGUI() {
        launch();
    }

    public String getStr(String label) {
        if (printWindows.containsKey(label)) {
            return printWindows.get(label).getEntry().getText();
        } else if (inputs.containsKey("__" + label + "__")) {
            return inputs.get("__" + label + "__").getValue();
        } else if (inputs.containsKey(label)) {
            return inputs.get(label).getValue();
        } else if (prompts.containsKey(label)) {
            return prompts.get(label).getEntry().getText();
        } else {
            // throw label not found error?
            return null;
        }
    }

    public int getInt(String label) {
        if (printWindows.containsKey(label)) {
            return Integer.parseInt(printWindows.get(label).getEntry().getText());
        } else if (inputs.containsKey("__" + label + "__")) {
            return Integer.parseInt(inputs.get("__" + label + "__").getValue());
        } else if (inputs.containsKey(label)) {
            return Integer.parseInt(inputs.get(label).getValue());
        } else if (prompts.containsKey(label)) {
            return Integer.parseInt(prompts.get(label).getEntry().getText());
        } else {
            // throw label not found error?
            return Integer.MIN_VALUE;
        }
    }

    public double getFloat(String label) {
        if (printWindows.containsKey(label)) {
            return Double.parseDouble(printWindows.get(label).getEntry().getText());
        } else if (inputs.containsKey("__" + label + "__")) {
            return Double.parseDouble(inputs.get("__" + label + "__").getValue());
        } else if (inputs.containsKey(label)) {
            return Double.parseDouble(inputs.get(label).getValue());
        } else if (prompts.containsKey(label)) {
            return Double.parseDouble(prompts.get(label).getEntry().getText());
        } else {
            // throw label not found error?
            return Double.NaN;
        }
    }

    public void set(String label, Object value) {
        set(label, value, false);
    }

    public void set(String label, Object value, boolean append) {
        if (printWindows.containsKey(label)) {
            printWindows.get(label).getEntry().setText(value.toString());
        } else if (prompts.containsKey(label)) {
            if (append) {
                prompts.get(label).getEntry()
                    .setText(prompts.get(label).getEntry().getText() + value.toString());
            } else {
                prompts.get(label).getEntry().setText(value.toString());
            }
        } else if (inputs.containsKey(label)) {
            Control object = inputs.get(label).getEntry();
            if (inputs.get(label).getType() == FIELD.COMBO) {
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

    public void setDebug(boolean debug) {
        GUI.debug = debug;
    }

}
