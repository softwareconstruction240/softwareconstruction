public class ExceptionThrowingExample {

    public void myMethod(int methodParam) {
        if(methodParam < 0) {
            throw new IllegalArgumentException("The parameter may not be negative");
        }
    }
}
