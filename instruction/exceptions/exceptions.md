# Java Exceptions

🖥️ [Slides](https://docs.google.com/presentation/d/1CIyKxxGhJXUCQvwsJT64Oaao0p9LcBIZ/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

📖 **Required Reading**: Core Java SE 9 for the Impatient

- Chapter 5: Exceptions, Assertions, And Logging. (_Only read sections 5.1-5.1.9: Exception Handling_)

Java exceptions allow you to escape out of the normal execution flow of a program when something exceptional happens. You then centrally handle exception at a location higher in the code. For example, you might have a program that requires configuration files in order to work correctly. If those files do not exist you want to handle that case when the program starts up and not deep down in the initialization code where it actually tries to load the file. The following is a simple example of exception handling.

```java
import java.io.File;
import java.io.FileNotFoundException;

public class ExceptionExample {
    public static void main(String[] args) {
        // Exceptions are handled centrally for anything that happens in this scope.
        try {
            var example = new ExceptionExample();
            example.loadConfig();
        } catch (Exception ex) {
            System.out.printf("Cannot start program: %s", ex);
        }
    }

    private void loadConfig() throws FileNotFoundException {
        loadConfigFile("user");
        loadConfigFile("system");
    }

    // Note that the function indicates that it can throw an exception.
    private void loadConfigFile(String location) throws FileNotFoundException {
        var file = new File(location);
        if (!file.exists()) {
            // Let the code above know there was an exception.
            throw new FileNotFoundException();
        }

        // Otherwise load the configuration
    }
}

```

Note that exceptions should be exceptional. Do not throw exceptions for things that happen in the normal flow of your code. For example, if it is expected that sometimes a file may not be found, then that is not exceptional.

## Things to Understand

- The difference between checked and unchecked exceptions in Java
- How and when to handle an exception in Java
- How and when to declare an exception in Java
- How to use try/catch blocks
- What finally blocks are and how to use them

## Videos

- 🎥 [Exceptions](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=83d5acf8-12b7-473d-919d-ad6b0124631b&start=0)
- 🎥 [Checked vs. Unchecked Exceptions](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=3e7b6f62-13e5-41e6-9a81-ad6b012e8b25&start=0)

## Demonstration code

📁 [ExceptionRethrowingExample](example-code/ExceptionRethrowingExample.java)

📁 [ExceptionThrowingExample](example-code/ExceptionThrowingExample.java)

📁 [FileReadingWithExceptions](example-code/FileReadingWithExceptions.java)

📁 [FileReadingWithoutExceptions](example-code/FileReadingWithoutExceptions.java)

📁 [FinallyExample](example-code/FinallyExample.java)

📁 [ImageEditorException](example-code/ImageEditorException.java)

📁 [TryCatchExample](example-code/TryCatchExample.java)

📁 [TryWithResourcesExample](example-code/TryWithResourcesExample.java)
