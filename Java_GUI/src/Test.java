import GUI.*;

public class Test {

    private static void fizzbuzz(GUI screen) {
        StringBuilder result = new StringBuilder();
        int limit = Integer.parseInt(screen.getStr("Enter a number"));
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
        screen.set("output", result.toString());
    }


    private static void squareNum(GUI screen) {
        String value = screen.getStr("Enter a number");
        int temp = Integer.parseInt(value);
        screen.set("output2", temp * temp);
    }


    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.setTitle("Windows 11");
        gui.setIcon("https://puu.sh/F5xQD.png");
        gui.addPrintWindow("output");


        gui.addText("Hello World", "Hello World");

        gui.addStringInput("Enter a number", "Enter a number");
        gui.addButton("Square", Test::squareNum);

        gui.addSpacer(2);

        gui.addFloatInput("identifier");

        gui.addStringInput("fizzbuzz");
        gui.addButton("FizzBuzz", Test::fizzbuzz);

        gui.setDebug(true);

        gui.startGUI();
    }

}