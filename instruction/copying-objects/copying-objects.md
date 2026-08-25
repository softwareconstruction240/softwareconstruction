# Copying Objects

🖥️ [Slides](https://docs.google.com/presentation/d/1TAl9a41zLMyQmuQTYgxmYct6gsWgWopc/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

🖥️ [Lecture Videos](#videos)

### 🔑 Key points

- The difference between a shallow copy and a deep copy.
- How to use copy constructors to implement deep copies.
- How to use `clone` methods to implement deep copies.

---


In software engineering, copying an object is rarely as simple as assigning one variable to another. Because objects are stored as references in memory, a simple assignment creates a new pointer to the same data, not a unique duplicate. Understanding the distinction between **Shallow Copies** and **Deep Copies** is fundamental to maintaining data integrity and preventing "spooky action at a distance," where modifying one variable unexpectedly alters another.

Properly applying copying principles aligns with the **Principle of Least Astonishment**. Developers expect that if they pass an object to a function, that function won't permanently mutate the original data unless explicitly intended. To prevent these side effects, engineers often employ **Defensive Copying**, creating a copy of an object before passing it to a subsystem or storing it in a class property to ensure the internal state remains encapsulated.

## Copy Constructors

A straightforward way to allow an object to be duplicated is to create a constructor that accepts an instance of its own class. This is commonly called a **Copy Constructor**.

The following example shows a class with both a standard constructor and a copy constructor. While the standard constructor initializes fields from individual arguments, the copy constructor extracts values from an existing object.

```java
public class MyClass {
    String name;

    // Standard constructor
    public MyClass(String name) {
      this.name = name;
    }

    // Copy constructor
    public MyClass(MyClass other) {
      // Since Strings are immutable in Java, we can share the reference.
      // For mutable objects, we would need to create a new instance.
      this.name = other.name;
    }
}
```

## Shallow and Deep Copies

When copying an object, you must distinguish between copying the **data** and copying a **reference** (pointer) to that data. 

- **Deep Copy:** Creates an independent duplicate of the data. Changes to the original do not affect the copy.
- **Shallow Copy:** Copies only the references to the data. Both the original and the copy point to the same underlying object. If that shared object is modified, the change is visible in both places.

A shallow copy is perfectly acceptable if the fields are **immutable** (cannot be changed after creation). However, if the fields are **mutable**, a deep copy is necessary to ensure the copy remains independent of the source.

### Shallow Copy Example
Notice that this example only copies the reference to the `data` array. If the values inside the array are changed via the `source` object, the `copy` object reflects those changes because they share the same array.

```java
public class ShallowCopy {
    String[] data;

    public ShallowCopy() {
        data = new String[]{"a", "b", "c"};
    }

    public ShallowCopy(ShallowCopy other) {
        // Shallow copy: both objects point to the same array
        this.data = other.data;
    }

    public static void main(String[] args) {
        var source = new ShallowCopy();
        var copy = new ShallowCopy(source);

        // Modify the source data
        source.data[0] = "x";

        // Side effect: The copy also reflects the change
        // This outputs 'x'
        System.out.println(copy.data[0]);
    }
}
```

### Deep Copy Example
To prevent side effects, we perform a deep copy by duplicating the actual contents of the array. This ensures the `copy` is independent of the `source`.

```java
import java.util.Arrays;

public class DeepCopy {
    String[] data;

    public DeepCopy() {
        data = new String[]{"a", "b", "c"};
    }

    public DeepCopy(DeepCopy other) {
        // Deep copy: create a new array and copy the elements
        this.data = Arrays.copyOf(other.data, other.data.length);
    }

    public static void main(String[] args) {
        var source = new DeepCopy();
        var copy = new DeepCopy(source);

        // Modify the source data
        source.data[0] = "x";

        // The copy remains independent
        // This outputs 'a'
        System.out.println(copy.data[0]);
    }
}
```

## Clone

As an alternative to a copy constructor, you can override the `clone` method from the Java `Object` class. To do this, your class must implement the `Cloneable` interface.

```java
import java.util.Arrays;

public class CloneCopy implements Cloneable {
    String[] data;

    public CloneCopy() {
        data = new String[]{"a", "b", "c"};
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        // Start with a bitwise copy from Object.clone()
        CloneCopy cloned = (CloneCopy) super.clone();
        // Manually deep copy mutable fields
        cloned.data = Arrays.copyOf(this.data, this.data.length);
        return cloned;
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        var source = new CloneCopy();
        var copy = (CloneCopy) source.clone();

        System.out.println(copy.data[0]);
    }
}
```

## Copy Constructor vs. Clonable

In modern Java development, **copy constructors** are widely preferred over the `clone()` method. While `Object.clone()` was part of the original language design, it is now often considered "broken" or problematic by experts like Joshua Bloch for the following reasons:

*   **Type Safety:** You don't need to cast the result from `Object` to your specific type.
*   **Final Fields:** Copy constructors allow you to initialize `final` fields, which `clone()` cannot do.
*   **No Checked Exceptions:** You don't have to catch `CloneNotSupportedException`.
*   **Control:** It is easier to implement "deep copies" (copying nested objects) manually within a constructor.


## Engineering Principles for Object Copying

When designing robust systems, consider these three principles:

1.  **Immutability by Default:** Whenever possible, treat objects as immutable. Instead of modifying an existing object, always return a new copy with the updated values. This simplifies debugging and state management (e.g., in React or Redux).
2.  **Defensive Copying in Constructors:** If your class accepts a list or an object as a parameter, copy it during initialization. This prevents the caller from modifying the internal state of your instance from the outside.
3.  **Performance Awareness:** Deep copies are computationally expensive. For large, deeply nested trees (like a DOM or a complex JSON response), frequent deep copying can lead to memory pressure and latency. In these cases, consider using **Immutable Data Structures** (like Immutable.js) which use structural sharing to make "copies" efficient.


## ☑ Exercise

```masteryls
{"id":"bfcc9583-3d22-4026-a36f-34fceb6be366", "title":"Essay", "type":"essay", "gradingCriteria":"- Addresses the prompt directly\n- Uses at least one concrete example\n- Demonstrates accurate understanding of key concepts" }
Which approach do you think would be easier to maintain if you added a list of grades to the `Student` class?
```

```masteryls
{"id":"113e9ac2-025a-40a3-bc30-7ffbbc94cbbb", "title":"Essay", "type":"essay" }
Write a simply copy constructor for a chess piece that has a color and type property.
```

## Videos

- 🎥 [Copying Objects - Overview (6:54)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=cfdfab1b-0c5b-4cdc-b40f-b19c011ca4cc) - [[transcript]](https://github.com/user-attachments/files/17737115/CS_240_Copying_Objects_Overview_Transcript.pdf)
- 🎥 [Making a Shallow Copy (7:24)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=eb6a8c67-9f61-4741-a099-b19c011ebcfe) - [[transcript]](https://github.com/user-attachments/files/17737145/CS_240_Copying_Objects_Shallow_Copy_Example_Transcript.pdf)
- 🎥 [Making a Deep Copy (8:32)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=3e36cfa7-d993-4460-a24b-b19c0121c62a) - [[transcript]](https://github.com/user-attachments/files/17737161/CS_240_Copying_Objects_Deep_Copy_Examples_Transcript.pdf)

## Demonstration code

### Copy

📁 [Course](example-code/Course.java)

📁 [Faculty](example-code/Faculty.java)

📁 [Person](example-code/Person.java)

📁 [Student](example-code/Student.java)

📁 [Test](example-code/Test.java)

📁 [YearInSchool](example-code/YearInSchool.java)

### Clone

📁 [Person](example-code/clone/Person.java)

📁 [Person2](example-code/clone/Person2.java)

📁 [Team](example-code/clone/Team.java)