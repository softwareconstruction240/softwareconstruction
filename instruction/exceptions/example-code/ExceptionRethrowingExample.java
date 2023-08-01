public class ExceptionRethrowingExample {

    public void myMethod() {
        try {
            // Code that may throw different types of exceptions
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            // Do something with all checked exceptions
        }
    }
}
