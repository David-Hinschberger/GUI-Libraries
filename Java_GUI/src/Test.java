public class Test {
    private static void printString(String num){
        System.out.println(num);
    }

    public static void main(String[] args) {
        GUI foo = new GUI();
        foo.addStringConsumers(Test::printString);



//        foo.main(args);
    }

}