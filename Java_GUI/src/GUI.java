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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public class GUI extends Application {

    //Grid for UI
    private GridPane grid = new GridPane();
    //Scene of application
    private Scene scene = new Scene(grid, 640, 480);
    static private int leftRow = 0;
    static private int rightRow = 0;

    static private List<Pair<String, String>> textFieldsList = new ArrayList<>();
    //data for buttons associated with id
    //prompt, function, ids...
    static private List<Pair<String, Object[]>> buttonsList = new ArrayList<>();
    static private HashMap<String, Button> buttonHashMap = new HashMap<>();
    static private HashMap<String, TextField> textFieldHashMap = new HashMap<>();

    static private List<Function<String[], String>> functionsList = new ArrayList<>();
    static int functions_index = 0;
    static private List<Consumer<String[]>> consumersList = new ArrayList<>();
    static int consumers_index = 0;


    static boolean addTextField(String id, String prompt) {
        return textFieldsList.add(new Pair<>(id, prompt));
    }

    static boolean addButton(String id, String prompt, Consumer<String[]> function, String... ids) {
        consumersList.add(function);
        return buttonsList.add(new Pair<>(id, new Object[]{prompt, ids, consumersList.size() - 1}));

    }

    static boolean addButton(boolean returns, String id, String prompt, Function<String[], String> function, String... ids) {
        if (returns) {
            functionsList.add(function);
        }
        return buttonsList.add(new Pair<>(id, new Object[]{prompt, ids, functionsList.size() - 1, returns}));
    }

//    static boolean addButton(String id, String prompt, Function<String[], String> function, String... ids) {

//    }

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
        final Label output = new Label("");
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

                if (data.getValue().length == 4) {
                    output.setText(functionsList.get((int) data.getValue()[2])
                            .apply(arguments.toArray(new String[arguments.size()])));
                } else {
                    consumersList.get((int) data.getValue()[2])
                            .accept(arguments.toArray(new String[arguments.size()]));
                }
            });
            buttonHashMap.put(data.getKey(), button);
        }


    }

    @Override
    public void start(Stage stage) {
//        String javaVersion = System.getProperty("java.version");
//        String javafxVersion = System.getProperty("javafx.version");
//        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");

        setup();

        //resizable
        stage.setResizable(true);
        //adds scene to stage
        stage.setScene(scene);
        stage.show();
    }

    public void main(String... args) {
        launch();
    }

}
