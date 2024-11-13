# Design Principles

🖥️ [Slides](https://docs.google.com/presentation/d/1f1X706vwJKqBRPhlB-yBF7-059--6DoF/edit#slide=id.p1)

🖥️ [Lecture Videos](#videos)

📖 **Required Reading**: None

Software design is the process of defining, architecting, and creating an application. The primary goal of any application is to satisfy a customer's requirements. With a firm focus on the customer, you then apply the principles of good software design to identify the important actors, objects, and interactions necessary to represent the application's domain. This naturally leads to a code architecture that is easy to understand, debug, enhance, and maintain as requirements change.

As you seek to design software you should focus on the following high level goals:

1. It does what the customer wants it to do
1. It is easy to understand, debug, and maintain
1. It is extensible to requirement changes

Using these goals we can discuss the methods that commonly lead to successful software designs.

## Domain Driven Design

In order to build an application that a customer wants you need to understand the domain that the customer lives in. This helps you to properly define the application in terms that the customer understands. This approach is often referred to as [Domain Driven Design](https://en.wikipedia.org/wiki/Domain-driven_design).

As software engineers, it is tempting to focus on computer science algorithms and data structures instead of the objects and actors that a user is familiar with. With Domain Driven Design you reverse the thought process and instead think of the following:

1. Who are the **actors** in the system?
1. What **tasks** do the actors want to accomplish?
1. What are the **objects** that the actors use?
1. What are the **interactions** between actors and objects that are necessary to complete the tasks?

Once you have the actors, tasks, objects, and interactions defined you can then think about the data structures, devices, and protocols that will best support the domain. Basically you think about retail stores, employees, SKUs, and credit cards before you worry about hashmaps, protocols, tables, and networks.

Be careful to consider all of your users, not just your target customers. Oftentimes internal corporate, or governmental, customers are just as important. That means you need to consider security, regulatory restrictions, data privacy, administration, reporting, and metrics as primary pieces of the domain design.

## Persona Role Play

Sometimes it is helpful to assign personas to your primary actors and have a role play conversation with them. Creating a persona that gives a name and backstory to an actor allows you to walk through a story with them to validate the assumptions of your design. It changes the conversation from a shallow statement like:

> "A user buys a car"

to something closer to the reality of the user's domain:

> "Perry is a student from rural Utah who is short on cash. He needs to buy a car so that he can get to his part time job. He is willing to spend a lot of time finding and negotiating the best deal possible. However, he finds interacting with sales people intimidating and would prefer an automated process. He is going to need to finance his car with a cosigner on the loan."

Being thoughtful about the background of your customer will make it easier to avoid incorrect assumptions in your design. The more real the persona becomes, the better the result will be. In the end, intentional introspection of this type will save you time because your earlier design iterations will be closer to what the customer wants.

## Top Level Design

Before you dig into the details of your design you want to create a couple of diagrams that capture the vision of what you are building. This is not meant to be an exhausting diagram, but it should make it so your team has a common vision of the most important pieces of the application architecture. It should represent both the high level UI pieces and the major components of the underlying application.

The following is an example of a top level design diagram for the Chess application.

![chess design](top-level-design.png)

## Iterative Design

It is important to realize that the complexity of software increases exponentially with the size of the application and the team working on it. One method for dealing with increasing complexity is to execute a series of simplified iterations. Each iteration becomes a deliverable by itself in a journey towards a larger goal. With the understanding that you are going to take an iterative approach to your design you then break each iteration into three distinct steps. First consider the design for some foundational piece of the application. For example, start with a nonfunctional client that displays hardcoded placeholders. Next, you build a minimal implementation that satisfies the design. Finally, you verify that your iteration satisfies the design by examining the test coverage, and soliciting user feedback that the implementation of the design is correct. You then repeat the process.

![Iterative Design](iterativeDesign.png)

Using an iterative design is important because it will break the application down into manageable pieces, incrementally introduce complexity, and allow you to correct bad design decisions early in the process.

The size of your iteration will depend on the size of your team and the complexity of the project, but work that can be completed in one to two weeks is a common measure. Iterating for more than four weeks will often lead to wasted or inefficient efforts.

## Abstraction

In order to understand the world we use abstraction. When we see a person, we don't see organs and DNA. When we think of a university, we don't think about databases of scholastic records, cleaning crews, pipes, and department budgets. Likewise, when we think of a software application, we don't consider all of the layers of complexity that make the application work. We abstract away many layers of detail and instead focus on the pieces necessary to complete our current task. Without abstracting away things like the hardware, operating system, application interface, threads, user interface, rendering engine, network communication, persistent storage, and memory we would never be able to keep even the simplest of programs in our heads.

When we create abstractions in our applications we begin by defining abstractions that represent real world objects. We call these the objects of our application domain. For example, a bank, customer, account, and loan. We then add an additional level of abstraction to represent the data structures and algorithms necessary to support the domain objects. For example, database schemas, network protocols, hash tables, and events.

In object oriented programming `Interfaces` and `Objects` are used to provide the bulk of abstraction.

Whenever you program you should try and abstract things into the following parts.

1. What are the input interfaces
1. What are the output interfaces
1. What interface does the my abstraction need to provide
1. What class will implement the interface

```mermaid
classDiagram
    MyAbstraction <-- Input
    MyAbstraction --> Output
    class Input{
      getA()
      getB()
    }
    class MyAbstraction{
      -private encapsulatedData
      -private encapsulatedMethods()
      process(Input):Output
    }
    class Output{
      setC(c)
    }
```

Note that sometimes it is not necessary to create an interface when a single class representation can simply expose public methods and abstract away the details. Interfaces are useful when there are multiple different algorithms that can be used to satisfy the interface, or when there are classes that implement multiple interfaces.

The important thing to remember about abstraction is that you:

1. Represent the domain and system objects with abstractions (e.g. interfaces and classes)

## Encapsulation

When we create our abstractions we want to hide anything that is not absolutely necessary to complete the task of the abstraction. In software design, hiding details is referred to as encapsulation. Some of the benefits of encapsulation include:

1. **Comprehension** - Less details makes it easier to understand how the objects interact and form a complete mental model.
1. **Extensibility** - When we are not aggressive with exposing details, we can expose those details later, or we can expose new operations that might have conflicted with previously exposed operations that were unnecessary at the time.
1. **Evolution** - Hiding how the object gets things done means that you can change the implementation without changing anything that depends on the object.
1. **Security** - Anything that is hidden by an object is less likely to be subject to attack through the object's interface.

One common mistake with encapsulation is to think that it only applies to the public methods that you include in a class. You can also provide data hiding by implementing interfaces that restrict the view of what an object can do to a small set of methods. For example, you might have a class that represents a person. In order to provide encapsulation of the class, the person might represent an `Object`, `LivingEntity` and `Animal` interface. By exposing different aspects of the person the consumer of the object only needs to know about the aspect that is of interest to them. This provides all of the benefits of comprehension, extensibility, evolution, and security.

The important thing to remember about encapsulation is that you:

1. Only expose what is necessary

## Decomposition

The basic idea of decomposition is to create another level of abstraction that represents layers of generalization and specialization. Each layer has a specific task to do and it accomplishes it by using the layers beneath. The idea is that you start at the top with a very general representation. For example, a chess game. You then decompose, or factor out, each layer of the higher level into increasingly specialized pieces. For example, a game is made up of participants, pieces, and a board. This process continues until the smallest necessary level of decomposition is completed. Continuing our example, this could include decomposing participants into players and observers, pieces into piece types, and a board into squares.

The advantage of decomposition is that you only need to think about the details of the layer when you are actually working on it. This includes defining its interfaces, implementing the details, and writing tests for that layer. For example, when defining the `Participant` layer, you only need to think about how a participant interacts with the `Game` and is represented by a `Player` or `Observer`. At the player level, you don't need to worry about what a `Board` is comprised of, or what the rules for moving a `King` are.

```mermaid
classDiagram
    Game *-- Board
    Game *-- Piece
    Game *-- Participant

    Board *-- Square
    Participant <|-- Player
    Participant <|-- Observer

    Piece <|-- King
    Piece <|-- Rook
    Piece <|-- Pawn
```

Programming languages themselves utilize decomposition to represent different parts of a program. When using Java we use the following decompositions:

| Decomposition | Purpose                                                          |
| ------------- | ---------------------------------------------------------------- |
| Application   | The top level that we hand to the operating system for execution |
| Jar           | A zip file containing packages of classes                        |
| Package       | A directory of classes                                           |
| Class         | A domain or system object                                        |
| Method        | An action of an object                                           |
| Expression    | Logic of a method                                                |

Using decomposition at the program level helps you so that you don't have to keep the whole code base on your screen at the same time. You just need to open the files that represent the current task.

## High Cohesion and Low Coupling

High cohesion means that an object only represents highly related data and functionality. You don't include tangentially related methods or fields in an object. Instead you create a cohesive object that executes in concert with other related objects.

Low coupling means that objects do not strongly rely on each other. High coupling occurs when an object that cannot be used without understanding the specific implementation details of another object, or when two objects require each other to operate. Generally, low coupling means that you are using interfaces appropriately and that objects do not have bidirectional bindings.

## Prefer Aggregation Over Inheritance

When you are creating your classes you need to carefully consider the different meanings and implications of using inheritance instead of aggregation. We often simplify the concepts of inheritance and aggregation to represent `is-a` and `has-a` relationships respectively. However, by favoring aggregation you can create composable objects that have the benefits of multiple inheritance without all of the complexity that multiple inheritance incurs. Aggregated objects can demonstrate polymorphic behavior by exposing interfaces that are implemented by the contained objects. As long as interfaces are used to access the aggregations, the containing class can replace the aggregated objects without impacting any users of the objects. In short, when combined with interfaces, aggregation can provide:

1. `has-a` and `is-a` relationships
1. The benefits of multiple inheritance
1. Encapsulation

This suggests that in many cases Aggregation should be preferred over inheritance.

## SOLID

The SOLID principles of clean code were promoted by a popular software design consultant named Robert Martin (AKA Uncle Bob).

![Uncle Bob](robert-martin.png)

> _source: [SmarterMSP.com](https://smartermsp.com/pioneers-in-tech-barbara-liskov-and-the-clu-programming-language/)_

> “Truth can only be found in one place: the code.”
>
> — Robert Martin

SOLID represent five key principles.

1. Single Responsibility - An actor has only one reason to use you
1. Open Closed - Open for extension, closed for modification
1. Liskov Substitution - Actually implement the interface
1. Interface Segregation - Keep interfaces cohesive
1. Dependency Inversion - Make dependencies parameters

Let's look at each of these in detail.

### Single Responsibility Principle

The [Single Responsibility Principle](https://en.wikipedia.org/wiki/Single-responsibility_principle) represents the desirability of high cohesion. The idea here is that an actor only has one reason to use an object. You don't have a `Person` class that represents everything associated with a person. You have a `Person` class that represents the distinct attributes of a person such as `name` and `birthDate`, and then you have other classes that represent things associated with a Person.

```mermaid
classDiagram
    Person <-- FoodConsumption : uses-a
    Person <-- OutdoorActivity : uses-a
    Death --* Person : has-a
    Birth --* Person : has-a
    class Person{
      name
      birth
      death
    }

    class FoodConsumption {
        eat(Person, Meal)
    }

    class OutdoorActivity {
        play(Person, Game)
    }

    Date <|-- Death
    Date <|-- Birth
    OutdoorActivity --> Game : uses-a

    class Death {
    }

    class Birth {
    }
```

Following the single responsibility principle makes it so there is only one reason to manipulate the class. You manipulate the `Person` class to represent the person and the `Death` class to represent a death. If you find yourself making a `FrankenObject` that represents multiple objects, or responsibilities, then you should consider refactoring your code into multiple classes.

The Java `String` class is a frequently cited example of violating the single responsibility principle as it not only represents an immutable string but provides operations for manipulating and converting the string. This makes the `String` class both a data container and a data mutator.

Classes are not the only places where you need to consider the single responsibility principle. Methods and variables can also fall prey to confusing and conflicting responsibilities. For example, the following method has been overloaded with multiple responsibilities and interpret the parameters and return value in contradictory ways.

If you find yourself changing a class for different reasons, functionality vs representation vs mutation vs display vs persistence, then you are probably in violation of the single responsibility principle.

#### Violation Example

```java
public interface FrankenPerson {
    public void drive();
    public void sleep();
    public void eat();
    public void work();
    public void die();
    public void play();

    public void setAlarm();
    public void planRoute();
    public void shopForFood();
    public void buyGymPass();
}
```

```java
public interface SRPViolation {
    /**
     * i < 0 delete the key and the empty string if successful
     * i == 0 return the old value if different
     * i > 0 replace the value and return the old value
     */
    public String dbAction(String key, String value, int i);
}
```

### Open Closed Principle

Classes should be open to extension and closed for modification. The core concept is that you should generalize the functionality of a class so that you don't have to internally modify it in order to provide a desired extension of its functionality.

A common example for the open closed principle involves passing in interfaces that control how the class works. This is in contrast to modifying the classes methods to provide new functionality.

#### Violation Example

As an example, the following code forces you to create a new method for every different type of format that you want the class to support. Additionally, the class has a constructor that represents a specific type of data. If you want to provide a different type of data, you must modify the class to include an additional constructor and internal data type.

```java
public static class OpenForModificationList {
    final private String[] items;

    public OpenForModificationList(String[] items) {
        this.items = items;
    }

    public String formatCommaSeparated() {
        return String.join(",", items);
    }

    public String formatQuotedCommaSeparated() {
        var formattedItems = new ArrayList<String>();
        for (var item : items) {
            formattedItems.add(String.format("'%s'", item));
        }

        return String.join(",", formattedItems);
    }
}
```

### Correct Example

We can improve the previous code by using interface parameters and Java generics to open the class to extension without ever modifying the code.

```java
public interface Formatter<T> {
    String format(T s);
}

public static class OpenForExtensionList<T> {
    final private List<T> items;

    public OpenForExtensionList(List<T> items) {
        this.items = items;
    }

    public String format(Formatter formatter, String separator) {
        var formattedItems = new ArrayList<String>();
        for (var item : items) {
            formattedItems.add(formatter.format(item));
        }

        return String.join(separator, formattedItems);
    }
}
```

In this example the `Formatter` interface extends how the class formats and the generic type extends the supported types.

### Liskov Substitution Principle

![Barbara Liskov](barbara-liskov.jpeg)

> _source: [SmarterMSP.com](https://smartermsp.com/pioneers-in-tech-barbara-liskov-and-the-clu-programming-language/)_

> “[be] aware not just of what you understand, but also what you don’t understand”
>
> — Barbara Liskov

If an operation is dependent on an interface, or base class, you must be able to substitute any derived class without altering the operation. This can happen if a base class throws an `UnsupportedException` for an interface or overridden method, or if the operation does a type cast on the interface.

#### Violation Example

```java
public class LSPExample extends Object {
    public int hashCode() {
        throw new UnsupportedOperationException();
    }
}
```

```java
void lspViolation2(List list) {
  var arrayList = (ArrayList)list;
}
```

Violations of this principle cause unexpected behaviors within the application and require the developer to understand all of the code before they can safely make substitutions.

### Interface Segregation Principle

When you define an interface you only include methods that work together as a cohesive whole. You don't add methods that are related, but not necessary for the consumption of the primary usage of the interface. Put another way, the interface segregation principle states that that no consumer of an interface should be forced to depend on methods it does not use.

Exposing methods to all consumers of the interface, without regard for the use of the methods by all the consumers, creates a significant maintenance problem. If you want to alter the interface then you must examine all uses of the interface. Instead, the preferred approach is to create multiple interfaces that an object uses and only use the interface that is appropriate to the consumer.

#### Violation Example

```java
public interface InterfaceSegregationExample {
    byte readByte();
    String readString();
    int readInt();

    // Outside cohesive whole.
    void writeByte(byte b);
    void writeString(String s);
    void writeInt(int i);
}
```

### Dependency Inversion Principle

The dependency inversion principle suggests the you should expose and use interfaces and not concrete classes. Interfaces enable the core abstraction necessary to make code extensible and maintainable. Whenever you expose a concrete class implementation you expose unintended coupling with the class. At the very least you are exposing a specific implementation, constructor, and potentially extraneous methods that are unnecessary to the use of the interface that should represent the class.

Put another way, the principle says that dependencies are made on aspects of functionality, not on implementations of the functionality. In the following example the high level `Route` class is highly coupled with the instantiation and use of the low level `Honda` object.

#### Violation Example

```java
class Violation {
    public static void main(String[] args) {
        Honda honda = new Honda();

        new Route().drive(honda);
    }

    static class Route {
        /**
         * Highly coupled with lower class implementation.
         */
        void drive(Honda car) {
            car.go();
        }

    }

    static class Honda {
        void go() {
            System.out.println("put put");
        }
    }
}
```

#### Correct Example

In order to properly apply the dependency inversion principle you invert the use of low level concrete classes and expose low level interfaces instead. The following example we use a factory method that uses reflection to load the constructor that is specified as a command line argument. The factory returns a `Vehicle` interface rather than a concrete class. Now the `Route` doesn't know know anything about the vehicle that is being used. It just calls `go`. This breaks the coupling between the objects and moves the decision about what vehicle is actually used to be completely out of the code.

```java
class Correct {
    public static void main(String[] args) {
        var vehicleMakerClass = args.length == 1 ? args[0] : "Honda";
        var factory = new VehicleFactory(vehicleMakerClass);

        Vehicle vehicle = factory.createVehicle();

        new Route().drive(vehicle);
    }

    interface Vehicle {
        void go();
    }

    static class Route {
        void drive(Vehicle vehicle) {
            vehicle.go();
        }
    }

    static class VehicleFactory {
        private Constructor<Vehicle> vehicleConstructor;

        VehicleFactory(String vehicleMakerClass) {
            try {
                var vehicleClass = Class.forName(vehicleMakerClass);
                vehicleConstructor = (Constructor<Vehicle>) vehicleClass.getDeclaredConstructor();
            } catch (Exception ignored) {
            }
        }

        Vehicle createVehicle() {
            if (vehicleConstructor != null) {
                try {
                    return vehicleConstructor.newInstance();
                } catch (Exception ignored) {
                }
            }
            return new Honda();
        }
    }

    static class Honda implements Vehicle {
        public void go() {
            System.out.println("put put");
        }
    }

    static class BMW implements Vehicle {
        public void go() {
            System.out.println("vroom");
        }
    }
}

```

By inverting the dependencies, you can decouple the code and move the commitment to an algorithm at a higher level. Now you can execute the code with different parameters and completely modify how it works.

## Immutability

Objects that do not change after they are constructed are referred to as immutable. In order to understand that value of immutability, consider the `String` class. If `String` was not immutable then you would never be sure you still had the same string value after a sub method was called. The following example demonstrates an unintentional side effect of calling an imaginary operation named `String.setText`.

```java

void printList(){
    String prefix = "- "
    var items = list.of("a", "b", "c");
    for (var item : items) {
        printWithPrefix(prefix, item);
    }
}

void printWithPrefix(String prefix, String text) {
    prefix.setText(prefix + text);
    System.out.println(prefix);
}

// Output:
// - a
// - a- b
// - a- b- c
```

In reality, because `String` is immutable, you never have to worry about its value being changed and you can safely pass it to any function.

Immutability also guarantees thread safe code because it eliminates the possibility that one thread can be modifying an object at the same time a different thread is reading it.

## Avoiding Code Duplication

If your code contains multiple copies of the same code then it is violating the `Do not repeat yourself`, or DRY, principle. Code duplication creates maintenance problems when you want to alter the code, increase the impact of errors, and makes it more difficult to correct the problems. It also makes the code unnecessarily complex because the reader has to read the same blocks over and over again to make sure they don't contain subtle variations.

You can reduce duplicated code by:

1. Using inheritance and encapsulation to represent a single version of the functionality.
1. Using utility methods for common operations.
1. Using generics to represent objects that only differ by type.

## Things to Understand

- The goals of software design
- Design is an iterative process
- Abstraction
- Single Responsibility Principle
- Decomposition
- Good algorithm and data structure selection
- Encapsulation - Information hiding
- DRY - Avoiding code duplication

## <a name="videos"></a>Videos (42:17)

- 🎥 [Design Principles - Introduction (2:27)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=3e09338f-77ca-4465-bebc-b17e014a8118)
- [CS_240_Design_Principles_Introduction_Transcript.pdf](https://github.com/user-attachments/files/17738057/CS_240_Design_Principles_Introduction_Transcript.pdf)
- 🎥 [Design Principles - Design Is Inherently Iterative (3:12)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=86a379ac-0a85-4843-9e3e-b17e014ba495)
- [CS_240_Design_Principles_Design_is_Inherently_Iterative_Transcript.pdf](https://github.com/user-attachments/files/17738081/CS_240_Design_Principles_Design_is_Inherently_Iterative_Transcript.pdf)
- 🎥 [Design Principles - Abstraction (8:29)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=0fa47857-e09e-47c7-8af1-b17e014cb378)
- [CS_240_Design_Principles_Abstraction_Transcript.pdf](https://github.com/user-attachments/files/17738090/CS_240_Design_Principles_Abstraction_Transcript.pdf)
- 🎥 [Design Principles - Good Naming (4:24)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=93653a93-9f2a-4550-89d5-b17e01513ccc)
- [CS_240_Design_Principles_Good_Naming_Transcript.pdf](https://github.com/user-attachments/files/17738108/CS_240_Design_Principles_Good_Naming_Transcript.pdf)
- 🎥 [Design Principles - Single Responsibility Principle (2:44)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=551b4011-c9c5-44b6-a190-b17e0152b18a)
- [CS_240_Design_Principles_Single_Responsibility_Principle_Transcript.pdf](https://github.com/user-attachments/files/17738113/CS_240_Design_Principles_Single_Responsibility_Principle_Transcript.pdf)
- 🎥 [Design Principles - Decomposition (5:25)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=8ebca729-bde2-43a3-a031-b17e015394f6)
- [CS_240_Design_Principles_Decomposition_Transcript.pdf](https://github.com/user-attachments/files/17738129/CS_240_Design_Principles_Decomposition_Transcript.pdf)
- 🎥 [Design Principles - Good Algorithm and Data Structure Selection (2:25)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=2356c7a4-3702-49c4-8c1d-b17e0155485a)
- [CS_240_Design_Principles_Good_Algorithm_and_Data_Structure_Selection_Transcript.pdf](https://github.com/user-attachments/files/17738176/CS_240_Design_Principles_Good_Algorithm_and_Data_Structure_Selection_Transcript.pdf)
- 🎥 [Design Principles - Low Coupling (9:37)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=b1c2ca29-d89f-44a5-9e82-b17e01562e89)
- [CS_240_Design_Principles_Low_Coupling_Transcript.pdf](https://github.com/user-attachments/files/17738182/CS_240_Design_Principles_Low_Coupling_Transcript.pdf)
- 🎥 [Design Principles - Avoid Code Duplication (3:34)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=81229b4c-251d-4aaa-a2c6-b17e0158fe0b)
- [CS_240_Design_Principles_Avoid_Code_Duplication_Transcript.pdf](https://github.com/user-attachments/files/17738191/CS_240_Design_Principles_Avoid_Code_Duplication_Transcript.pdf)
