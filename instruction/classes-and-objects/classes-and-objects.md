# Classes and Objects

🖥️ [Slides](https://docs.google.com/presentation/d/17S-Y7Og08S9kRWHZfnH8k2wTBht39aCd/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

📖 **Required Reading**: Core Java for the Impatient Chapter 2: Object-Oriented Programming

🖥️ [Lecture Videos](#videos)

Classes are the primary programming construct for Java. A class contains fields and methods. Fields represent variables within the class and methods represent operations that the class performs. For example, if we had a class that represents a person, we might have a field called `name` that contains the person's name, and a method called `sayName` that outputs the name. An object is an instantiation, or instance, of a class that has been initialized to contain values. A class may also have a constructor that provides the default values for the fields when the class is instantiated into an object.

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

If you wanted to instantiate a `Person` object from the `Person` class, you use the `new` operator and pass the values you want to initialize the object with. The provided values then get passed to the class's constructor. Here is an example of a Java program that uses the `Person` class.

```java
public class HelloPerson {
  public static void main(String[] args) {
    var person = new Person("James Gosling");
    person.sayName();
  }
}
```

## This Keyword

When you use the `new` operator to instantiate an object it returns a pointer reference to the new object. You can then use that to call methods and access variables to the object.

```java
var obj = new Object();
obj.toString();
```

That works great for code that uses your class, but sometimes you need a reference to the object within the class code. For example, consider the `toString` method. If you want to reference a class field as part of the output of the method then you can use the keyword `this`, as a substitute for the reference to the object.

```java
public class ThisExample {
    String value = "example";

    public String toString() {
        return this.value;
    }
}
```

In order to keep you from having to use the `this` keyword every time you reference a class field, Java will infer `this` when you reference a class field and there is no other conflicting variable with the same name. That makes it so you only have to use `this` when there is a conflict. This commonly happens in a class constructor method as demonstrated above in the `Person` example.

## Constructors

An object constructor receives parameters and executes the code necessary to completely construct an object. A constructor is specified with a method that has the same name as the class. You can have multiple, or overloaded, constructors by defining constructors that take different parameters. This is useful if you want the ability to construct an object with default values, or with explicit values.

A common pattern with constructors is to create what is called a `copy constructor` that takes an object of the class type, and deep copies all of the object's values to the new instance.

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
        this(copy.value);
    }

    public static void main(String[] args) {
        System.out.println(new ConstructorExample().value);
        System.out.println(new ConstructorExample("A").value);
        System.out.println(new ConstructorExample(new ConstructorExample("B")).value);
    }
}
```

If you don't provide a constructor then Java provides a default constructor that does nothing.

## Getters and Setters

An important concept in object oriented programming is the idea of encapsulation. This basically means that you only expose information on a `need to know basis`. Encapsulation makes it easier to change unexposed code.

One common design pattern to enable encapsulation is the use of getters and setters methods to access fields instead of publicly exposing the field directly. These methods are often called `accessor methods`. A getter is simply a method that gets the value of the field, and a setter is a method that sets the value of a field. This makes it so you can hide how the field is implemented and protect the data integrity of your fields.

Here is a simple example of using accessor methods.

```java
public class GetSetExample {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Keep fields private
    private String name;
}
```

Initially this may seem like overhead, but when your fields get more complex, or you decide to store your data in a different way, or even a different place, the value of accessor methods becomes clearer. Consider an example where the field type is an array, and you don't want to expose a mutable version of the array with the getter. Additionally, you want to check to make sure all the numbers in the array are less than 100 on the setter.

```java
public class GetSetExample {
    public int[] getScores() {
        int[] copy = new int[scores.length];
        System.arraycopy(scores, 0, copy, 0, scores.length);
        return copy;
    }

    public void setScores(int[] scores) {
        for (var i = 0; i < scores.length; i++) {
            if (scores[i] > 100) {
                return;
            }
        }
        this.scores = scores;
    }

    private int[] scores = new int[10];
}
```

If you have accessor methods for your fields then you can make changes like this without having to worry about how the code is being used.

## Enums

Enumerations allow you to create textual representations of labeled sets. You can define an enumeration in Java using the `enum` keyword.

```java
public enum Peak {
    NEBO, PROVO, SANTAQUIN, TIMPANOGOS, CASCADE, SPANISH, LONE
}

```

Using enumerations help to make your code more readable, validate parameters, and restrict values to a closed set. Here is an example that uses the `Peak` enumeration to parse command line arguments.

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

## Things to Understand

- What object references are and why we need them
- The differences between static methods and variables and instance methods and variables
- How constructors work in Java
- What constructor the compiler writes (and when it doesn't write one)
- What code the compiler adds to some constructors and why
- What the 'this' reference is used for
- When the 'this' reference is required and when it is optional
- What an enum is and how to implement one
- The standard order of elements in a Java class

## <a name="videos"></a>Videos (1:12:18)

- 🎥 [Overview (2:47)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=8248d213-ef10-44e1-8dbf-ad5d0143c0f8&start=0)
- 🎥 [Object References (10:00)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=cd1007ae-04da-4311-9e2d-ad5d0144b41a&start=0)
- 🎥 [Static Variables and Methods (7:07)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=c2af67a3-d801-4550-bffe-ad5d01493675&start=0)
- 🎥 [Getters and Setters (2:33)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=b9ff467d-a874-4778-9b2a-ad5d014b74ad&start=0)
- 🎥 [Constructor Methods (9:59)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=0d212d67-270d-4775-8ebb-ad5d014c7721&start=0)
- 🎥 [Inheritance (4:10)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=74faf0ca-9a24-4800-8ded-ad5d014f9493&start=0)
- 🎥 [Method Overriding (9:36)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=d47ce0c1-85e5-45a7-b56d-ad5d01512d78&start=0)
- 🎥 [Implementing a Hashcode Method (9:46)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=a486e175-a53f-4aed-b436-ad5d015744ac&start=0)
- [CS_240_Implementing_a_Hashcode_Method_Transcript.pdf](https://github.com/user-attachments/files/17707561/CS_240_Implementing_a_Hashcode_Method_Transcript.pdf)
- 🎥 [Method Overloading (1:57)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=7bec5f67-10c3-4b19-a0fc-ad640139627a&start=0)
- 🎥 [The Final Keyword (2:10)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=c8298a1a-b65c-43bd-8928-ad64013a3b89&start=0)
- 🎥 [The `this` Reference (3:21)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=4d34dffd-7fec-4c10-b15a-ad64013b245c&start=0)
- 🎥 [Enums (3:23)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=57082049-bdad-4525-a6a1-ad64013e1eab&start=0)
- 🎥 [Object-Oriented Design Overview (5:29)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=77c184e5-8afd-4c56-84c8-ad64013f7a4b&start=0)

## Demonstration code

📁 [Simple Class Example](example-code/SimpleClassExample)

📁 [Constructor Example](example-code/ConstructorExample)

📁 [Enum Example](example-code/EnumExample)

📁 [Equality Example](example-code/EqualityExample)

📁 [Override Example](example-code/OverrideExample)

📁 [Static Example](example-code/StaticExample)
