# Java Lambdas

🖥️ [Slides](https://docs.google.com/presentation/d/16JLfaKkdoYEe5CyN61rQ5n1hj0BgRhjD/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

📖 **Required Reading**: Core Java SE 9 for the Impatient

- Chapter 3: Interfaces and Lambda Expressions. _Only_: section 3.4

The Java programming language originally required everything to be defined within the scope of a class. That meant that defining a simple utility, or one off function, outside of a class was not allowed. This becomes problematic when you wanted to write small one line functions that do things like handle a mouse click event, or run a simple concurrent task. By forcing all functions to be defined in a class, Java effectively forced developers to new class, in a new file, for each single line function they wanted to implement. You can image hundreds or even thousands of these little files cluttering up a large sized application.

Consider the following `Speaker` interface,

```java
interface Speaker {
    String sayHello();
}
```

and a method that takes a `Speaker` interface like the following.

```java
void speak(Speaker speaker) {
  System.out.println(speaker.sayHello());
}
```

Assume you want to create speakers for different languages. To do this you would need to create a new class for every language that you wanted to implement. Each of these would implement the `Speaker` interface and be in their own Java class file.

```java
// FrenchSpeaker.java
class FrenchSpeaker implements Speaker {
    String sayHello() {
      return 'bonjour';
    }
}

// GermanSpeaker.java
class GermanSpeaker implements Speaker {
    String sayHello() {
      return 'hallo';
    }
}

// EnglishSpeaker.java
class EnglishSpeaker implements Speaker {
    String sayHello() {
      return 'hello';
    }
}
```

You could then use each of these classes in your calls to `speak`.

```java
speak(new FrenchSpeaker());
speak(new GermanSpeaker());
speak(new EnglishSpeaker());
```

You can simplify this problem somewhat by using anonymous classes. This removes the need for all the extra class files, but it still creates a lot of redundant overhead in your code.

```java
speak((new Speaker {
    String sayHello() {
      return 'bonjour';
    });
speak(new Speaker {
    String sayHello() {
      return 'hallo';
    });
speak(new Speaker {
    String sayHello() {
      return 'hello';
    });
```

To overcome this deficiency in the language, Java introduced `Lambda` functions. In Java, Lambda functions are effectively a syntactic simplification of anonymous inner classes that implement an interface with a single function. An interface of this type is called a `Functional Interface`.

If you have a parameter that is expecting a `Functional Interface` then you can use the abbreviated lambda syntax. The lambda syntax provides the input parameters on the left and the return value on the right, separated by an `->` symbol. A simple lambda function that takes two numbers as input and then returns their sum would look like the following.

```java
(a, b) -> a + b
```

Notice that you do not have to declare the types, include the block curly braces, or use the return keyword. Those are all implied by the syntax and the `Functional Interface` that the lambda function implements.

For the example we described above, we could reduce our anonymous speaker classes to be the following.

```java
speak(() -> 'bonjour');
speak(() -> 'hallo');
speak(() -> 'hello');
```

Notice how compact the lambda function representation is. If you need to provide more than a single line when defining the body of your lambda function, you can include curly braces, but you must also explicitly include the `return` keyword.

```java
(a, b) -> {
  return a + b;
}
```

## Lambdas and JDK Collections

Because many of the JDK collection classes provide operations that require a `Functional Interface`, they can use the simplified lambda syntax. For example, the `List.removeIf` operation requires an object that implements the `Predicate` interface. This interface is defined as the following.

```java
@FunctionalInterface
public interface Predicate<T> {
    boolean test(T t);
}
```

That means we can provide either the long form anonymous class implementation,

```java
var list = new ArrayList<>(List.of(1, 3));
list.removeIf(new Predicate<Integer>() {
    public boolean test(Integer n) {
        return n > 2;
    }
});
```

or the shortened form lambda syntax.

```java
var list = new ArrayList<>(List.of(1, 3));
list.removeIf(n -> n > 2);
```

Either way is equivalent, but the lambda represent is much more concise and easy to digest once you are familiar with it.

## Closure

Just like anonymous classes, lambda functions also support closure. That means you can reference variables and parameters that are declared in the scope that defines the lambda function. This is true even if the lambda function is passed out of the defining scope.

```java
    public static void main(String[] args) {
        var englishSpeaker = speakerFactory("hello");
        speak(englishSpeaker);
    }

    static Speaker speakerFactory(String msg) {
        return () -> msg;
    }
```

## Demonstration code

📁 [ComparatorWithLambda.java](example-code/ComparatorWithLambda.java)

📁 [ComparatorWithoutLambda.java](example-code/ComparatorWithoutLambda.java)

📁 [RemoveIfExample.java](example-code/RemoveIfExample.java)

📁 [StringManipulator.java](example-code/StringManipulator.java)

📁 [StringSelectorExample.java](example-code/StringSelectorExample.java)
