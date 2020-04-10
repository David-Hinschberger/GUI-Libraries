package GUI;

import java.util.function.Consumer;
import javafx.scene.control.Button;

class Function {
    private Consumer<GUI> function;
    private int row;
    private int col;
    private Button entry;

    Function(Consumer<GUI> function, int row, int col){
        this.function = function;
        this.row = row;
        this.col = col;
    }

    Consumer<GUI> getFunction() {
        return function;
    }

    int getRow() {
        return row;
    }

    int getCol() {
        return col;
    }

    Button getEntry() {
        return entry;
    }

    void setEntry(Button entry) {
        this.entry = entry;
    }
}
