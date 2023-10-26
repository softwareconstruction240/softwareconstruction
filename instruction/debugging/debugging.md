# Debugging

🖥️ [Slides](https://docs.google.com/presentation/d/14CiV7TwmAG-vEWWsQQtaP_-Hx-pqJlGR/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

Debugging is one of the most important development skills that you can master. If you can learn how to rapidly reproduce a problem, narrow down its cause, and quickly implement a solution, you will dramatically increase your value as a software engineer.

Knowing what debugging tools are available and how to effectively employ them is key to your success. Common categories of debugging tools include:

1. Visual debuggers
1. Stack traces
1. Logs
1. Metrics
1. Customer reports
1. Development and staging environments
1. Unit, system, and end to end tests
1. Code reviews
1. Performance profilers

The more you think about debugging as applying the scientific method, the more you will become adept at the debugging process.

1. Concisely state the problem
1. Reproduce the problem with a unit test
1. Isolate a problem to its simplest representation
1. Step through the tested code
1. Implement a solution
1. Verify that the unit test passes
1. Verify that all tests pass

In this instruction we focus on `visual debuggers`. You are encouraged to become experts with the debugger that is available for your development environment. In our case this is IntelliJ. Learn how to quickly execute the debugger, use it with only keystrokes, maximize the use of breakpoints, inspect variables and execution stacks, and isolate a reproduction of the problem, possibly with new unit tests.

## Example Debugging

```java
List<String> filterToCWordsAnyLengthAndAWordsGreaterThanFive(List<String> words) {
    var result = new ArrayList<String>();
    try {
        for (var i = words.size(); i > 0; i--) {
            var word = words.get(i);
            if (word.matches("^(c|a).{5,100}$")) {
                result.add(word);
            }
        }
    } catch (Exception ignore) {
    }
    return result;
}
```

## Generating Tests

![Generate Test](generate-test.png)

```java
@Test
void filterToCWordsAnyLengthAndAWordsGreaterThanFive() {
}
```

```java
@Test
void filterToCWordsAnyLengthAndAWordsGreaterThanFive() {
    var words = List.of("cattle", "dog", "appalachian", "apple", "pig");
    var results = BugExample.filterToCWordsAnyLengthAndAWordsGreaterThanFive(words)
            .stream().sorted().toList();

    var expected = List.of("cattle", "appalachian").stream().sorted().toList();
    assertIterableEquals(expected, results);
}
```

## Stepping Through Code

## Breakpoints

### Conditional Breakpoints

When you create a breakpoint in IntelliJ you can specify conditions such as the required value of a variable before the breakpoint will trigger.

![Conditional Breakpoint](conditional-breakpoint.png)

## Examining up the Stack

![Stack](stack.png)

## Breaking on Exceptions

## Error Messages

```text
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 5
	at java.base/java.util.ImmutableCollections$ListN.get(ImmutableCollections.java:680)
	at debugging.BugExample.filter(BugExample.java:16)
	at debugging.BugExample.main(BugExample.java:9)
```

## Enhancing Tests

As you fix bugs you will often discover other problems that haven't been reported yet. For example, our specification says that you can have `a` words of length greater than five, but if we modify our test to try long `a` words we will discover that there is still a bug.

```java
@Test
void filterToCWordsAnyLengthAndAWordsGreaterThanFive() {
    var big = "a";
    for (var i = 0; i < 1005; i++) {
        big += 'a';
    }

    var words = List.of("cattle", "dog", "appalachian", "apple", "pig", big);
    var results = BugExample.filterToCWordsAnyLengthAndAWordsGreaterThanFive(words);
    results = results.stream().sorted().toList();

    var expected = List.of("cattle", "appalachian", big);
    expected = expected.stream().sorted().toList();
}
```

## Things to Understand

- How to set a breakpoint
- How to step through code and into method calls
- How to set conditional breakpoints
- How to view the values and local and instance values while stepping through code
- How to view the current call stack
- How to set watches

## Videos

- 🎥 [Introduction](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=f2279dc0-fd71-46af-ab7a-ad6d01516f20&start=68.656585)
- 🎥 [Debugging in IntelliJ](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=6ff3df28-71f9-435e-915e-ad6d01535f13&start=253.821601)
