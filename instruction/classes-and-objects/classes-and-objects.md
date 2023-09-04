# Classes and Objects

🖥️ [Slides](https://docs.google.com/presentation/d/17S-Y7Og08S9kRWHZfnH8k2wTBht39aCd/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

📖 **Required Reading**: Core Java for the Impatient Chapter 2: Object-Oriented Programming

Classes are the primary programming construct for Java. A class contains fields and methods. Fields represent variables within the class and methods represent operations that the class performs. For example, if we had a class that represents a person we might have a data member called `name` that contains the person's name, and a method call `sayName` that outputs the name. An object is an instantiation, or instance, of a class that has been initialized to contain values. A class may have a constructor that provides the default values for the fields when the class is instantiated into an object. This class would look like the following.

```java
public class Person {
  // Field
  private String name;

  // Constructor
  public Person(String name) {
    this.name = name;
  }

  // Method
  public void printName() {
    System.out.println(name);
  }
}
```

If you wanted to instantiate a `Person` object from the `Person` class you use the `new` operator and pass the values you want to initialize the object with to the class' constructor. Here is an example of a Java program that uses the `Person` class.

```java
public class HelloWorld {
  public static void main(String[] args) {
    var person = new Person("James Gosling");
    person.sayName();
  }
}
```

## Things to Understand

1. What object references are and why we need them
1. The differences between static methods and variables and instance methods and variables
1. How constructors work in Java
1. What constructor the compiler writes (and when it doesn't write one)
1. What code the compiler adds to some constructors and why
1. How to do inheritance in Java
1. How to override methods in Java
1. How to properly implement a hashcode() method
1. How and why to overload a method
1. What the final keyword means when applied to variables, methods and classes
1. What the 'this' reference is used for
1. When the 'this' reference is required and when it is optional
1. What an enum is and how to implement one
1. The standard order of elements in a Java class

## Videos

- 🎥 [Overview](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=8248d213-ef10-44e1-8dbf-ad5d0143c0f8&start=0)
- 🎥 [Object References](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=cd1007ae-04da-4311-9e2d-ad5d0144b41a&start=0)
- 🎥 [Static Variables and Methods](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=c2af67a3-d801-4550-bffe-ad5d01493675&start=0)
- 🎥 [Getters and Setters](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=b9ff467d-a874-4778-9b2a-ad5d014b74ad&start=0)
- 🎥 [Constructor Methods](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=0d212d67-270d-4775-8ebb-ad5d014c7721&start=0)
- 🎥 [Inheritance](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=74faf0ca-9a24-4800-8ded-ad5d014f9493&start=0)
- 🎥 [Method Overriding](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=d47ce0c1-85e5-45a7-b56d-ad5d01512d78&start=0)
- 🎥 [Implementing a Hashcode Method](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=a486e175-a53f-4aed-b436-ad5d015744ac&start=0)
- 🎥 [Method Overloading](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=7bec5f67-10c3-4b19-a0fc-ad640139627a&start=0)
- 🎥 [The Final Keyword](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=c8298a1a-b65c-43bd-8928-ad64013a3b89&start=0)
- 🎥 [The `this` Reference](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=4d34dffd-7fec-4c10-b15a-ad64013b245c&start=0)
- 🎥 [Enums](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=57082049-bdad-4525-a6a1-ad64013e1eab&start=0)
- 🎥 [Object-Oriented Design Overview](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=77c184e5-8afd-4c56-84c8-ad64013f7a4b&start=0)

## Demonstration code

📁 [Simple Class Example](example-code/SimpleClassExample)

📁 [Constructor Example](example-code/ConstructorExample)

📁 [Enum Example](example-code/EnumExample)

📁 [Equality Example](example-code/EqualityExample)

📁 [Override Example](example-code/OverrideExample)

📁 [Static Example](example-code/StaticExample)
