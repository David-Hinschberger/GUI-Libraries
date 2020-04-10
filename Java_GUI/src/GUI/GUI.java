package GUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

public class GUI extends Application {
    static final int PROMPTCOLUMN = 0;
    static final int INPUTCOLUMN  = 1;
    static final int BUTTONCOLUMN = 2;

    private static boolean debug = false;

    private static String title;
    private static String iconURL;

    //Grid for UI
    private GridPane grid = new GridPane();
    private VBox vbox = new VBox();
    //Scene of application
    private Scene scene = new Scene(vbox, 670, 640);
    private static int leftRow = 0;
    private static int rightRow = 0;

    private HashMap<String, Input> inputs = new HashMap<>();
    private HashMap<String, Prompt> prompts= new HashMap<>();

    //ignore below


    //Hashmap of IDs mapped to the corresponding element
    private static HashMap<String, ELEMENT> IDMap = new HashMap<>();

    //List of pairs with id as key and value being the field's prompt
    private static List<Pair<String, String>> fieldsList = new ArrayList<>();
    //list of pairs with id as key and value being a list of button prompt(string),
    //ids of inputs to be connected to(string), index of the function to be called
    //from either consumerList or functionsList (int), and a boolean that specifies
    //consumerList or functionsList.
    private static List<Pair<String, Object[]>> buttonsList = new ArrayList<>();
    //Hashmap of IDs mapped to the corresponding button
    private static HashMap<String, Button> buttonHashMap = new HashMap<>();
    //Hashmap of IDs mapped to the corresponding TextField
    private static HashMap<String, TextField> textFieldHashMap = new HashMap<>();
    //List of functions
    private static List<Function<String[], String>> functionsList = new ArrayList<>();
    //List of Consumers
    private static List<Consumer<String[]>> consumersList = new ArrayList<>();

    /**
     * Returns a list of labels added in bottom-down order in code (chronological).
     * @return keys in the HashMap inputs sorted according to the index value of the Inputs
     */
    private List<String> getSortedLabels(){
        List<String> labels= new ArrayList<>(inputs.keySet());
        Collections.sort(labels, Comparator.comparingInt(x -> inputs.get(x).getIndex()));
        return labels;
    }

    /**
     * Refreshes the input for all the variables stored from the data on the GUI
     */
    private void refreshInput(){
        List<String> sortedLabels = getSortedLabels();
        for(String label: sortedLabels){
            inputs.get(label).setValue(inputs.get(label).getEntry());
        }
    }

    /**
     * Handles button press
     * @param userFunction is the function the user writes to be called back upon.
     */
    private void buttonPressed(Consumer<GUI> userFunction){
        refreshInput();
        userFunction.accept(this);
    }

    /**
     * Adds a field input
     * @param label
     * @param col
     * @param row
     * @param defValue
     * @param typeOfInput
     */
    private void inputHelper(String label, int col, int row, Object defValue, FIELD typeOfInput){
        inputs.put(label, new Input(col, row, defValue, typeOfInput));
    }


    private void startInput(Stage stage) {
        if(title != null){
            stage.setTitle(title);
        }
        if (iconURL != null) {
            stage.getIcons().add(new Image(iconURL));
        }

        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        for(String p: prompts.keySet()){
            Label label = new Label(prompts.get(p).getPrompt());
            GridPane.setConstraints(label, prompts.get(p).getCol(), prompts.get(p).getRow());
            grid.getChildren().add(label);
            prompts.get(p).setEntry(label);
        }

        


        final TextArea output = new TextArea();
        output.setWrapText(false);
        output.setEditable(false);

        VBox.setVgrow(output, Priority.ALWAYS);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.getChildren().add(grid);
        vbox.getChildren().add(output);

        final Label test = new Label("Enter a number:");
        GridPane.setConstraints(test, 0, leftRow++);
        grid.getChildren().add(test);

        for (Pair<String, String> data : fieldsList) {
            TextField field = new TextField();
            field.setPromptText(data.getValue());
            field.setPrefColumnCount(20);
            switch (IDMap.get(data.getKey())) {
                case DECIMALFIELD:
                    field.textProperty().addListener(
                        (observable, oldValue, newValue) ->
                        {
                            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                                field.setText(oldValue);
                            }
                        });
                    break;
                case INTFIELD:
                    field.textProperty().addListener(
                        (observable, oldValue, newValue) ->
                        {
                            if (!newValue.matches("\\d{0,10}")) {
                                field.setText(oldValue);
                            }
                        });
                    break;
            }
            GridPane.setConstraints(field, 0, leftRow++);
            grid.getChildren().add(field);
            textFieldHashMap.put(data.getKey(), field);
        }

