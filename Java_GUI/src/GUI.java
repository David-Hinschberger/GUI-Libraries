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

public class GUI extends Application {

    //Grid for UI
    GridPane grid = new GridPane();
    //Scene of application
    Scene scene = new Scene(grid, 640, 480);
    List<Consumer<String>> stringConsumers;

    void addStringConsumers(Consumer<String> consumer){
        if(stringConsumers == null){
            stringConsumers = new ArrayList<>();
        }
        stringConsumers.add(consumer);
        main(null);
    }

    void setup() {
        System.out.println(stringConsumers == null);

        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        //Defining the Name text field
        final TextField name = new TextField();
        name.setPromptText("Enter your first name.");
        name.setPrefColumnCount(10);
        name.getText();
        GridPane.setConstraints(name, 0, 0);
        grid.getChildren().add(name);
//Defining the Last Name text field
        final TextField lastName = new TextField();
        lastName.setPromptText("Enter your last name.");
        GridPane.setConstraints(lastName, 0, 1);
        grid.getChildren().add(lastName);
//Defining the Comment text field
        final TextField comment = new TextField();
        comment.setPrefColumnCount(15);
        comment.setPromptText("Enter your comment.");
        GridPane.setConstraints(comment, 0, 2);
        grid.getChildren().add(comment);
//Defining the Submit button
        Button submit = new Button("Submit");
        GridPane.setConstraints(submit, 1, 0);
        grid.getChildren().add(submit);
//Defining the Clear button
        Button clear = new Button("Clear");
        GridPane.setConstraints(clear, 1, 1);
        grid.getChildren().add(clear);


        //Adding a Label
        final Label label = new Label();
        GridPane.setConstraints(label, 0, 3);
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
        clear.setOnAction(e -> {
            name.clear();
            lastName.clear();
            comment.clear();
            label.setText(null);
            if(stringConsumers == null){
                System.out.println("didn't initialize the list yet");
            }
            else if(stringConsumers.size() > 0){
                stringConsumers.get(0).accept(name.getText());
            }
        });
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
