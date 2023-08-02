# Java Inheritance

📖 **Required Reading**: Core Java SE 9 for the Impatient

- Chapter 3:
  - Section 1 - Interfaces
  - Section 2 - Static, Default, and Private Methods
  - Section 3 - Examples of Interfaces

Interfaces allows you to define what something does without specifying how it does it. It also allows you to create different implementations without changing how other code uses the interface. For example, you can have an interface that defines what a `List` can do, and then create classes that provide different implementations of the `List` interface. Perhaps one implementation uses less memory, and a different one is faster. You can then write code that uses the `List` interface and not have to worry about how it is implemented.

```java
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

public class ListExample {
  public void listExample() {
    // Represent to different implementations with the same List interface.
    List<String> list1 = new ArrayList<>();
    List<String> list2 = new LinkedList<>();

    add(list1, "vanilla");
    add(list2, "taco");

    System.out.println(list1);
    System.out.println(list2);
  }

  // This function takes the interface and doesn't know how the list is
  // implemented.
  private void add(List<String> list, String value) {
    list.add(value);
  }
}
```

## Things to Understand

- What polymorphism is
- What abstract classes are and when to use them
- What interfaces are and when to use them
- How interfaces and abstract classes are both similar and different
- How to implement an interface in a class
- How to create an interface
- The guarantee you have when a reference of an abstract class or interface type does not refer to null

## Videos

- 🎥 [Polymorphism](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=23d2e58e-9628-43a4-9aaa-ad640141e7dc&start=0)
- 🎥 [Polymorphism Example Part 1](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=88adc709-e900-47d6-9e9a-ad64014400ad&start=0)
- 🎥 [Polymorphism Example Part 2](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=ebfcd403-53e4-4b68-8a3d-ad6401453df4&start=0)
- 🎥 [Polymorphism Example Part 3](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=f451dd38-e32d-445f-be0d-ad6401470c45&start=0)
- 🎥 [Creating an Interface](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=2da0fb3a-7aca-4344-a42b-ad640149f9e2&start=0)
- 🎥 [Implementing an Interface](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=f7ec17c1-c815-429b-8ffd-ad64014b0921&start=0)
