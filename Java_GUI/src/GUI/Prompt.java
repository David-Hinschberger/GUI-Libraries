package GUI;

import javafx.scene.control.Control;

class Prompt {

    private String prompt;
    private boolean alignLeft;
    private int row;
    private int col;
    private Control Entry;

    Prompt(String prompt, boolean alignLeft, int row, int col){
        this.prompt = prompt;
        this.alignLeft = alignLeft;
        this.row = row;
        this.col = col;
    }


    String getPrompt() {
        return prompt;
    }

    boolean isAlignLeft() {
        return alignLeft;
    }

    int getCol() {
        return col;
    }

    int getRow() {
        return row;
    }

    void setEntry(Control entry) {
        Entry = entry;
    }
}
