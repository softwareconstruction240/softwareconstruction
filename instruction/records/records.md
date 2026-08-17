# Java Records

🖥️ [Slides](https://docs.google.com/presentation/d/18afVnWrynaF_7Tqet6SpqeBM3Ns_tS-v9wW86TliEX4/edit?usp=sharing)

🖥️ [Lecture Videos](#videos)

### 🔑 Key points

- Java records provide a concise representation of immutable data classes
- Records support a reasonable equals and hashcode implementations

---


When writing object-oriented code, it is common to use classes that exist solely to act as containers for data. These are often called "data objects" or "data-carrier classes." Their primary purpose is to store state and pass it between different parts of an application. 

For example, you might have a `Pet` object with `id`, `name`, and `type` fields. This object could be passed to a `Walker` object to track exercise or to a `PetHealth` object to monitor medical history. This type of encapsulation helps create cohesive objects with a single responsibility. 

A key characteristic of well-designed data objects is **immutability**. An immutable object's state cannot be modified after it is created. This makes them safer to use in multi-threaded environments and easier to reason about, as you do not have to worry about their values changing unexpectedly when passed to other methods.

Consider the following traditional class representing a pet. To make it immutable, we use the `final` keyword on all fields and initialize them via the constructor. We also provide "getters" for all fields and override `equals()`, `hashCode()`, and `toString()`.

```java
import java.util.Objects;

class PetClass {
    private final int id;
    private final String name;
    private final String type;

    PetClass(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetClass petClass = (PetClass) o;
        return id == petClass.id && 
               Objects.equals(name, petClass.name) && 
               Objects.equals(type, petClass.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type);
    }

    @Override
    public String toString() {
        return "PetClass[" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ']';
    }
}
```

While this class is fully functional, it requires over 50 lines of boilerplate code just to represent three fields. Because data objects are so common, Java introduced the `record` keyword to make this syntax much more concise.

To define a record, you simply declare the fields you want it to contain. Here is a record replacement for our `PetClass` that is functionally equivalent:

```java
record PetRecord(int id, String name, String type) {}
```

When you use Java records, the compiler automatically provides the following:

1.  **Immutability**: All fields are private and `final`.
2.  **Canonical Constructor**: A constructor that initializes all fields is created for you.
3.  **Accessor Methods**: Instead of `getName()`, records use the field name as the method name (e.g., `name()`).
4.  **`equals()`**: A method that compares two records based on their field values.
5.  **`hashCode()`**: A method that calculates a hash based on all fields.
6.  **`toString()`**: A string representation showing all field names and their values.

You can also add custom methods to a record. This is useful if you want to provide logic related to the data. Note that because records are immutable, a method cannot modify an existing field; instead, it must return a new instance of the record with the updated data.

```java
public record PetRecord(int id, String name, String type) {
    /**
     * Returns a new PetRecord with the updated name.
     */
    public PetRecord rename(String newName) {
        return new PetRecord(id, newName, type);
    }
}
```


## ☑ Exercise


```masteryls
{"id":"dff0788f-db66-4a2f-bd87-1de307ac5bf0", "title":"Records", "type":"teaching" }
What is the value of a Java record? Why not just always use a class?
```

```masteryls
{"id":"23e9006e-f703-4ee0-a7bc-4f2ec146aef4","title":"Clarity as Kindness","type":"essay"}
How does using records to express data clearly and concisely make life easier for those who read your code, and how does writing for others' understanding reflect the BYU aim of building character?
```


## Videos

- 🎥 [Java Records (6:19)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=1f70d34e-b330-4e45-9df0-b19c011a79bc) - [[transcript]](https://github.com/user-attachments/files/17752045/CS_240_Java_Records_Transcript.pdf)