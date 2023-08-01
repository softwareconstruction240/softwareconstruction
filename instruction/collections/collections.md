# Java Collections

🖥️ [Slides](https://docs.google.com/presentation/d/1yAxwkW1qClRlFBxAokyBvfDhuTI6LXmA/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

📖 **Required Reading**: Core Java SE 9 for the Impatient

- Chapter 7: Collections

The Java Collection library provide several useful utility classes for dealing with data structures. This includes things like lists, sets, and maps. Using the standard collections library makes it so that you don't have to write this code yourself. You can also be confident that the code has been thoroughly tested, is secure, and is multithreaded where appropriate.

Here is an example of using the `ArrayList` class.

```java
import java.util.ArrayList;

public class Mountains {
  ArrayList<String> mountains = new ArrayList<>();

  public Mountains() {
    mountains.add("Nebo");
    mountains.add("Timpanogos");
    mountains.add("Lone Peak");
    mountains.add("Cascade");
    mountains.add("Provo");
    mountains.add("Spanish Fork");
    mountains.add("Santaquin");
  }

  public void printList() {
    System.out.println(mountains);
  }
}
```

In order to fully understand collections we need to explore the following topics.

- The interfaces and classes that make up the Java collection API
- What each interface and class is used for
- The importance of overriding the `equals(...)` and `hashcode()` methods of classes that will be placed in collections
- The importance of implementing the Comparable interface in classes that will be placed in collections

## Videos

- 🎥 [Java Collections](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=7f2f800e-d46e-4ce4-8839-ad5f011fa7a1&start=0)
- 🎥 [Using Collections Correctly](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=bea26db3-5825-4df2-9ba0-ad5f01260f7e&start=0)
