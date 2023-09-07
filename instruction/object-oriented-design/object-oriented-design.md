# Object Oriented Design

🖥️ [Slides](https://docs.google.com/presentation/d/17S-Y7Og08S9kRWHZfnH8k2wTBht39aCd/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

`Object Oriented` design focuses on describing objects in the application domain as literal programming constructs. That means if your application contains people who eat fruit. Then you model the application by creating a `Person` and `Fruit` object. Those objects will have properties such as name, ripeness, and color. The objects will also have operations such as eat, plant, grow, or purchase. You then write your code as real world interactions between the core application domain objects. To continue our example, you would have interactions where a `Person` will either `plant`, or `purchase`, a `Fruit` object and then `eat` the fruit.

![Person Fruit Model](personFruitModel.jpg)

Object oriented design owes much of its popularity to the natural representation of the real world that it provides. By carefully modeling the actual application domain, the resulting code will naturally exclude unnecessary complications, and overhead, that would have resulted if you use other [programming paradigms](https://en.wikipedia.org/wiki/Programming_paradigm) that focus more on functional logic or declarative constructs.

In object oriented programming everything revolves around a `Class` construct that serves at the template for actual objects. Classes always represent nouns, or things, such as a cat, car, word, database row, or even abstract things such as a thought. A class's operations, or methods, are always verbs, such as construct, run, speak, compute, or destroy. When you instantiate a class into an object you convert the template into an actual thing. Think of it as creating an object named `James` from

In order to fully model the real world with your objects. The relationships between objects fall into three different catagories.

| Relationship | Description | Example            |
| ------------ | ----------- | ------------------ |
| Is-A         |             | A Cow is an Animal |
| Has-A        |             | A Cow has a Brain  |
| Uses-A       |             | A Cow uses a Barn  |

## Things to Understand

- First understand the application domain
- Domain represented with classes
- Classes are nouns that represent real world objects
- Classes have methods and properties just like real world objects
- Classes have relationships to each other. Is-A, Has-A, Uses-A
- You encapsulate data in order to hide implementation details
- Single Responsibility Principle
- Class diagram
- Sequence diagram

## Videos

- 🎥 [Object-Oriented Design Overview](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=77c184e5-8afd-4c56-84c8-ad64013f7a4b&start=0)
