# Code Coverage

🖥️ [Slides](https://docs.google.com/presentation/d/14fDhNHsnU-knkVYmfNDsIwZQ027tu_DB/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

🖥️ [Lecture Videos](#videos)

Once you begin using test driven development, you start to rely on tests to tell you when something is broken. This can lead to a state of false confidence if your tests don't actually cover all of the paths through your code. This happens when you have tests that only cover some conditional branches, when the tests don't cover the exceptional branches, or when not all of the possible range of inputs are represented.

Modern integrated development environments (IDEs) often provide tools for determining the amount of code coverage that your tests provide. You can use the code coverage tool built into IntelliJ to see how much you should trust the tests in your project. To access the coverage tool, you choose the `Run test with Coverage` option from the test execution actions. This will pop up a window that shows you how much of your code is being executed by the test. The coverage tool shows a green marker next to all the code that was executed, and a red marker next to lines that were skipped by the test.

The following video shows a function called `branch` that has three paths. In order to cover all the code of the function, the test must provide a `true`, `false`, and `null` value for the parameter. You can see the coverage increasing to 100% as we successively enable all of the necessary testing code required for all of the branches.

![Code coverage](codeCoverage.gif)

## How much coverage?

There subject of what percentage of coverage is necessary to enable confidence in your code is a very opinionated topic. Some developers feel that anything less that 100% is a problem. Others feel that obtaining 100% creates unmaintainable tests, causes harmful overhead in the actual code, and decrease productivity. The correct answer to this question is dependent on the specifics of the project you are working on.

You should note that just because you have 100% coverage, does not mean that all paths through the code are fully exercised, or that your code is correct. Consider the case where you have a function that takes an object and references it.

```java
public static void oneHundredPercentCoverage(Object obj) {
    obj.toString();
}
```

You can write a test that calls the function and obtains 100% coverage, but unless the test includes a call where `obj` is null, you will have an unhandled `RuntimeException` waiting to happen to your users.

Code coverage can also give you a false sense of security if all of your testing is simple unit tests. Many bugs only happen when your application is running from end to end. This includes a front end client, middleware business logic, and data services. Unit tests often stub out, or mock, input and output in order to isolate the test. That means it can easily miss real world parameter values, and failure cases, that occur only when the application is executing in a real world environment.

With all of that said, code coverage does provide a important indicator of the value your testing is providing. It can also point you to locations in your code that are problematic and need review. This is especially true for code that is highly complex, has a high branching factor, or has paths the rarely get executed.

## Videos

- 🎥 [Code Coverage Overview (8:18)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=5fc1d70d-9c34-41f1-b195-b1a001161540&start=0) - [[transcript]](https://github.com/user-attachments/files/17707673/CS_240_Code_Coverage_Overview_Transcript.pdf)
- 🎥 [Code Coverage Tools (1:38)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=c58109b6-ea78-4e98-8554-b1a001187fdb&start=0) - [[transcript]](https://github.com/user-attachments/files/17707688/CS_240_Code_Coverage_Tools_Transcript.pdf)
- 🎥 [Intellij Code Coverage (3:49)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=cbb47ecd-2023-4911-933b-b1a001195835&start=0) - [[transcript]](https://github.com/user-attachments/files/17707699/CS_240_IntelliJ_Code_Coverage_Transcript.pdf)
