public class Test {

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

        gui.main(args);
//        Application.launch(GUI.class, args);

    }

}