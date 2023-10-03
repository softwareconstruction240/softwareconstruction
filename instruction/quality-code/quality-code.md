# Writing Quality Code

🖥️ [Slides](https://docs.google.com/presentation/d/13wQKz2ZLsqJVFDuoy0IJ_wLspmSpipS0/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

📖 **Optional Reading**: [Clean Code](https://www.oreilly.com/library/view/clean-code-a/9780136083238/)

Despite stereotypes to the contrary, software engineering is an art form. It requires significant creativity to envision, architect, and create quality user experiences. That same care and artistic expression should be reflected in how your organize and write your code. One quality of a distinguished engineer is that they write code that is easy to discover, read, comprehend, manipulate, and extend. Take for example the following two small programs.

**Example 1**

```java
public class ComputeIt {
public static void main(String[] args) {
System.out.println(doIt(Integer.parseInt(args[0]), 0));
}

static int doIt(int ipt, int ftr) {
    // initialize the base cases
    var a = 0; var othr = 0;
        // for each x make sure it is less than ipt otherwise break
        for (var x=0;x<ipt;x++)
        {
            if (x == 1) othr = 1;else {
               var t = a + othr;
               a = othr; ftr = ftr++;
               othr = t;
               }
        }
        return a + othr;
}
}
```

**Example 2**

```java
public class Fibonacci {
    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                var sequencePosition = Integer.parseInt(args[0]);
                var fibonacciNumber = computeFibonacci(sequencePosition);

                System.out.println(fibonacciNumber);
                return;
            } catch (NumberFormatException ignored) {
            }
        }

        System.out.println("Invalid input.\nSyntax: java Fibonacci <Sequence Position>");

    }

    /**
     * Computes the Fibonacci number for a given position in the Fibonacci
     * sequence. A <a href="https://en.wikipedia.org/wiki/Fibonacci_sequence">Fibonacci</a>
     * number is a sum of the two previous Fibonacci numbers based upon a starting value
     * of 0 and 1 for the first two positions in the sequence.
     *
     * @param sequencePosition for the Fibonacci number to calculate.
     * @return the Fibonacci number for the given sequence position.
     */
    private static int computeFibonacci(int sequencePosition) {
        var n2Value = 0;
        var n1Value = 0;
        for (var currentPosition = 0; currentPosition < sequencePosition; currentPosition++) {
            if (currentPosition == 1) {
                n1Value = 1;
            } else {
                var currentValue = n2Value + n1Value;
                n2Value = n1Value;
                n1Value = currentValue;
            }
        }
        return n2Value + n1Value;
    }
}
```

Both of these examples correctly compute the Fibonacci number for a given Fibonacci sequence position. However, the first example, while shorter, seems to go to great lengths to obfuscate its purpose and mix different coding styles. The second example seeks to intentionally define its purpose and match the Fibonacci algorithm with clarity.

Quality coding extends well beyond writing clear functions. It begins with a design that clearly expresses the problem from the user's perspective. Its structure naturally fits the real world objects contained in the problem. It considers the code base as a whole (including build, testing, and delivery systems), instead of narrowly focusing on the code that is currently under development. It relies on proper naming of objects, methods, variables, and resources. It uses documentation and comments to clarify intent rather than duplicate code. Simplicity is valued over brevity and performance, when performance is not a critical factor. Each piece of the code does one thing, and does one thing well. It does not repeat itself. It values the ability to enhance or extend the code without impacting the larger context. It sets up safeguards that enable rapid enhancement without prolonged regression testing.

Just like good art, quality code is often a subjective opinion. There are lots of good guidelines, but there are no absolute rules. A true artist knows when to break rules for the sake of a larger goal. However, if you cannot understand your own code the week after you wrote it, or if your definition of quality does not match that of your team, then there is definitely a problem.

Learning to write quality code requires effort and experience. One way to accelerate your mastery is to study quality code that others have written. Many open source projects found on GitHub provide examples of industrial quality code. You can also debug into the libraries that you commonly use, in order to learn from what they have done. Consider all of these examples with a critical eye. Ask yourself if there is a better way to structure and represent this problem.

## Names

You should give appropriate consideration to the names that you use for your classes, methods, and variables. A good name will convey meaning that makes your code more maintainable and less of a cognitive load to read. Consider the following example where you have to read the docs in order to know what is happening.

```java
/**
 * Compute the mean of the numbers and
 * ignore any values that are outliers.
 */
public int calc(int[] x, boolean ignore);
```

Because the names are terse and nondescript it encourages the reader to trust the docs, which might not actually reflect what the code actually does. It also means that as the code is altered, the programmer may feel no responsibility to be true to the original intent of the name. Before long this function may be doing much more than `calc` and the meaning of `ignore` may stray far from excluding outliers.

Instead, if reasonable function and parameter names were chosen, the code becomes self documenting and a future programmer will think twice before changing the obvious intent conveyed by the name.

```java
public int calculateMean(int[] numbers, boolean ignoreOutliers);
```

A good name will encourage the following:

1. Self documenting code
1. Cohesion
1. Encapsulation
1. Decomposition
1. Reduction of code duplication
1. Reduction of nested statements

There are standard conventions that should be followed when choosing names. For the Java language, these conventions include the following.

1. Object names are nouns
1. Method names are verbs
1. Objects begin with uppercase
1. Methods begin with lowercase
1. Variables begin with lowercase
1. Package names begin with lowercase and should be separated by dots
1. Constants are all uppercase
1. CamelCase should be used for all names, except constants

## Parameters

When declaring a method's parameters you want to make sure you consider their complexity. Having too many parameters, parameters that interact in complex ways, or parameters that are ambiguous in their meaning are a common source of bugs. Here are some suggestions to keeping your parameters clean.

| Description                                                                                                                                        | Example                                                                                  |
| -------------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------- |
| Use Enum instead of String, Integer, or Boolean                                                                                                    | `ChessPiece.KING` instead of `"king"`, `AutoSave.ENABLED`, instead of `true`             |
| Pass configuration objects rather than long parameter lists                                                                                        | `new Game(config)` instead of `new Game(3, "white", initialBoard, true, true, false, 9)` |
| Use consistent ordering (e.g. input params followed by output params)                                                                              | `scoreGame(players, board, result)` instead of `scoreGame(players, result, board)`       |
| Use return values that mutate a copy of the input parameter instead of manipulating the value of a parameter that acts as both an input and output | `Board makeMove(Board boardIn)` instead of `void makeMove(Board boardInAndOut)`          |

## Immutability

Objects that do not change after they are constructed are referred to as immutable. In order to understand that value of immutability, consider the `String` class. If `String` was not immutable then you would never be sure you still had the same string value after a sub method was called. The following example demonstrates that an unintentional side effect of calling an imaginary operation named `String.setText`.

```java

void printWithPrefix(){
    String prefix = "- "
    for (var item : list.of("a", "b", "c")) {
        System.out.println(appendPrefix(prefix, item));
    }
}

void appendPrefix(String prefix, String text) {
    prefix.setText(prefix + text);
    return prefix;
}

// Output:
// - a
// - a- b
// - a- b- c
```

Because `String` is immutable, you never have to worry about its value being changed and you can safely pass it to any function.

Immutability guarantees thread safe code because it eliminates the possibility that one thread can be modifying an object at the same time a different thread is reading it.

## Declaring Variables

When you are declaring variables in want to consider the following.

1. Don't reuse variables for different purposes. Declaring a variable with the `final` keyword makes it clear that it should not be reused.
1. Declare your variables close to where they are used. This makes it easier to determine the variables scope and type.
1. Always initialize your variables. Leaving a variable uninitialized creates the opportunity for a null pointer exception or other unexpected behavior.
1. Remove unused variables. Leaving an unused variable declaration in your code makes it more difficult to manage your code.

## Code Layout

You want your code to read like a newspaper. The most interesting things should be on the front page in a very short concise representation. You then can move to a section of the paper that is of interest, and turn to the following pages if you want the details of a story.

Likewise, in your code you want to follow an intuitive and consistent ordering.

### Application Layout

1. Keep all your application code in a single directory
1. Include an application README.md file
1. Include a .gitignore file
1. Include a license file
1. Have an obvious entry point to your code base. For example, a `Main` class in your top level package.
1. Decompose sub packages into lower level classes and concrete implementations
1. Maintain cohesion within a package
1. Include package level JavaDocs as useful

### Class Layout

1.  Class JavaDocs as necessary
1.  Private fields
1.  Constructors
1.  Public methods
1.  Public override methods
1.  Private methods

## Method Characteristics

Methods are a powerful coding abstraction. You should maximize the value of methods according to the following principles.

| Principle                                      | Description                                                                                                                                   |
| ---------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------- |
| Appropriate method length                      | The method should be short enough that you can understand it with a quick scan. Usually this means less than 20 lines.                        |
| Embrace Decomposition                          |                                                                                                                                               |
| Self documentation                             |                                                                                                                                               |
| Avoiding duplication                           |                                                                                                                                               |
| Avoiding expression nesting                    |                                                                                                                                               |
| Declare variables where they are used          |                                                                                                                                               |
| Prefer single a return statement               | Having multiple return statements in different express blocks forces the reader to carefully examine the code in order to understand the flow |
| Don't recycle variables for different purposes | If `time` is used to represent the time of day, don't reuse it later to represent the time spent executing a function                         |
| Appropriate line length                        | Keep your line lengths short enough that you can understand it with out scrolling or parsing complex expressions                              |

## Style

In the early days of programming there were endless debates about where to put your curly braces, how much whitespace to put between functions, and if your code should be indented with tabs or spaces. However, now days, most professionals agree that stylistic differences are not as important as consistency. This is true both within a team, and across the industry. For that reason, most teams choose an industry, or language, idiomatic formatter and just let it do its job.

## Things to Understand

- The importance of a good name.
- Three reasons to create methods.
- How to decompose a complex algorithm into sub methods.
- How the use of good names and decomposition can reduce or eliminate the need to write inline comments.
- How to properly use parameters.
- How to properly layout your code to maximize readability.
- How to make expressions easy to read and understand.
- How to write good pseudocode.

## Videos

- 🎥 [Introduction](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=ea73fd31-9c36-40d5-9e46-ad6b0151100a&start=0)
- 🎥 [Cohesion and Method Naming](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=776f9b2c-49ce-4645-93eb-ad6b015221a7&start=0)
- 🎥 [Three Reasons for Creating Methods](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=22c699ef-4f72-4d58-9e29-ad6b0153913d&start=0)
- 🎥 [Method Parameters](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=d1a57a5c-9408-4b14-b1f6-ad8e014aa62d&start=0)
- 🎥 [Initializing Data](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=db14c566-28cf-4635-944f-ad6b01573031&start=0)
- 🎥 [Code Layout](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=89391851-a002-40f4-8740-ad6b015871b5&start=0)
- 🎥 [Pseudo-Code](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=56a43caf-5554-4d6d-a845-ad6d013f861a&start=0)
- 🎥 [Variable Names](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=6297fb45-e525-4e3b-a8a6-ad6d0140afb3&start=0)

## Demonstration code

📁 [x](example-code/)
