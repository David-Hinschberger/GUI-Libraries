package GUI;

import java.util.function.Consumer;
import javafx.scene.control.Button;

class Function {
    private Consumer<GUI> function;
    private int row;
    private Button entry;

    Function(Consumer<GUI> function, int row){
        this.function = function;
        this.row = row;
    }

    Consumer<GUI> getFunction() {
        return function;
    }

    int getRow() {
        return row;
    }

    Button getEntry() {
        return entry;
    }

    void setEntry(Button entry) {
        this.entry = entry;
    }
}
