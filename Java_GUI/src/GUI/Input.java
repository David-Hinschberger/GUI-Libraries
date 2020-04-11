package GUI;

import java.util.List;
import javafx.scene.control.Control;

/**
 * Defines data for input
 */
class Input {

    private static int count = 0;
    private String value;
    private Object initValue;
    private FIELD type;
    private int col;
    private int row;
    private int index;
    private Control Entry;

    /**
     * constructor
     * @param col
     * @param row
     * @param defValue
     * @param typeOfInput
     */
    Input(int col, int row, Object defValue, FIELD typeOfInput) {
        this.value = typeOfInput == FIELD.COMBO ? ((List<String>) defValue).get(0) : defValue.toString();
        this.initValue = defValue;
        this.type = typeOfInput;
        this.col = col;
        this.row = row;
        this.index = ++count;
    }

    String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }

    Object getInitValue() {
        return initValue;
    }

    FIELD getType() {
        return type;
    }

    void setType(FIELD type) {
        this.type = type;
    }

    int getCol() {
        return col;
    }

    int getRow() {
        return row;
    }

    int getIndex() {
        return index;
    }

    void setIndex(int index) {
        this.index = index;
    }

    Control getEntry() {
        return Entry;
    }

    void setEntry(Control entry) {
        Entry = entry;
    }
}
