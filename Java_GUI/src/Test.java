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

        GUI gui = new GUI();
        GUI.addTextField("first", "Enter a number");
        GUI.addButton("identifier", "Square this number", Test::squareNum, "first");

        GUI.addTextField("second", "Enter gibberish here");
        GUI.addTextField("third", "Enter gibberish here");
        GUI.addTextField("fourth", "Enter gibberish here");
        GUI.addTextField("fifth", "Enter gibberish here");
        GUI.addTextField("sixth", "Enter gibberish here");
        GUI.addButton("print_things", "Print things!", Test::printThings, "second", "third",
                "fourth", "fifth", "sixth");

        GUI.addTextField("fizzbuzz", "Fizz Buzz until: ");
        GUI.addButton(true, "fizzbuzz button", "FizzBuzz!", Test::fizzbuzz, "fizzbuzz");


        gui.main();
//        Application.launch(GUI.class, args);

    }

}