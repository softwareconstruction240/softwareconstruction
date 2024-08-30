# Defensive Programming

🖥️ [Slides](https://docs.google.com/presentation/d/1VOvCn5605TAaCC4DBZBH-B4YSDZij0UF/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

📖 **Required Reading**: Core Java for the Impatient

- Chapter 5: Exceptions, Assertions, and Logging. _Only Section 2 Assertions - 2.2: Enabling and Disabling Assertions_

🖥️ [Lecture Videos](#videos)

Java introduced the `assertion` keyword with version 1.4.

## Protecting Public Access

It is critical that you defend yourself from any input provided to your application by a user or external application. Failure to do this can allow a user to breach your application security, or cause your application to act erratically or completely fail.

There are two types of action you generally take to ensure that user inputs are safe.

- **Validation** - Asserting that the input meets the parameters of the request. This can include out of bounds parameters such as a negative value where only positive values are allowed, or text that is beyond the expected length of the input.
- **Sanitization** - Modifying user input in order to make it fit the parameters of the request. Sanitation assumes that request is benignly malformed and seeks to provide a satisfactory response. Type casting, defaulting to a defined value, and removing input that otherwise would be harmful are all considered sanitization.

You must be careful to not make your application so flexible with validation and sanitization that you enlarge the attack surface of your application to the point where a malevolent user can exploit your validation with a denial of service attack, or penetrate your sanitization with an injection attack.

Commonly, protecting public access, involves quickly executing tests that reject a user's request by throwing an exception or returning a failure error. For example, if you had a public HTTP endpoint, that required the input to be of alphabetic characters of a certain length then you would test that requirement and return a 400 error if violated.

```java
public class DefensiveExample {
    public static void main(String[] args) {
        Spark.get("/name/:name", DefensiveExample::getName);
    }

    private static Object getName(Request request, Response response) {
        response.type("application/json");
        var name = request.params(":name");
        if (!name.matches("(\\w|\\d){3,64}")) {
            response.status(400);
            return new Gson().toJson(Map.of("error", "invalid parameter"));
        }

        return new Gson().toJson(Map.of("result", name));
    }
}
```

## Protecting Internal Assumptions

You may also protect internal objects and methods from inadvertent parameters by using the Java `assert` keyword. For example, in the following code, if the provided name is numerical, the Java runtime will throw an exception.

```java
private String normalize(String name) {
    assert !name.matches("\\d+") : "Numeric name provided";

    return name.toUpperCase();
}
```

By Java assertions described by the `assert` keyword are ignored by the runtime. You must explicitly enable them by providing the `-ea` switch when executing the `java` interpreter.

This makes it so you use the `-ea` switch during testing and development, and then disable it when you deploy it for production so that you don't have to incur the performance overhead associated with the assertions.

## Full Example

Here is a full example that demonstrates the use of both exceptions and assertions.

```java
public class DefensiveExample {
    public static void main(String[] args) {
        Spark.get("/name/:name", DefensiveExample::getName);
    }

    private static Object getName(Request request, Response response) {
        response.type("application/json");
        var name = request.params(":name");
        if (!name.matches("(\\w|\\d){3,64}")) {
            response.status(400);
            return new Gson().toJson(Map.of("error", "invalid parameter"));
        }

        name = normalize(name);

        return new Gson().toJson(Map.of("result", name));
    }

    private static String normalize(String name) {
        assert !name.matches("\\d+") : "Numeric name provided";

        return name.toUpperCase();
    }
}
```

## Things to Understand

- How and when to write assertions.
- How and when to use parameter checking instead of assertions.

## <a name="videos"></a>Videos (14:15)

- 🎥 [Assertions (11:48)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=934d5be6-15b3-4213-a25b-ad6d01430c86&start=0)
- 🎥 [Parameter Checking (2:27)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=4d06fa38-cf64-4dc2-ace5-ad6d0146799a&start=0)
