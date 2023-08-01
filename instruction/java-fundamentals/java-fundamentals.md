# Java Fundamentals

🖥️ [Slides](https://docs.google.com/presentation/d/1qC9DMSf7PYm-vBhK_qRS0Zu8lNVwQ4H7/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

📖 **Required Reading**: Core Java SE 9 for the Impatient

- Chapter 1: Fundamental Programming Structures
- Sections of Chapter 9:
  - Section 9.1 Input/Output Streams, Readers, and Writers
  - Section 9.2 Paths, Files, and Directories

In Java, all code is contained in a class that contains properties and methods. If your class has a method name `main` then it can act as the starting point for your program. A simple Java program looks like the following.

**HelloWorld.java**

```java
public class HelloWorld {
    public static void main(String[] args) {
      System.out.println("Hello World!");
    }
}
```

You can convert the above Java code into an intermediary binary format calls Java Byte code using the `javac` compiler. You can then execute the byte code using the Java virtual machine.

```sh
➜ javac HelloWorld.java
➜ java HelloWorld

Hello World!
```

Things to understand:

1. Ways that Java is different from C++
1. How to get and install Java and an IDE
1. Why Java is both portable and fast
1. How to compile and run Java code
1. The primitive data types available in Java
1. What's the difference between creating a string with `new` and creating one by just specifying it in double quotes?
1. How to declare, create and initialize arrays
1. How to find the length of an array
1. How to create and access arrays of arrays
1. How to specify command-line parameters in IntelliJ
1. The relationship between Packages, Imports and the CLASSPATH environment variable
1. How to use a Scanner to read a text file of words, separated by whitespace

## Videos

- 🎥 [Java History and Overview](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=cb99566e-3a9d-40c9-86c0-ad56013f4a64)
- 🎥 [Installing Java](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=0c39ffac-a1b2-4fa3-bb1e-ad560142a73b)
- 🎥 [Java Architecture](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=e9ff3ffe-ce6e-4bba-b363-ad560143be64)
- 🎥 [Writing, Compiling, and Running Java Code](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=6e020a04-00fd-40cf-95dc-ad560146bbd8&start=0)
- 🎥 [Javadoc](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=2a12a261-6e95-4e97-a838-ad56014c1ceb&start=0)
- 🎥 [Primitive Data Types](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=5d673b50-c9a2-465e-8d5c-ad56014eaeed&start=0)
- 🎥 [Working with Strings](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=f77e9a7f-36b2-40c8-8fa3-ad5601520775&start=0)
- 🎥 [Combining Strings with StringBuilder](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=b7d693f9-a705-42f4-98f4-adf8015c3166&start=0)
- 🎥 [Arrays](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=4d122f41-6fd6-4e78-bb3e-ad8e013d82c0&start=0)
- 🎥 [Array of Arrays](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=71826d41-0b65-4b98-903f-ad8e014772e0&start=0)
- 🎥 [Command Line Arguments](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=ddfefe0e-442d-4c56-8f60-ad5d013b4005&start=0)
- 🎥 [Packages, Imports, and the CLASSPATH Environment Variable](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=c52bc183-f041-42c6-8e36-ad5d013d318f&start=0)
- 🎥 [How to use a Scanner to read a text file of words separated by whitespace](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=3501b44d-296c-40b8-aee1-ad5d014138c8&start=0)

## Demonstration code

📁 [Simple Classes](example-code/01-simple-classes/)

📁 [Primitive Data Types](example-code/02-primitive-data-types/)

📁 [Strings](example-code/03-strings/)

📁 [Arrays](example-code/04-arrays/)

📁 [Command Line Arguments](example-code/05-command-line-arguments/)

📁 [Packages Imports](example-code/06-packages-imports/)

📁 [Input Output](example-code/07-input-output/)
