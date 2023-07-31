package StaticExamples;

public class StaticExampleWrong2 {

    private static int myInstanceVariable;

    public static void main(String [] args) {
        myInstanceVariable = 10;
        myInstanceMethod();
    }

    public static void myInstanceMethod() {

    }
}
