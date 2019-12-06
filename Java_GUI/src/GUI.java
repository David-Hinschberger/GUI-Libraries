import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
    static private List<Consumer<Object>> consumerList = new ArrayList<>();
    static private List<Pair<String, String>> textFieldsList = new ArrayList<>();
    static private List<Pair<String, Pair<String, Consumer<Object>>>> buttonsList = new ArrayList<>();


    static boolean addFunction(Consumer<Object> c){
        return consumerList.add(c);
    }

    static boolean addTextField(String id){
        return addTextField(id, "");
    }

    static boolean addTextField(String id, String prompt){
        return textFieldsList.add(new Pair<>(id, prompt));
    }

    static boolean addButton(String id, String prompt, Consumer<Object> function){
        return buttonsList.add(new Pair<>(id, new Pair<>(prompt, function)));
    }


    private void setup() {

        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        for(Pair<String, String> data: textFieldsList){
            TextField field = new TextField();
            field.setPromptText(data.getValue());
            field.setPrefColumnCount(10);
            field.getText();
            GridPane.setConstraints(field, 0, leftRow++);
            grid.getChildren().add(field);
        }

        //Defining the Name text field
        final TextField name = new TextField();
        name.setPromptText("Enter your first name.");
        name.setPrefColumnCount(10);
        name.getText();
        GridPane.setConstraints(name, 0, leftRow++);
        grid.getChildren().add(name);

        for(Pair<String, Pair<String, Consumer<Object>>> data: buttonsList) {
            Button button = new Button(data.getValue().getKey());
            GridPane.setConstraints(button, 1, rightRow++);
            grid.getChildren().add(button);
            button.setOnAction(e -> data.getValue().getValue().accept(name.getText()));
        }


//Defining the Last Name text field
        final TextField lastName = new TextField();
        lastName.setPromptText("Enter your last name.");
        GridPane.setConstraints(lastName, 0, leftRow++);
        grid.getChildren().add(lastName);
//Defining the Comment text field
        final TextField comment = new TextField();
        comment.setPrefColumnCount(15);
        comment.setPromptText("Enter your comment.");
        GridPane.setConstraints(comment, 0, leftRow++);
        grid.getChildren().add(comment);
//Defining the Submit button
        Button submit = new Button("Submit");
        GridPane.setConstraints(submit, 1, rightRow++);
        grid.getChildren().add(submit);
//Defining the Clear button
        Button custom_action = new Button("Custom");
        GridPane.setConstraints(custom_action, 1, rightRow++);
        grid.getChildren().add(custom_action);

        //Adding a Label
        final Label label = new Label("");
        GridPane.setConstraints(label, 0, leftRow++);
        GridPane.setColumnSpan(label, 2);
        grid.getChildren().add(label);

//Setting an action for the Submit button
        submit.setOnAction(e -> {
            if ((comment.getText() != null && !comment.getText().isEmpty())) {
                label.setText(name.getText() + " " + lastName.getText() + ", "
                        + "thank you for your comment!");
            } else {
                label.setText("You have not left a comment.");
            }
        });

//Setting an action for the Clear button
        custom_action.setOnAction(e -> consumerList.get(0).accept(name.getText()));
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

    public void main(String[] args) {
        launch();
    }

}
