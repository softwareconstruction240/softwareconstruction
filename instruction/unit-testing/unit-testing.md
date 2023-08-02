# Unit Testing

🖥️ [Slides](https://docs.google.com/presentation/d/1gRoHgp1j28GHaJvzHJlqnrqtwfWfZz3F/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

📖 **Required Reading**: [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)

- Sections 1 through 1.4.3
- Sections 2 through 2.2
- Section 2.4

Test driven development (TDD) was introduced in the late 1990s as part of the [extreme programming](https://en.wikipedia.org/wiki/Extreme_programming) wave. The idea is that you begin writing software by creating tests that represent the consumer of your software. You then use the tests to drive the development of your code. When the tests pass you know that your code is complete.

TDD has been proven to decrease development time, provide documentation and examples for your code, result in less bugs, and prevent against the introduction of future bugs. Additionally, by writing your tests by focusing on the consumer of your code, you tend to design better interfaces and accurate domain models.

Today, TDD is a common industry practice that you will be expected to use on a daily basis. However, it takes effort to learn how to write tests that are effective and efficient. Making this a standard part of your development process will give you a significant advantage as you progress in your professional career.

JUnit is a common library that is used for testing Java code. JUnit uses a combination of annotations and assertion functions to provide its basic functionality. A simple JUnit test would look like the following.

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExampleTests {
    @Test
    public void simpleAssertionTest() {
        assertEquals(200, 100 + 100);
        assertTrue(100 == 2 * 50);
        assertNotNull(new Object(), "Response did not return authentication String");
    }
}
```

## Things to Understand

- Why unit testing is important
- How to write unit tests using the JUnit testing framework
- How to run Junit tests from Intellij
- Special considerations for testing database code

## Videos

- 🎥 [Why We Need Unit Testing](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=c5707c4c-77d9-43d7-b96b-ad6b014612ca&start=0)
- 🎥 [Unit Testing Overview](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=bb1884b3-55ab-4a8b-be05-ad6b01477df8&start=0)
- 🎥 [The JUnit Testing Framework](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=071e47be-a746-4e74-afd5-ad6b0149b2b8&start=0)
- 🎥 [Unit Testing Database Code](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=6d8bf3b3-3ddd-4f3d-b90d-ad6b014f2bb7&start=0)
- 🎥 [Correction - Unit Testing Database Code](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=9178d92a-e41b-48f4-8e68-adf8015d7a91&start=0)

## Demonstration code

📁 [main](example-code/main/)

📁 [test](example-code/test/)
