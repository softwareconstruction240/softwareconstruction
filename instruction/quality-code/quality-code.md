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
