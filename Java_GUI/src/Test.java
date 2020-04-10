import static GUI.FIELD.*;
import GUI.*;

public class Test {

    private static String fizzbuzz(String... args) {
        StringBuilder result = new StringBuilder();
        int limit = Integer.parseInt(args[0]);
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


    static void squareNum(String... args) {
        String value = args[0];
        int temp = Integer.parseInt(value);
        System.out.println(temp * temp);
    }


    public static void main(String[] args) {
        GUI.setTitle("Windows 11");
        GUI.setIcon("https://puu.sh/F5xQD.png");

        GUI.addTextField("Enter a number", "Enter a number");
        GUI.addPrintButton("squareBtn", "Square this number", Test::squareNum, "Enter a number");

        GUI.addField(DECIMALFIELD, "identifier", "Decimal here");

        GUI.addTextField("fizzbuzz", "Fizz Buzz until: ");
        GUI.addGUIButton("fizzbuzzBtn", "FizzBuzz!", Test::fizzbuzz, "fizzbuzz");

        GUI.setDebug(true);

        new GUI().start();
    }

}