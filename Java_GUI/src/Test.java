import javafx.application.Application;

public class Test {
    private static void printObject(Object num){
        System.out.println(num);
    }

    private static void squareNum(Object num){
        int temp = Integer.parseInt((String) num);
        System.out.println(temp * temp);
    }

    public static void main(String[] args) {
        GUI.addButton("identifier", "Square this number", Test::squareNum);
        GUI.addTextField("first", "prompt text");

        Application.launch(GUI.class, args);

    }

}