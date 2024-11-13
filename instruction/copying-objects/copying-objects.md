# Copying Objects

🖥️ [Slides](https://docs.google.com/presentation/d/1TAl9a41zLMyQmuQTYgxmYct6gsWgWopc/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

🖥️ [Lecture Videos](#videos)

## Copy Constructors

An easy way to provide the ability to make a copy of your object is to create a constructor that takes an object you want to copy. This is commonly called a `Copy Constructor`.

Here is an example of a class that has a default constructor, and a copy constructor. Both constructors create the object, but with the default constructor you supply all the fields. With the copy constructor all the fields are copied out of the provided object.

```java
public class MyClass {
    String name;

    public MyClass(String name) {
      this.name = name;
    }

    public MyClass(MyClass copy) {
      this.name = new String(copy.name);
    }
}
```

## Shallow and Deep Copies

When you make a copy of an object you must consider the important difference between making a copy of the data and making a copy of a pointer to the data. When you make a copy of the data you make an independent duplicate of the data. When you copy a pointer to data, the copy can change when the data that the pointer references changes.

A copy that only copies the references is called a `shallow copy`. A copy that copies all of the data values is called a `deep copy`. It is fine to do a shallow copy if all of the fields in the object are immutable (i.e. cannot change), but if the data can change, then you need to copy all of the fields.That makes it so the values in the copy cannot be changed by manipulating the source object fields.

Here is an example of a shallow copy. Notice that it only copies the reference to the `data` array. That means that if the values in that array are changed, then it will also change the values in the copy.

```java
public class ShallowCopy {
    String[] data;

    public ShallowCopy() {
        data = new String[]{"a", "b", "c"};
    }

    public ShallowCopy(ShallowCopy copy) {
        data = copy.data;
    }

    public static void main(String[] args) {
        var source = new ShallowCopy();
        var copy = new ShallowCopy(source);

        // Change the source data
        source.data[0] = "x";

        // ERROR: The copy outputs 'x'
        System.out.println(copy.data[0]);
    }
}
```

To correct this problem, we do a deep copy of all the object fields. That makes it so we can modify the source object without modifying the copy.

```java
public class DeepCopy {
    String[] data;

    public DeepCopy() {
        data = new String[]{"a", "b", "c"};
    }

    public DeepCopy(DeepCopy copy) {
        data = Arrays.copyOf(copy.data, copy.data.length);
    }

    public static void main(String[] args) {
        var source = new DeepCopy();
        var copy = new DeepCopy(source);

        // Change the source data
        source.data[0] = "x";

        // Copy is independent from source
        // outputs 'a'
        System.out.println(copy.data[0]);
    }
}
```

## Clone

As an alternative to writing a copy constructor, you can override the `clone` operation of the Java `Object` class.

```java
public static class CloneCopy implements Cloneable {
    String[] data;

    public CloneCopy() {
        data = new String[]{"a", "b", "c"};
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        var clone = new CloneCopy();
        clone.data = Arrays.copyOf(data, data.length);
        return clone;
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        var source = new CloneCopy();
        var copy = (CloneCopy) source.clone();

        System.out.println(copy.data[0]);
    }
}
```

However, overriding `clone` requires that you implement the `Cloneable` marker interface, do a typecasting on the result, and handle the possibility of a `CloneNotSupportedException`.

## Things to Understand

- The difference between a shallow copy and a deep copy
- How to use `copy constructors` to implement deep copies
- How to use `clone` methods to implement deep copies

## <a name="videos"></a>Videos (22:50)

- 🎥 [Copying Objects - Overview (6:54)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=cfdfab1b-0c5b-4cdc-b40f-b19c011ca4cc)
- [CS_240_Copying_Objects_Overview_Transcript.pdf](https://github.com/user-attachments/files/17737115/CS_240_Copying_Objects_Overview_Transcript.pdf)
- 🎥 [Making a Shallow Copy (7:24)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=eb6a8c67-9f61-4741-a099-b19c011ebcfe)
- [CS_240_Copying_Objects_Shallow_Copy_Example_Transcript.pdf](https://github.com/user-attachments/files/17737145/CS_240_Copying_Objects_Shallow_Copy_Example_Transcript.pdf)
- 🎥 [Making a Deep Copy (8:32)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=3e36cfa7-d993-4460-a24b-b19c0121c62a)
- [CS_240_Copying_Objects_Deep_Copy_Examples_Transcript.pdf](https://github.com/user-attachments/files/17737161/CS_240_Copying_Objects_Deep_Copy_Examples_Transcript.pdf)

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
