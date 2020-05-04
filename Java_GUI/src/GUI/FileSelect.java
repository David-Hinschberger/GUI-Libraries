package GUI;

import java.io.File;
import javafx.scene.control.TextField;

public class FileSelect {
    private String promptText;
    private File file = null;
    private int row;
    private TextField textField;

    FileSelect(int row, String promptText){
        this.row = row;
        this.promptText = promptText;
    }

    void setFile(File file){
        this.file = file;
    }

    File getFile() {
        return file;
    }

    TextField getTextField() {
        return textField;
    }

    void setTextField(TextField textField) {
        this.textField = textField;
    }
}
