# Java Inner Classes

📖 **Required Reading**: Core Java for the Impatient

- Chapter 2 section 2.7 Nested Classes
- Chapter 3 section 3.9 Local and Anonymous Classes

🖥️ [Lecture Videos](#videos)

Normally in Java, a class must be defined at the top level of a file that has the same name as the class. However, there are times when a class is only used within the context of another class, method, or scope. This is where inner, or nested, classes come into play.

There are four types of Inner (Nested) Classes in Java.

- **Static Inner Classes** - Defined within the scope of another class.
- **Inner Classes** - Defined within the scope of another class and shares the `this` pointer.
- **Local Inner Classes** - Defined within the scope of a block and shares scope variables.
- **Anonymous Inner Classes** - Defined without a class name and shares scope variables.

## Static Inner Classes

A static inner class is defined inside another class. Commonly this is done as a utility class within the parent, as an aggregation piece of the parent's fields, or as a public class that in necessary for the operation of the outer class.

You can think of a static inner class as being a convenient way to declare a class, without creating a new file, that is tightly coupled to the outer class.

Because the inner class is declared as being static, it is completely independent of the outer class, and does not have access to outer class's `this` pointer.

```java
public class StaticOuterExample {
    public static void main(String[] args) {
        System.out.println(new StaticOuterExample());
    }

    private StaticInnerExample inner = new StaticInnerExample();

    private static class StaticInnerExample {
        public String toString() {
            var inner = this.getClass().getName();
            return String.format("Inner: %s", inner);
        }
    }

    public String toString() {
        return inner.toString();
    }
}
```

**Output**

```sh
Inner: StaticOuterExample$StaticInnerExample
```

## Inner Classes

An inner class, that is not marked as static, is similar to a static inner class, but has access to the `this` pointer of the outer class. To access the outer classes `this` pointer, the inner class prefixes the `this` pointer with the outer class's name.

```java
public class OuterExample {
    public static void main(String[] args) {
        System.out.println(new OuterExample());
    }

    private InnerExample inner = new InnerExample();

    private class InnerExample {
        public String toString() {
            var inner = this.getClass().getName();
            // Note the use of the outer class's this pointer.
            var outer = OuterExample.this.getClass().getName();
            return String.format("Inner: %s has access to Outer: %s", inner, outer);
        }
    }

    public String toString() {
        return inner.toString();
    }
}
```

**Output**

```sh
Inner: OuterExample$InnerExample has access to Outer: OuterExample
```

## Local Inner Class

A local inner class is like a normal inner class, but are declared within the scope of the block. A declaration scope may be a method, or something like a `for loop`. An important property of local inner classes is that, whatever variables exist within the declaration scope are also available to the local inner class.

```java
public class LocalOuterExample {
    public static void main(String[] args) {
        System.out.println(new LocalOuterExample());
    }

    public String toString() {
        var outerLocalVar = "outerLocalVar";

        class InnerExample {
            public String toString() {
                var inner = this.getClass().getName();
                // Note the use of the outer class's this pointer and scope variables.
                var outer = LocalOuterExample.this.getClass().getName();
                return String.format("Inner: %s has access to Outer: %s, and variables: %s", inner, outer, outerLocalVar);
            }
        }

        InnerExample inner = new InnerExample();

        return inner.toString();
    }
}
```

**Output**

```sh
Inner: OuterExample$1InnerExample has access to Outer: OuterExample, and variables: outerLocalVar
```

## Closure

The ability for a local inner class to access the variables of its declaring scope is a programming concept called `closure`. This is useful when you want to create object factories that can be parameterized by the environment that they are declared in. Here is an example of using `closure` to create objects that can speak different languages.

Notice that the object returned from factory method can access the `helloPhrase` parameter that was passed to the factory method even after the factory method has gone out of scope.

```java
public class Closure {
    public interface Speaker {
        String sayHello();
    }

    public static void main(String[] args) {

        var spanish = SpeakerFactory("Hola");
        var german = SpeakerFactory("Hallo");

        System.out.printf("Spanish: %s\nGerman: %s", spanish.sayHello(), german.sayHello());
    }

    private static Speaker SpeakerFactory(String helloPhrase) {
        class InnerExample implements Speaker {
            public String sayHello() {
                return helloPhrase;
            }
        }

        return new InnerExample();
    }
}
```

**Output**

```sh
Spanish: Hola
German: Hallo
```

## Anonymous Inner Class

An anonymous inner class, is defined inline by referencing an interface, followed by the implementation. This is useful in cases where you have commonly used interfaces that only have a single method. This saves you from creating lots of little classes that are only used once. In the following example we can create two different implementations of the `Speaker` interface by defining the `sayHello` method inline.

Note that anonymous inner classes also have closure on the scope that they were declared in.

```java
public class AnonymousExample {
    public interface Speaker {
        String sayHello();
    }

    public static void main(String[] args) {
        var spanish = new Speaker() {
            public String sayHello() {
                return "Hola";
            }
        };
        var german = new Speaker() {
            public String sayHello() {
                return "Hallo";
            }
        };

        System.out.printf("Spanish: %s\nGerman: %s", spanish.sayHello(), german.sayHello());
    }
}
```

## Things to Understand

- What are static inner classes and what benefits do they provide?
- What can regular inner classes do that static inner classes cannot?
- What can local inner classes do that regular inner classes cannot?
- What does it mean for a variable to be final or effectively final?
- Why do local inner classes have a restriction on which local variables they can access?
- How do you define an anonymous inner class and what benefit does it provide over a local inner class?

## Videos

- 🎥 [Introduction (6:23)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=1a6ec347-24b8-41db-9dd8-ad72014f1ec1&start=0) - [[transcript]](https://github.com/user-attachments/files/17780969/CS_240_Inner_Class_Introduction.pdf)
- 🎥 [Static Inner Classes (5:55)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=c02cdeb8-5929-4b80-b83a-ad72015107bc&start=0) - [[transcript]](https://github.com/user-attachments/files/17780972/CS_240_Static_Inner_Classes.pdf)
- 🎥 [Inner Classes (2:06)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=201871e8-f294-40d4-944c-ad720152d070&start=0) - [[transcript]](https://github.com/user-attachments/files/17804795/CS_240_Inner_Classes.pdf)
- 🎥 [Local Inner Classes (7:09)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=817e3e88-8d19-4ffa-85b4-ad7201539033&start=0) - [[transcript]](https://github.com/user-attachments/files/17804799/CS_240_Local_Inner_Classes.pdf)
- 🎥 [Anonymous Inner Classes (5:16)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=7faf22b0-00df-4429-818f-ad720155ad20&start=0) - [[transcript]](https://github.com/user-attachments/files/17804804/CS_240_Anonymous_Inner_Classes.pdf)

## Demonstration code

📁 [Iterator.java](example-code/Iterator.java)

📁 [anonymousInnerClass](example-code/anonymousInnerClass)

📁 [innerClass](example-code/innerClass)

📁 [localInnerClass](example-code/localInnerClass)

📁 [staticInnerClass](example-code/staticInnerClass)
