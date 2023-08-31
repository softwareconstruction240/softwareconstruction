# Java Object Class

All classes in Java can have a single super class that they derive from. The default super class is called the `Object` class. If you do not explicitly specify what super class your class extends then you will automatically inherit the `Object` class.

When you extend another class, you inherit all of the functionality of that class. Often times this means you can overload the methods of the super class in order to alter its functionality. The base `Object` class contains the following methods that you can override.

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

When you compare primitive types, like an `int` or `char`, you can use the `==` operator. When you want to compare objects you want to use the `equals` operator. The `Object` class `==` and `equals` implementation will only return true if you are comparing the exact same instance. If you want to actually compare the values of an object then you need to override the `equals` method and implement what equality means for the class. For example, here is a class that compares the `value` field in order to determine equality.

```java
public class EqualExample {
    private String value;

    public EqualExample(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EqualExample cmp) {
            return value.equals(cmp.value);
        }
        return false;
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

## hashcode

Many of the collection objects in Java require a fast method for determining equality. The `hashcode` method returns a reasonably unique number that represents the object's values. When a collection is attempting to determine equality to two objects, it will first call the `hashcode` method and if the returned values match, it will then call the `equals` method.

Here is an example, of a simple `hashcode` implementation that gets the hash code for the underlying `value` field and multiplies it by a prime number in order to make it more unique to this class.

```java
public class HashcodeExample {
    String value;

    public HashcodeExample(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return 71 * value.hashCode();
    }
}
```

## Concepts

The concepts we will cover as part of your work on the spelling corrector include the following.

1. What the `toString()` method does and how to override it
1. What the `equals(...)` method does and how to override it
1. How hash tables work and why we need a `hashcode()` method
1. How to override the `hashcode()` method

## Videos

- 🎥 [Classes and Objects Overview](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=8d7440ec-313d-45d1-891f-ad5f01307ab8&start=0)
- 🎥 [The `Object` Super Class](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=1de40809-379f-44fd-8ffe-ad5f01307a86&start=0)
- 🎥 [Overriding toString()](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=cc129f1b-ae0f-44b0-a424-ad5f01307ae4&start=0)
- 🎥 [Overriding equals()](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=7ecb0a44-16bc-4fa7-b924-ad5f01307b29&start=0)
- 🎥 [Hash Tables](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=42265b8a-aced-457d-a494-ad5f0130d9a9&start=0)
