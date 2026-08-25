# Java Lambdas

🖥️ [Slides](https://docs.google.com/presentation/d/16JLfaKkdoYEe5CyN61rQ5n1hj0BgRhjD/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

📖 **Required Reading**: Core Java for the Impatient

- Chapter 3: Interfaces and Lambda Expressions. _Only_: section 3.4

🖥️ [Lecture Videos](#videos)

### 🔑 Key points

- Java Lambdas are concise representations of anonymous functions
- Java Method References are a more concise representation for pass through lambda functions

---

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
static class FrenchSpeaker implements Speaker {
    public String sayHello() {
        return "bonjour";
    }
}

// GermanSpeaker.java
static class GermanSpeaker implements Speaker {
    public String sayHello() {
        return "hallo";
    }
}

// EnglishSpeaker.java
static class EnglishSpeaker implements Speaker {
    public String sayHello() {
        return "hello";
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
speak(new Speaker() {
    public String sayHello() {
        return "bonjour";
    }
});
speak(new Speaker() {
    public String sayHello() {
        return "hallo";
    }
});
speak(new Speaker() {
    public String sayHello() {
        return "hello";
    }
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
speak(() -> "bonjour");
speak(() -> "hallo");
speak(() -> "hello");
```

Notice how compact the lambda function representation is. If you need to provide more than a single line when defining the body of your lambda function, you can include curly braces, but you must also explicitly include the `return` keyword.

```java
(a, b) -> {
  return a + b;
}
```

### Syntax Structure
*   **Basic Syntax:** Consists of three parts: `(parameters) -> { body }`.
*   **Parameter variations:**
    *   Zero parameters: `() -> System.out.println("Hello")`
    *   One parameter: `x -> x * x` (Parentheses are optional for a single inferred parameter).
    *   Multiple parameters: `(a, b) -> a + b`
*   **Body variations:**
    *   **Expression body:** `(a, b) -> a + b` (Implicitly returns the result).
    *   **Block body:** `(a, b) -> { return a + b; }` (Requires explicit braces and `return` keyword).

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

*   **Scope:** Lambdas can access variables from their enclosing scope.
*   **Effectively Final:** Any local variable accessed inside a lambda must be `final` or "effectively final" (meaning its value is never changed after initialization).
```java
    public static void main(String[] args) {
        var englishSpeaker = speakerFactory("hello");
        speak(englishSpeaker);
    }

    static Speaker speakerFactory(String msg) {
        return () -> msg;
    }
```


## Method References

Method references are a specialized form of lambda expressions that allow you to further simplify your code when a lambda expression does nothing but call an existing method. They act as "syntactic sugar," making the code more readable by pointing directly to the method by name rather than describing how to call it.

When you use a method reference, the Java compiler automatically infers the parameters from the functional interface's abstract method. If the lambda expression `(s) -> System.out.println(s)` is replaced with `System.out::println`, the compiler understands that the argument passed to the functional interface should be passed directly into the `println` method.

Consider a scenario where we want to sort a list of strings while ignoring case sensitivity. Using a standard lambda expression vs. a method reference demonstrates the gain in clarity:

```java
List<String> names = Arrays.asList("Alice", "bob", "Charlie", "david");

// Approach 1: Using a Lambda Expression
Collections.sort(names, (s1, s2) -> s1.compareToIgnoreCase(s2));

// Approach 2: Using an Instance Method Reference of an Arbitrary Object
// This is cleaner and more descriptive of the intent
names.sort(String::compareToIgnoreCase);
```

Notice that because the lambda function is simply passing the exact parameters through

```java
(s1, s2) -> s1.compareToIgnoreCase(s2)
```

it can be replaced with the more concise **Method Reference** syntax.

```java
String::compareToIgnoreCase;
```

## ☑ Exercise


```masteryls
{"id":"8d174ea4-f27d-4e6b-a4bb-97b6392042a2","title":"Invalid Lambda Syntax for Sorting","type":"multiple-choice"}
Given 

`List<String> fruits = Arrays.asList("Apple", "Orange", "Banana");`

which of the following is an **invalid** lambda expression replacement for an anonymous inner class `Comparator` when calling the `sort` method?

- [ ] `fruits.sort((a, b) -> a.compareTo(b));`
- [ ] `fruits.sort(String::compareTo);`
- [x] `fruits.sort((f1, f2) -> { f1.compareTo(f2); });`
- [ ] `fruits.sort((String f1, String f2) -> f1.length() - f2.length());`
```



```masteryls
{"id":"7d844be9-f784-47bd-98d1-6c16d71514af", "title":"Lambda Functions", "type":"essay" }
What is your opinion about the usefulness of Lambda Functions?
```


## Videos

- 🎥 [Lambda Expressions Overview (6:47)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=6cfff192-6903-40b0-bacb-b053010e7658) - [[transcript]](https://github.com/user-attachments/files/18563796/CS_240_Lambda_Expressions_Overview_Transcript.pdf)
- 🎥 [How Java Lambdas Work (7:14)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=30d50a0f-b462-4d41-b7c6-b05301107ef8) - [[transcript]](https://github.com/user-attachments/files/18563807/CS_240_How_Java_Lambdas_Work_Transcript.pdf)
- 🎥 [Lambda Syntax (2:56)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=66a7b726-985e-4f7e-be87-b0530112a93e) - [[transcript]](https://github.com/user-attachments/files/18563809/CS_240_Lambda_Syntax_Transcript.pdf)
- 🎥 [Function / Lambda Variables (2:47)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=3c58fb2e-dc04-4dc4-b040-b0530113a8f6) - [[transcript]](https://github.com/user-attachments/files/18563817/CS_240_Function_Lambda_Variables_Transcript.pdf)
- 🎥 [Creating APIs with Lambdas (5:10)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=4f332bfb-9836-41d8-acc0-b0530114dcb2) - [[transcript]](https://github.com/user-attachments/files/18563835/CS_240_Creating_APIs_with_Lambdas_Transcript.pdf)
- 🎥 [Using Generic Interfaces Example Revisited (2:10)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=ceee1a53-7ff3-4a71-8b26-b0530116817b) - [[transcript]](https://github.com/user-attachments/files/18563838/CS_240_Using_Generic_Interfaces_Example_Revisited_Transcript.pdf)
- 🎥 [Use of Lambdas in Existing Classes (3:22)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=19b04002-8028-4e50-bd68-b0530117654d) - [[transcript]](https://github.com/user-attachments/files/18563840/CS_240_Use_of_Lambdas_in_Existing_Java_ClassesTranscript.pdf)
- 🎥 [Method References (4:16)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=465b91d6-210d-4838-b266-b0530119161e) - [[transcript]](https://github.com/user-attachments/files/18563848/CS_240_Method_References_Transcript.pdf)
