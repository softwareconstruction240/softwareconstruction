# Java Exceptions

🖥️ [Slides](https://docs.google.com/presentation/d/1CIyKxxGhJXUCQvwsJT64Oaao0p9LcBIZ/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

📖 **Required Reading**: Core Java for the Impatient

- Chapter 5: Exceptions, Assertions, And Logging. (_Only read sections 5.1-5.1.9: Exception Handling_)

Java exceptions allow you to escape out of the normal execution flow of a program when something exceptional happens. You can then centrally handle the exception at a location higher in the code execution stack.

Java uses the standard `try`, `throw`, and `catch` syntax that are found in most programming languages. You define a block where exceptions can occur with the `try` statement. The `try` block is then followed by one or more `catch` blocks. For each `catch` block you can specify what exception type(s) the block handles. That type and any types derived from it will also be caught by that block unless they also match a more specific block. The runtime will pick the block that most specifically matches your exception. If you want to handle all exceptions, then you can specify the `Exception` base class in your catch block. Keep in mind that it is often best to catch only the most specific exception type that will be thrown.

```java
try {
    // Code that might throw an exception
} catch (FileNotFoundException ex) {
    // Specific file error handling
} catch (IOException ex) {
    // Other IO error handling except file not found
    /* FileNotFoundException is a subclass of
       IOExeption, but won't trigger this block. */
} catch (Exception ex) {
    // General error handling
}

```

## Throw and Throws

You use the `throw` keyword followed by the allocation of a new exception in order to raise an exception.

```java
throw new IllegalArgumentException("Missing required parameter");
```

When you throw an exception the normal flow of your code is interrupted and the execution pointer will skip to the closest catch block in the execution stack.

You can throw any exception from a function, but Java requires that your function signature declares all of the exceptions that the function throws. Note that the declaration requirement propagates to any function that calls a function that can throw an exception.

```java
void top() {
    try {
        A();
    } catch (Exception ex) {
        System.out.println("this WILL execute");
    }
}

void A() throws Exception {
    B();
    System.out.println("this will NOT execute");
}

void B() throws Exception {
    C();
    System.out.println("this will NOT execute");
}

void C() throws Exception {
    throw new Exception("declarations all the way up");
    System.out.println("this will NOT execute");
}
```

### Unchecked Exceptions

The exclusion to the `throws` declaration rule is when you throw what is known as an unchecked exception. Unchecked exceptions are defined as any class that is derived from the `RuntimeException` class. The reason for unchecked exceptions is that they can be thrown at anytime and so it is unreasonable to explicitly handle them on every function in your code. These should be caught or thrown only very rarely, as they usually indicate a bug in your code (such as a NullPointerException) rather than a problem that can ocurr during normal execution of your program (such as a FileNotFoundException).

A slight warning is that if you use IntelliJ's auto-suggestions about an uncaught exception, it will often generate a try/catch block with a `throw new RuntimeException();` call, which will silently crash your program if reached. Remember to always replace this will something else.

## Finally

You can also use the `try` syntax to create a block of code that always gets executed whenever the try block exits. This is called a finally block. The finally block is executed whether or not an exception is throw. If an exception is thrown, but there is no catch block, the finally method will get called, but then the exception continues up the call stack until a catch block is discovered.

```java
try {
    // Code that may throw an exception
} finally {
    // Code that always gets called
}
```

## Example

Consider the example of a program that requires a configuration file in order to work correctly. If the file does not exist, then you want report the error from your `main` function and not deep down in the initialization code where the file fails to load.

Note the use of multiple `catch` blocks, the use of `finally`, and also the necessity of declaring the exceptions that may be thrown.

```java
import java.io.File;
import java.io.FileNotFoundException;

public class ExceptionExample {
    public static void main(String[] args) {
        // Exceptions are handled centrally for anything that happens in this scope.
        try {
            var example = new ExceptionExample();
            example.loadConfig();
        } catch (FileNotFoundException ex) {
            System.out.printf("Required file not found: %s", ex);
        } catch (Exception ex) {
            System.out.printf("General error: %s", ex);
        } finally {
            System.out.println("Program completed");
        }
    }

    private void loadConfig() throws Exception {
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

## Custom Exception Types

Java has many useful Exception types you can `throw`, but often you won't find one that matches what you need. You can create your own exception types by creating subclasses of the Exception class. Feel free to add fields to your custom classes to contain any information that might be useful about what went wrong. If you find yourself catching an exception and then checking the message string to see what kind of error it is, you may want to replace it with a custom exception type instead.

## Try with Resource

Not closing resources, such as file handles or database connections, can lead to leaks that will cause your application to fail. The following example shows the allocation of an input stream that closes the stream after it is used. However, if an exception is thrown during the read operation the stream is not closed and the file handle is leaked. That means the resources associated with the file are never released and eventually that application will not be able to open files.

```java
public void NoTry() throws IOException {
    FileInputStream input = new FileInputStream("test.txt");
    System.out.println(input.read());

    // If an exception is thrown this will not close the stream
    input.close();
}
```

To work around this, it is common to use the `try/finally` syntax to clean up resources that need to be closed. In the example below the stream will be closed whether or not an exception is thrown.

```java
public void TryWithFinally() throws IOException {
    FileInputStream input = null;
    try {
        input = new FileInputStream("test.txt");
        System.out.println(input.read());
    } finally {
        if (input != null) {
            // If an exception is thrown this will not close the stream
            input.close();
        }
    }
}
```

As you can see by the previous example, resource cleanup introduces a lot of boilerplate code. To make this common, necessary, activity easier to implement Java introduced the `try with resource` syntax. You can use this syntax with any class that implements the [closable](https://docs.oracle.com/javase/8/docs/api/java/io/Closeable.html) interface. This includes things like input and output streams, readers and writers, network connections, files, and channels.

To use this syntax you place the allocation of the object as a parameter to the `try` keyword. The Java complier will automatically generate the finally block and call close for you.

```java
public void tryWithResources() throws IOException {
    // Close is automatically called at the end of the try block
    try (FileInputStream input = new FileInputStream("test.txt")) {
        System.out.println(input.read());
    }
}
```

## Exceptions Should be Exceptional

Remember that exceptions should be exceptional. Do not throw exceptions for things that happen in the normal flow of your code. For example, if it is expected that sometimes a file may not be found, then that is not exceptional. Also do not throw exceptions to return values from a function. For example, a token parser should not throw exceptions in order to return tokens that it parses to anyone with a catch block.

Using exceptions for non-exceptional cases makes debugging much more difficult and creates unexpected side effects in your code that make it less maintainable.

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
