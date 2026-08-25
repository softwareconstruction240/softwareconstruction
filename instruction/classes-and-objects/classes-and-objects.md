# Classes and Objects

🖥️ [Slides](https://docs.google.com/presentation/d/17S-Y7Og08S9kRWHZfnH8k2wTBht39aCd/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

📖 **Required Reading**: Core Java for the Impatient Chapter 2: Object-Oriented Programming

🖥️ [Lecture Videos](#videos)

### 🔑 Key points

- What object references are and why we need them
- The differences between static methods and variables and instance methods and variables
- How constructors work in Java
- What constructor the compiler writes (and when it doesn't write one)
- What code the compiler adds to some constructors and why
- What the `this` reference is used for
- When the `this` reference is required and when it is optional
- What an enum is and how to implement one
- The standard order of elements in a Java class

---

Classes are the fundamental building blocks of Java programs. A class contains **fields** and **methods**. Fields represent the state (data) within the class, and methods represent the behavior (operations) that the class performs. For example, if we had a class representing a person, we might have a field called `name` and a method called `sayName` that prints that name. 

An **object** is an instantiation, or instance, of a class that has been initialized with specific values. A class often includes a **constructor**, which is a special block of code used to initialize the fields when the class is instantiated.

```java
public class Person {
  // Field
  private String name;

  // Constructor
  public Person(String name) {
    this.name = name;
  }

  // Method
  public void sayName() {
    System.out.println(name);
  }
}
```

To create a `Person` object from the `Person` class, you use the `new` operator and pass the required arguments to the constructor. Here is an example of a program that uses the `Person` class:

```java
public class HelloPerson {
  public static void main(String[] args) {
    var person = new Person("James Gosling");
    person.sayName();
  }
}
```

## The `this` Keyword

When you use the `new` operator to instantiate an object, it returns a **reference** to the new object. You use that reference to call methods and access fields.

```java
var obj = new Object();
obj.toString();
```

While external code uses a variable name to refer to an object, you sometimes need a way to refer to the current object from *within* its own class code. The keyword `this` acts as a reference to the current instance.

```java
public class ThisExample {
    String value = "example";

    @Override
    public String toString() {
        return this.value;
    }
}
```

To reduce verbosity, Java automatically infers `this` when you reference a class field, provided there is no local variable or parameter with the same name. You only *must* use `this` when a naming conflict occurs, which is common in constructors:

```java
public Person(String name) {
    this.name = name; // 'this.name' refers to the field; 'name' refers to the parameter
}
```

## Constructors

A constructor receives parameters and executes the code necessary to initialize a new object. A constructor must have the same name as the class and no return type. You can define multiple **overloaded** constructors with different parameter lists, allowing objects to be created with default or explicit values.

A common pattern is the **copy constructor**, which takes an existing object of the same type and copies its values into a new instance.

```java
public class ConstructorExample {
    public String value;

    /** Default constructor */
    public ConstructorExample() {
        value = "default";
    }

    /** Overloaded explicit constructor */
    public ConstructorExample(String value) {
        this.value = value;
    }

    /** Copy constructor */
    public ConstructorExample(ConstructorExample copy) {
        this(copy.value); // Calls the explicit constructor above
    }

    public static void main(String[] args) {
        System.out.println(new ConstructorExample().value);
        System.out.println(new ConstructorExample("A").value);
        System.out.println(new ConstructorExample(new ConstructorExample("B")).value);
    }
}
```

If you do not provide any constructors, the Java compiler automatically provides a **default no-argument constructor** that does nothing. If you define at least one constructor, the compiler will not generate the default one.

## Getters and Setters

An important concept in object-oriented programming is **encapsulation**. This involves hiding the internal state of an object and requiring all interaction to be performed through a well-defined interface (a "need-to-know" basis). Encapsulation makes code easier to maintain and change without breaking other parts of the system.

One common pattern for encapsulation is using **getters** (accessors) and **setters** (mutators) to manage access to private fields.

```java
public class GetSetExample {
    private String name; // Keep fields private

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

While this may seem like extra work initially, it allows you to change how data is stored or validated later without changing the code that uses the class. For instance, you might want a getter to return a copy of an array (to prevent external modification) or a setter to validate data:

```java
public class GetSetExample {
    private int[] scores = new int[10];

    public int[] getScores() {
        // Return a copy to protect the internal array
        int[] copy = new int[scores.length];
        System.arraycopy(scores, 0, copy, 0, scores.length);
        return copy;
    }

    public void setScores(int[] scores) {
        // Validate data before updating
        for (int score : scores) {
            if (score > 100) {
                return; // Or throw an exception
            }
        }
        this.scores = scores;
    }
}
```

## Enums

Enumerations (**enums**) allow you to define a fixed set of named constants. This makes your code more readable and prevents invalid values from being used.

```java
public enum Peak {
    NEBO, PROVO, SANTAQUIN, TIMPANOGOS, CASCADE, SPANISH, LONE
}
```

Enums are useful for validating parameters and restricting variables to a closed set of options. The following example demonstrates how to parse a string into an enum constant:

```java
public static void main(String[] args) {
    try {
        var e = Enum.valueOf(Peak.class, args[0].toUpperCase());

        if (e == Peak.LONE) {
            System.out.println("You chose Lone Peak");
        }
    } catch (IllegalArgumentException ex) {
        System.out.println("Unknown peak provided");
    }
}
```


## ☑ Exercise


```masteryls
{"id":"8818e31c-102f-4930-bc88-8f5662f09975","title":"Relationship Between Classes and Objects","type":"multiple-choice"}
In object-oriented programming, which statement best describes the fundamental relationship between a **class** and an **object**?

- [x] A class acts as a blueprint or template that defines properties and behaviors, while an object is a specific instance created from that blueprint.
- [ ] An object is a static blueprint that defines the structure of a program, while a class is the dynamic memory allocated during execution.
- [ ] A class is a specific realization of an object that contains unique data values assigned at runtime.
- [ ] Classes and objects are synonymous terms used to describe the same data structures within a programming language.
```

```masteryls
{"id":"c935ba62-abf8-45a9-bb1c-22806e4296e2","title":"Modeling with Integrity","type":"essay"}
As you learn to model real-world things as classes and objects, how does striving to represent them accurately and honestly shape the way you build software, and how does this reflect the BYU aim of developing Christlike character?
```


## Videos

- 🎥 [Overview (2:47)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=8248d213-ef10-44e1-8dbf-ad5d0143c0f8&start=0) - [[transcript]](https://github.com/user-attachments/files/17756219/CS_240_Classes_and_Objects_Overview.pdf)
- 🎥 [Object References (10:00)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=cd1007ae-04da-4311-9e2d-ad5d0144b41a&start=0) - [[transcript]](https://github.com/user-attachments/files/17756222/CS_240_Object_References.pdf)
- 🎥 [Static Variables and Methods (7:07)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=c2af67a3-d801-4550-bffe-ad5d01493675&start=0) - [[transcript]](https://github.com/user-attachments/files/17756224/CS_240_Static_Variables_and_Methods.pdf)
- 🎥 [Getters and Setters (2:33)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=b9ff467d-a874-4778-9b2a-ad5d014b74ad&start=0) - [[transcript]](https://github.com/user-attachments/files/17756226/CS_240_Getters_and_Setters.pdf)
- 🎥 [Constructor Methods (9:59)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=0d212d67-270d-4775-8ebb-ad5d014c7721&start=0) - [[transcript]](https://github.com/user-attachments/files/17756229/CS_240_Constructor_Methods.pdf)
- 🎥 [Inheritance (4:10)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=74faf0ca-9a24-4800-8ded-ad5d014f9493&start=0) - [[transcript]](https://github.com/user-attachments/files/17756231/CS_240_Inheritance.pdf)
- 🎥 [Method Overriding (9:36)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=d47ce0c1-85e5-45a7-b56d-ad5d01512d78&start=0) - [[transcript]](https://github.com/user-attachments/files/17756232/CS_240_Method_Overriding.pdf)
- 🎥 [Implementing a Hashcode Method (9:46)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=a486e175-a53f-4aed-b436-ad5d015744ac&start=0) - [[transcript]](https://github.com/user-attachments/files/17707561/CS_240_Implementing_a_Hashcode_Method_Transcript.pdf)
- 🎥 [Method Overloading (1:57)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=7bec5f67-10c3-4b19-a0fc-ad640139627a&start=0) - [[transcript]](https://github.com/user-attachments/files/17756235/CS_240_Method_Overloading.pdf)
- 🎥 [The Final Keyword (2:10)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=c8298a1a-b65c-43bd-8928-ad64013a3b89&start=0) - [[transcript]](https://github.com/user-attachments/files/17756237/CS_240_The_Final_Keyword.pdf)
- 🎥 [The `this` Reference (3:21)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=4d34dffd-7fec-4c10-b15a-ad64013b245c&start=0) - [[transcript]](https://github.com/user-attachments/files/17756239/CS_240_The_.this._Reference.pdf)
- 🎥 [Enums (3:23)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=57082049-bdad-4525-a6a1-ad64013e1eab&start=0) - [[transcript]](https://github.com/user-attachments/files/17756242/CS_240_Enums.pdf)
- 🎥 [Object-Oriented Design Overview (5:29)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=77c184e5-8afd-4c56-84c8-ad64013f7a4b&start=0) - [[transcript]](https://github.com/user-attachments/files/17756245/CS_240_Object_Oriented_Design_Overview.pdf)

## Demonstration code

📁 [Simple Class Example](example-code/SimpleClassExample)

📁 [Constructor Example](example-code/ConstructorExample)

📁 [Enum Example](example-code/EnumExample)

📁 [Equality Example](example-code/EqualityExample)

📁 [Override Example](example-code/OverrideExample)

📁 [Static Example](example-code/StaticExample)

## Lecture files

📁 [Lecture Files](lecture-files/)