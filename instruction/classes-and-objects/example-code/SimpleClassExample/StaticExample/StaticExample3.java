package StaticExamples;

public class StaticExample3 {

    private int myInstanceVariable;

    public static void main(String [] args) {
        StaticExample3 instance = new StaticExample3();
        instance.myInstanceVariable = 10;
        instance.myInstanceMethod();
    }

    public void myInstanceMethod() {

    }
}
