package GUI;

import javafx.scene.control.Label;

class Prompt {

    private String prompt;
    private boolean alignLeft;
    private int row;
    private int col;
    private Label Entry;

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

    void setEntry(Label entry) {
        Entry = entry;
    }

    public Label getEntry() {
        return Entry;
    }
}
