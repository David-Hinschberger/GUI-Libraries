import GUI.GUI;
import java.util.List;

public class Test {

    private static void addTwoValues(GUI screen){
        int int1 = screen.getInt("int1");
        int int2 = screen.getInt("int2");
        int total = int1 + int2;
        screen.set("window1", total, true);
    }

    private static void floatAdd1(GUI screen){
        screen.set("window1", screen.getFloat("float1") + 1);
        screen.set("float1", screen.getFloat("float1") + 1);
    }

    private static void fizzbuzz1(GUI screen) {
        StringBuilder result = new StringBuilder();
        int limit = Integer.parseInt(screen.getStr("int1"));
        for (int i = 1; i <= limit; i++) {
            StringBuilder sb = new StringBuilder();
            if (i % 3 == 0) {
                sb.append("Fizz");
            }
            if (i % 5 == 0) {
                sb.append("Buzz");
            }
            if (sb.length() == 0) {
                sb.append(i);
            }
            sb.append(System.lineSeparator());
            result.append(sb);
        }
        screen.set("window1", result.toString(), true);
    }


    public static void main(String[] args) {
        int int1 = 10;
        int int2 = 20;
        double float1 = 123.45;

        GUI gui = new GUI();

        gui.addText("label1", "Testing data entry", false);
        gui.addSpacer(1);
        gui.addFileInput("Choose a File");

        gui.addText("label2", "Enter int 1");
        gui.addIntInput("int1", "int1", int1);

        gui.addText("label3", "Enter int 2");
        gui.addIntInput("int2", "int2", int2);

        gui.addText("label4", "Enter float 1");
        gui.addFloatInput("float1", "float1", float1);

        gui.addText("label5", "Enter string 1");
        gui.addStringInput("str1");

        List<String> fruits = List.of("Apple", "Pear", "Grape", "Orange");
        gui.addComboInput("Enter your favorite fruit", fruits);
        gui.addButton("add", Test::addTwoValues);
        gui.addButton("Float + 1", Test::floatAdd1);
        gui.addButton("Fizzbuzz until int1", Test::fizzbuzz1);

        gui.addText("Label6", "Total");
        gui.addIntInput("total");

        gui.addPrintWindow("window1");
        gui.setIcon("https://puu.sh/F5xQD.png");

        gui.setTitle("Windows 11");
        gui.setBackgroundColor("#4e00ff");


        System.out.println("Before inputs");
        System.out.println("int1 is " + int1);
        System.out.println("int2 is " + int2);
        System.out.println("float is " + gui.getFloat("float1"));
        System.out.println("String is " + gui.getStr("str1"));
        System.out.println("Total is " + gui.getInt("total"));
        System.out.println("Numeric total is " + (int1 + int2 + gui.getFloat("float1")));
        System.out.println("Favorite fruit is " + gui.getStr("Enter your favorite fruit"));

        gui.startGUI();
        int1 = gui.getInt("int1");
        int2 = gui.getInt("int2");

        System.out.println("\n\nAfter inputs");
        System.out.println("int1 is " + int1);
        System.out.println("int2 is " + int2);
        System.out.println("float is " + gui.getFloat("float1"));
        System.out.println("String is " + gui.getStr("str1"));
        System.out.println("Total is " + gui.getInt("total"));
        System.out.println("Numeric total is " + (int1 + int2 + gui.getFloat("float1")));
        System.out.println("Favorite fruit is " + gui.getStr("Enter your favorite fruit"));

        System.out.println(gui.getFile("Choose a File"));
    }


    private static void squareNum(GUI screen) {
        String value = screen.getStr("Enter a number");
        int temp = Integer.parseInt(value);
        screen.set("output", temp * temp);
    }

    private static void favoriteFruit(GUI screen){
        String favoriteFruit = screen.getStr("Favorite fruit?");
        screen.set("output", favoriteFruit);
    }

}