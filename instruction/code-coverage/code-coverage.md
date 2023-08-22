# Code Coverage

🖥️ [Slides](https://docs.google.com/presentation/d/14fDhNHsnU-knkVYmfNDsIwZQ027tu_DB/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

When you are trying to build confidence that you code is working, you write tests to exercise the code. Once you begin using test driven development you start to rely on the tests to tell you when something is broken. This can lead to a state of false confidence if your tests don't actually cover all of the paths through your code. This happens when you have tests that only cover some conditional branches, when the tests don't cover the exceptional branches, or when not all of the possible inputs are represented.

Modern integrated development environments often provide tools for determining the amount of code coverage that your tests provide. You can use the code coverage tool built into IntelliJ to see how much you should trust the tests in your project. To access the coverage tool you choose the `Run test with Coverage` option from the test execution actions. This will pop up a window that shows you how much of your code is being executed by the test. The coverage tool shows a green marker for all the lines that are begin executed, and a red marker for lines that are being skipped by the test.

The following video shows a function called `branch` that has three paths. In order to cover all the code of the function, our test must provide a `true`, `false`, and `null` value for the parameter. You can see the coverage increasing to 100% as we successively enable all of the necessary testing code required for all of the branches.

![Code coverage](codeCoverage.gif)

## How much coverage?

There subject of what percentage of coverage is necessary to enable confidence in your code is a very opinionated topic. Some developers feel that anything less that 100% is a problem. Others feel that obtaining 100% creates unmaintainable tests, causes harmful overhead in the actual code, and decrease productivity. The correct answer to this question is one that you should decide as a team based upon the specifics of the project you are working on.

You should note that just because you have 100% coverage, does not mean that all paths through the code are exercised and that you are guaranteed that your code is correct. Consider the case where you have a function that takes an object and references it.

```java
public static void oneHundredPercentCoverage(Object obj) {
    obj.toString();
}
```

You can write a test that calls `oneHundredPercentCoverage` and provides 100% coverage, but unless the test includes a call where `obj` is null, you will have an unhandled `RuntimeException` waiting to happen in your code.

Code coverage can also give a false sense of security if all of your testing is simple unit tests. Many bugs only happen when your application is running from end to end. This includes a front end client, middleware business logic, and data services. Unit tests often stub out, or mock, input and output in order to isolate the test. That means it can easily miss real world parameter values, and failure, that occur only when the application is executing in a real world environment.

With all of that said, code coverage does provide a important indicator of the value your testing is providing. It can also point you to locations in your code that are problematic and need review. This is especially true for code that is not covered, and is highly complex or often executed.
