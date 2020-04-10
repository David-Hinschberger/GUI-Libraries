package GUI;

public class PrintWindow {
    private int col;
    private int row;
    PrintWindow(int col, int row){
        this.col = col;
        this.row = row;
    }

    int getCol() {
        return col;
    }

    int getRow() {
        return row;
    }
}
