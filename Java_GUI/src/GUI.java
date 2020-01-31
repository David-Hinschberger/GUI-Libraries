import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public class GUI extends Application {

    private static boolean debug = false;

    private static String title;
    private static String iconURL;

    //Grid for UI
    private GridPane grid = new GridPane();
    //Scene of application
    private Scene scene = new Scene(grid, 640, 480);
    private static int leftRow = 0;
    private static int rightRow = 0;

    private enum ELEMENT {
        TEXTFIELD, BUTTON, LABEL;
    }

    //Hashmap of IDs mapped to the element they are
    private static HashMap<String, ELEMENT> IDMap = new HashMap<>();
    //list of pairs with id as key and value being the textfield's prompt
    private static List<Pair<String, String>> textFieldsList = new ArrayList<>();
    //list of pairs with id as key and value being a list of button prompt(string),
    //ids of inputs to be connected to(string), index of the function to be called
    //from either consumerList or functionsList (int), and a boolean that specifies
    //consumerList or functionsList.
    private static List<Pair<String, Object[]>> buttonsList = new ArrayList<>();
    private static HashMap<String, Button> buttonHashMap = new HashMap<>();
    private static HashMap<String, TextField> textFieldHashMap = new HashMap<>();

    private static List<Function<String[], String>> functionsList = new ArrayList<>();
    private static List<Consumer<String[]>> consumersList = new ArrayList<>();

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

    static boolean addTextField(String id, String prompt) throws DuplicateIDException {
        checkDuplicateID(id);
        IDMap.put(id, ELEMENT.TEXTFIELD);
        return textFieldsList.add(new Pair<>(id, prompt));
    }

    static boolean addPrintButton(String id, String prompt, Consumer<String[]> function,
        String... ids) {
        checkDuplicateID(id);
        checkUnreferencedIDs(ids);
        IDMap.put(id, ELEMENT.BUTTON);
        consumersList.add(function);
        return buttonsList
            .add(new Pair<>(id, new Object[]{prompt, ids, consumersList.size() - 1, false}));
    }

    static boolean addGUIButton(String id, String prompt, Function<String[], String> function,
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

    private void setup() {
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        for (Pair<String, String> data : textFieldsList) {
            TextField field = new TextField();
            field.setPromptText(data.getValue());
            field.setPrefColumnCount(20);
            field.getText();
            GridPane.setConstraints(field, 0, leftRow++);
            grid.getChildren().add(field);
            textFieldHashMap.put(data.getKey(), field);
        }

        //Adding a Label
        final Label outputLabel = new Label("Output:");
        GridPane.setConstraints(outputLabel, 0, leftRow++);
        GridPane.setColumnSpan(outputLabel, 2);
        grid.getChildren().add(outputLabel);

        final TextArea output = new TextArea();
        output.setWrapText(false);
        output.setEditable(false);
        GridPane.setConstraints(output, 0, leftRow++);
        GridPane.setColumnSpan(output, 2);

        grid.getChildren().add(output);

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

    @Override
    public void start(Stage stage) {
        if (debug) {
            System.out.println(
                "JavaFX Version: " + System.getProperty("java.version") + "\nJava Version: "
                    + System.getProperty("javafx.version"));
        }

        setup();
        stage.setTitle(title);
        if (iconURL != null) {
            stage.getIcons().add(new Image(iconURL));
        }

        //resizable
        stage.setResizable(true);
        //adds scene to stage
        stage.setScene(scene);
        stage.show();
    }

    public void main(String... args) {
        launch(args);
    }

}
