package GUI;

import java.util.List;
import javafx.scene.control.Control;

/**
 * Defines data for input
 */
class Input {
    private String value;
    private Object initValue;
    private FIELD type;
    private int row;
    private Control Entry;
    private String placeholder;

    /**
     * constructor
     * @param row
     * @param defValue
     * @param typeOfInput
     */
    Input(int row, Object defValue, FIELD typeOfInput) {
        this.value = typeOfInput == FIELD.COMBO ? ((List<Object>) defValue).get(0).toString() : defValue.toString();
        this.initValue = defValue;
        this.type = typeOfInput;
        this.row = row;
    }

    /**
     * constructor
     * @param row
     * @param defValue
     * @param typeOfInput
     * @param promptText
     */
    Input(int row, Object defValue, FIELD typeOfInput, String promptText) {
        this.value = typeOfInput == FIELD.COMBO ? ((List<Object>) defValue).get(0).toString() : defValue.toString();
        this.initValue = defValue;
        this.type = typeOfInput;
        this.row = row;
        this.placeholder = promptText;
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

    int getRow() {
        return row;
    }

    Control getEntry() {
        return Entry;
    }

    void setEntry(Control entry) {
        Entry = entry;
    }

    String getPlaceholder() {
        return placeholder;
    }
}
