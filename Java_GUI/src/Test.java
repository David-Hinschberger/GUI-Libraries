import GUI.*;

public class Test {

    private static String fizzbuzz(GUI screen) {
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
        return result.toString();
    }


    static void squareNum(GUI screen) {
        String value = screen.getStr("Enter a number");
        int temp = Integer.parseInt(value);
        System.out.println(temp * temp);
    }


    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.addTitle("Windows 11");
        gui.addIcon("https://puu.sh/F5xQD.png");

        gui.addStringInput("Enter a number", "Enter a number");
        gui.addButton("squareBtn", Test::squareNum);

        gui.addFloatInput("identifier");

        gui.addStringInput("fizzbuzz");
        gui.addButton("fizzbuzzBtn", Test::fizzbuzz);

        gui.setDebug(true);

        gui.startGUI();
    }

}