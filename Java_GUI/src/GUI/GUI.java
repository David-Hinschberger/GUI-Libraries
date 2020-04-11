package GUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application {

    static final int PROMPTCOLUMN = 0;
    static final int INPUTCOLUMN = 1;
    static final int BUTTONCOLUMN = 2;

    private static boolean debug = false;

    private static String title;
    private static String iconURL;

    //Grid for UI
    private GridPane grid = new GridPane();
    private VBox vbox = new VBox();
    //Scene of application
    private Scene scene = new Scene(vbox, 670, 640);

    private static HashMap<String, Input> inputs = new HashMap<>();
    private static HashMap<String, Prompt> prompts = new HashMap<>();
    private static HashMap<String, PrintWindow> printWindows = new HashMap<>();
    private static HashMap<String, Function> functions = new HashMap<>();
    private static int[] colRowCount = new int[3];

    /**
     * Returns a list of labels added in bottom-down order in code (chronological).
     *
     * @return keys in the HashMap inputs sorted according to the index value of the Inputs
     */
    private List<String> getSortedLabels() {
        List<String> labels = new ArrayList<>(inputs.keySet());
        Collections.sort(labels, Comparator.comparingInt(x -> inputs.get(x).getIndex()));
        return labels;
    }

    /**
     * Refreshes the input for all the variables stored from the data on the GUI
     */
    private void refreshInput() {
        List<String> sortedLabels = getSortedLabels();
        for (String label : sortedLabels) {
            Input input = inputs.get(label);
            if(input.getType() == FIELD.COMBO){
                input.setValue(((ComboBox<String>) (input.getEntry())).getValue());
            } else{
                input.setValue(((TextField) input.getEntry()).getText());
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
     * Adds a field input
     */
    private void inputHelper(String label, int col, int row, Object defValue, FIELD typeOfInput) {
        inputs.put(label, new Input(col, row, defValue, typeOfInput));
    }


    private void setup(Stage stage) {
        if (title != null) {
            stage.setTitle(title);
        }
        if (iconURL != null) {
            stage.getIcons().add(new Image(iconURL));
        }

        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.getChildren().add(grid);

        for (String label : printWindows.keySet()) {
            TextArea printWindow = new TextArea();
            printWindow.setWrapText(false);
            printWindow.setEditable(false);
            VBox.setVgrow(printWindow, Priority.ALWAYS);
            vbox.getChildren().add(printWindow);
            printWindows.get(label).setEntry(printWindow);
        }

        for (String p : prompts.keySet()) {
            Label label = new Label(prompts.get(p).getPrompt());
            GridPane.setConstraints(label, prompts.get(p).getCol(), prompts.get(p).getRow());
            grid.getChildren().add(label);
            prompts.get(p).setEntry(label);
        }

        // no need for spacers in Javafx?

        for (String sortedLabel : getSortedLabels()) {
            Input label = inputs.get(sortedLabel);
            if (label.getType() == FIELD.COMBO) {
                ComboBox<String> comboBox = new ComboBox<>(FXCollections
                    .observableList((List<String>) label.getInitValue()));
                label.setEntry(comboBox);
                GridPane.setConstraints(comboBox, label.getCol(), label.getRow());
                inputs.get(label).setEntry(comboBox);
            } else {
                TextField field = new TextField();
                field.setPromptText(label.getValue().toString());
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
                GridPane.setConstraints(field, label.getCol(), label.getRow());
                grid.getChildren().add(field);
                inputs.get(sortedLabel).setEntry(field);
            }
        }

        for (String funcLabel : functions.keySet()) {
            Function function = functions.get(funcLabel);
            Button button = new Button(funcLabel);
            GridPane.setConstraints(button, function.getCol(), function.getRow());
            grid.getChildren().add(button);
            button.setOnAction(e -> buttonPressed(function.getFunction()));
            function.setEntry(button);
        }

        stage.setOnCloseRequest(e -> refreshInput());
    }

    public void addPrintWindow(String label) {
        PrintWindow printWindow = new PrintWindow(printWindows.size());
        printWindows.put(label, printWindow);
    }

    public void addButton(String label, Consumer<GUI> function) {
        Function f = new Function(function, colRowCount[BUTTONCOLUMN]++, BUTTONCOLUMN);
        functions.put(label, f);

    }

    public void addText(String identifier, String prompt) {
        addText(identifier, prompt, true);
    }

    public void addText(String identifier, String prompt, boolean alignLeft) {
        Prompt p = new Prompt(prompt, alignLeft, colRowCount[PROMPTCOLUMN]++, PROMPTCOLUMN);
    }

    public void addSpacer(int col) {
        colRowCount[col]++;
    }

    public void addIntInput(String label) {
        addIntInput(label, 0);
    }

    public void addIntInput(String label, int defValue) {
        inputHelper(label, INPUTCOLUMN, colRowCount[INPUTCOLUMN]++, defValue, FIELD.INT);
    }

    public void addStringInput(String label) {
        addStringInput(label, "");
    }

    public void addStringInput(String label, String defValue) {
        inputHelper(label, INPUTCOLUMN, colRowCount[INPUTCOLUMN]++, defValue, FIELD.STRING);
    }

    public void addFloatInput(String label) {
        addFloatInput(label, 0f);
    }

    public void addFloatInput(String label, double defValue) {
        inputHelper(label, INPUTCOLUMN, colRowCount[INPUTCOLUMN]++, defValue, FIELD.FLOAT);
    }

    public void addComboInput(String prompt, List<Object> choices) {
        int row = Math.max(colRowCount[PROMPTCOLUMN], colRowCount[INPUTCOLUMN]);
        Prompt p = new Prompt(prompt, true, row, PROMPTCOLUMN);
        inputHelper("__" + prompt + "__", INPUTCOLUMN, row, choices, FIELD.COMBO);
        colRowCount[PROMPTCOLUMN] = row + 1;
        colRowCount[INPUTCOLUMN] = row + 1;
    }

    public void addTitle(String title) {
        GUI.title = title;
    }

    public void addIcon(String icon) {
        GUI.iconURL = icon;
    }

    public static void setIcon(String icon) {
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
            return (String) inputs.get("__" + label + "__").getValue();
        } else if (inputs.containsKey(label)) {
            return (String) (inputs.get(label).getValue());
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
            return Integer.parseInt((String) (inputs.get("__" + label + "__").getValue()));
        } else if (inputs.containsKey(label)) {
            return Integer.parseInt((String) (inputs.get(label).getValue()));
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
            return Double.parseDouble((String) (inputs.get("__" + label + "__").getValue()));
        } else if (inputs.containsKey(label)) {
            return Double.parseDouble((String) (inputs.get(label).getValue()));
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
