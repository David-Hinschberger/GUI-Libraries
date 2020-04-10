package GUI;

import javafx.scene.control.TextArea;

public class PrintWindow {
    private int row;
    private TextArea Entry;

    PrintWindow(int row){
        this.row = row;
    }

    int getRow() {
        return row;
    }

    TextArea getEntry() {
        return Entry;
    }

    void setEntry(TextArea entry) {
        Entry = entry;
    }
}