        for (Pair<String, Object[]> data : buttonsList) {
            Button button = new Button((String) data.getValue()[0]);
            GridPane.setConstraints(button, 1, rightRow++);
            grid.getChildren().add(button);
            button.setOnAction(e -> {
                List<String> arguments = new ArrayList<>();
                for (String id : (String[]) data.getValue()[1]) {
                    arguments.add(textFieldHashMap.get(id).getText());
                }

                if ((boolean) data.getValue()[3]) {
                    output.setText(functionsList.get((int) data.getValue()[2])
                        .apply(arguments.toArray(new String[0])));
                } else {
                    consumersList.get((int) data.getValue()[2])
                        .accept(arguments.toArray(new String[0]));
                }
            });
            buttonHashMap.put(data.getKey(), button);
        }
    }




    public static void setTitle(String title) {
        GUI.title = title;
    }

    private static void checkDuplicateID(String id) {
        if (IDMap.containsKey(id)) {
            throw new DuplicateIDException(IDMap.get(id).toString());
        }
    }

    private static void checkUnreferencedID(String id) {
        if (!IDMap.containsKey(id)) {
            throw new UnreferencedIDException(id);
        }
    }

    private static void checkUnreferencedIDs(String... ids) {
        for (String id : ids) {
            checkUnreferencedID(id);
        }
    }

    public static boolean addField(FIELD field, String id, String prompt)
        throws DuplicateIDException {
        checkDuplicateID(id);
        ELEMENT elem = field == FIELD.STRINGFIELD ? ELEMENT.STRINGFIELD :
            field == FIELD.DECIMALFIELD ? ELEMENT.DECIMALFIELD :
                field == FIELD.INTFIELD ? ELEMENT.INTFIELD :
                    null;
        IDMap.put(id, elem);
        return fieldsList.add(new Pair<>(id, prompt));
    }

    static boolean addIntField(String id, String prompt) throws DuplicateIDException {
        checkDuplicateID(id);
        IDMap.put(id, ELEMENT.INTFIELD);
        return fieldsList.add(new Pair<>(id, prompt));
    }

    public static boolean addTextField(String id, String prompt) throws DuplicateIDException {
        checkDuplicateID(id);
        IDMap.put(id, ELEMENT.STRINGFIELD);
        return fieldsList.add(new Pair<>(id, prompt));
    }

    public static boolean addPrintButton(String id, String prompt, Consumer<String[]> function,
        String... ids) {
        checkDuplicateID(id);
        checkUnreferencedIDs(ids);
        IDMap.put(id, ELEMENT.BUTTON);
        consumersList.add(function);
        return buttonsList
            .add(new Pair<>(id, new Object[]{prompt, ids, consumersList.size() - 1, false}));
    }

    public static boolean addGUIButton(String id, String prompt, Function<String[], String> function,
        String... ids) {
        checkDuplicateID(id);
        checkUnreferencedIDs(ids);
        IDMap.put(id, ELEMENT.BUTTON);
        functionsList.add(function);
        return buttonsList
            .add(new Pair<>(id, new Object[]{prompt, ids, functionsList.size() - 1, true}));
    }

    public static void setIcon(String icon) {
        GUI.iconURL = icon;
    }

    public static void setDebug(boolean debug) {
        GUI.debug = debug;
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
            scene.widthProperty().addListener(
                (observableValue, oldSceneHeight, newSceneHeight) -> System.out
                    .println("Height: " + newSceneHeight));
        }

        startInput(stage);


        //resizable
        stage.setResizable(true);
        //adds scene to stage
        stage.setScene(scene);

        stage.show();
    }

    public void start() {
        launch();
    }

}
