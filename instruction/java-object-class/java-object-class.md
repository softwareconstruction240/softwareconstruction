# Java Object Class

🖥️ [Slides](https://docs.google.com/presentation/d/17S-Y7Og08S9kRWHZfnH8k2wTBht39aCd/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

🖥️ [Lecture Videos](#videos)

All classes in Java can have a single base class that they extend. When you extend another class you inherit all of the public and protected methods and fields that the class provides. The default Java base class is called the `Object` class. If you do not explicitly specify what base class your class extends then you will automatically inherit the `Object` class.

When one class extends another class it can override, or overload, the base classes methods in order to alter, or extend, its functionality. The base `Object` class contains the following methods that you can override.

| Method          | Comment                                                                    |
| --------------- | -------------------------------------------------------------------------- |
| clone           | Creates a copy of the object                                               |
| equals          | Returns true if the object equals the provided object                      |
| getClass        | Gets the name of the class that the object represents                      |
| toString        | Provides a human readable string that represents the object's state        |
| wait and notify | Used to control multi-threaded concurrency by using the object as the lock |

Here is an example of a `Person` class that explicitly extends the `Object` class and overrides its `toString` method.

```java
// Note that extending Object is the default and not normally explicitly stated
public class Person extends Object {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    /**
     * Override the Object class implementation
     */
    @Override
    public String toString() {
        return String.format("My name is %s", name);
    }
}
```

Just like classes that you write, the JDK builds on the `Object` class to provide many common implementations for things like lists, sets, network, stream, database, and math. You should become familiar with the common JDK classes by exploring the documentation.

## equals

When you compare primitive types, like an `int` or `char`, you can use the `==` operator. When you want to compare objects you want to use the `equals` operator. The `Object` class `==` and `equals` implementation will only return true if you are comparing the exact same object instance. If you want to actually compare the values of an object, then you need to override the `equals` method and implement what equality means for the class. For example, here is a class that compares the `value` field in order to determine equality.

```java
public class EqualExample {
    private String value;

    public EqualExample(String value) {
        this.value = value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EqualExample that = (EqualExample) o;
        return value.equals(that.value);
    }

    public static void main(String[] args) {
        var o1 = new EqualExample("taco");
        var o2 = new EqualExample("taco");
        var o3 = new EqualExample("fish");

        System.out.println(o1 == o2);      // returns false
        System.out.println(o2 == o2);      // returns true
        System.out.println(o1.equals(o1)); // returns true
        System.out.println(o1.equals(o2)); // returns true
        System.out.println(o1.equals(o3)); // returns false
    }
}
```

## hashCode

Many of the collection objects in Java require a fast method for determining equality. The `hashCode` method returns a reasonably unique number that represents the object's values. When a collection is attempting to determine the equality of two objects, it will first call the `hashCode` method and if the returned values match, it will then call the `equals` method.

Here is an example of a simple `hashCode` implementation that gets the hash code for the underlying `value` field and multiplies it by a prime number in order to make it more somewhat unique to this class.

```java
public class HashcodeExample {
    String value;

    public HashcodeExample(String value) {
        this.value = value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashcodeExample that = (HashcodeExample) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return 71 * value.hashCode();
    }
}
```

When an object overrides both the `equals` and `hashCode` methods, you can use it with any of the JDK's collection classes that do equality checks. For example, the `HashMap` class indexes the objects that it contains by organizing them by their hash code and equality. If you do not override those functions then the HashMap will use the `Object` class's default implementation. That will result in every object in the map being considered as unique, even if they have the same field values.

## Things to Understand

- How to override methods in Java
- How to properly implement a hashCode() method
- How and why to overload a method
- What the final keyword means when applied to variables, methods and classes
- What the `toString()` method does and how to override it
- What the `equals(...)` method does and how to override it
- How to override the `hashCode()` method
- How hash tables work and why we need a `hashCode()` method

## <a name="videos"></a>Videos

- 🎥 [Classes and Objects Overview](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=8d7440ec-313d-45d1-891f-ad5f01307ab8&start=0) - [[transcript]](https://github.com/user-attachments/files/17750879/CS_240_Classes_and_Objects_Overview_Transcript.1.pdf)
- 🎥 [The `Object` Super Class](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=1de40809-379f-44fd-8ffe-ad5f01307a86&start=0) - [[transcript]](https://github.com/user-attachments/files/17750887/CS_240_Classes_and_Objects_The_Object_Super_Class_Transcript.pdf)
- 🎥 [Method Overriding](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=d47ce0c1-85e5-45a7-b56d-ad5d01512d78&start=0) - [[transcript]](https://github.com/user-attachments/files/17805005/CS_240_Method_Overriding.pdf)
- 🎥 [Overriding toString()](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=cc129f1b-ae0f-44b0-a424-ad5f01307ae4&start=0) - [[transcript]](https://github.com/user-attachments/files/17805007/CS_240_Classes_and_Objects_Overriding_toString.pdf)
- 🎥 [Overriding equals()](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=7ecb0a44-16bc-4fa7-b924-ad5f01307b29&start=0) - [[transcript]](https://github.com/user-attachments/files/17805009/CS_240_Classes_and_Objects_Overriding_equals.pdf)
- 🎥 [Implementing a Hashcode Method](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=a486e175-a53f-4aed-b436-ad5d015744ac&start=0) - [[transcript]](https://github.com/user-attachments/files/17750911/CS_240_Implementing_a_Hashcode_Method_Transcript.1.pdf)
- 🎥 [Method Overloading](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=7bec5f67-10c3-4b19-a0fc-ad640139627a&start=0) - [[transcript]](https://github.com/user-attachments/files/17805015/CS_240_Method_Overloading.pdf)
- 🎥 [Hash Tables](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=42265b8a-aced-457d-a494-ad5f0130d9a9&start=0) - [[transcript]](https://github.com/user-attachments/files/17805022/CS_240_Classes_and_Objects_Hash_Tables.pdf)
