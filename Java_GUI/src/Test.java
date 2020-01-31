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

//    private static void addTwoNumbers(){
//        int foo = GUI.GUI.getInt(1);
//        int bar = GUI.GUI.getInt(2);
//
//
//    }

    private static void printThings(String... args) {
        for (String s : args) {
            if (!s.equals("")) {
                System.out.println(s);
            }
        }
    }

    private static void squareNum(String... args) {
        String value = args[0];
        int temp = Integer.parseInt(value);
        System.out.println(temp * temp);
    }

    public static void main(String[] args) {
        GUI.setTitle("Windows 11");
        GUI.setIcon("https://puu.sh/F5xQD.png");

        GUI.addTextField("Enter a number", "Enter a number");
        GUI.addPrintButton("squareBtn", "Square this number", Test::squareNum, "Enter a number");

        GUI.addField(INTFIELD, "identifier", "Integer here");

//        GUI.GUI.addTextField("1", "Enter gibberish here");
//        GUI.GUI.addTextField("2", "Enter gibberish here");
//        GUI.GUI.addTextField("3", "Enter gibberish here");
//        GUI.GUI.addTextField("4", "Enter gibberish here");
//        GUI.GUI.addTextField("5", "Enter gibberish here");
//        GUI.GUI.addPrintButton("printBtn", "Print things!", Test::printThings, "1", "2",
//            "3", "4", "5");
//
//        GUI.GUI.addTextField("fizzbuzz", "Fizz Buzz until: ");
//        GUI.GUI.addGUIButton("fizzbuzzBtn", "FizzBuzz!", Test::fizzbuzz, "fizzbuzz");

//        GUI.GUI.addGUIButton("fizzbuzzBtn", "FizzBuzz!", Test::addTwoNumbers, "int1", "int2");

        GUI.setDebug(true);

        new GUI().start();
    }

}