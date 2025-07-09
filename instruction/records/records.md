# Java Records

🖥️ [Slides](https://docs.google.com/presentation/d/18afVnWrynaF_7Tqet6SpqeBM3Ns_tS-v9wW86TliEX4/edit?usp=sharing)

🖥️ [Lecture Videos](#videos)

When writing object oriented code it is very common to have classes that only exist to represent a collection of data fields. These are called `data objects`. Data objects exist only to serve as input, or output, for other objects that operate on them. For example, you could have `Pet` object with id, name, and type fields. A pet object would then be passed to a `Walker` object that would give it exercise. Another object, `PetHealth` would be associated with the pet in order to track the effect of the exercise. This type of encapsulation helps to create cohesive objects that have only one responsibility. An additional desirable attribute of data objects is that they are usually immutable. This means that after they are created they do not change. Immutable objects are desirable because they are safe to pass around and use in multiple contexts at the same time, without worrying that they will have mutated when they come back to you.

Consider the following data object class that represents a pet. In this example, we set the properties of the pet in the constructor and make it immutable with the use of the `final` keyword on all the fields. The code also provides getters for all the fields, as well as overriding equals, hashcode, and toString.

```java
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

        if (id != petClass.id) return false;
        if (!Objects.equals(name, petClass.name)) return false;
        return Objects.equals(type, petClass.type);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PetClass[" +
                "id=" + id +
                ", name=" + name +
                ", type=" + type +
                ']';
    }
}
```

That is everything we need to make a fully functional, immutable, data object. However, it is also 52 lines of boilerplate code just to represent three fields. Since data objects are very common, Java introduced the `record` keyword in order to make the syntax more concise.

All you have to do is declare a record and the fields you want it to contain. Here is a record replacement for our pet class that is functionally equivalent.

```java
record PetRecord(int id, String name, String type) {}
```

When you use Java records you get all of the following benefits.

1. Immutability. All fields are final.
1. Simplified constructor syntax.
1. Automatic getters.
1. Automatic equals that compares all the fields.
1. Automatic hashcode that calculates based on all the fields.
1. Automatic toString that represents all the fields.

You can also put methods on your records if you would like. This is useful if you want to do things like provide a way to rename a pet. Note that because the record is immutable, the method cannot modify the name field and so it creates a new pet with the new name.

```java
public record PetRecord(int id, String name, String type) {
    PetRecord rename(String newName) {
        return new PetRecord((id), newName, type);
    }
}
```

## <a name="videos"></a>Videos (6:19)

- 🎥 [Java Records (6:19)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=1f70d34e-b330-4e45-9df0-b19c011a79bc) - [[transcript]](https://github.com/user-attachments/files/17752045/CS_240_Java_Records_Transcript.pdf)
