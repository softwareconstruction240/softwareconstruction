# Design Principles

🖥️ [Slides](https://docs.google.com/presentation/d/1f1X706vwJKqBRPhlB-yBF7-059--6DoF/edit#slide=id.p1)

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

1. Who are the **actors** in the system.
1. What **tasks** do the actors want to accomplish.
1. What are the **objects** that the actors use.
1. What are the **interactions** between actors and objects that are necessary to complete the tasks.

Once you have the actors, tasks, objects, and interactions defined you can then think about the data structures, devices, and protocols that will best support the domain. Basically you think about retail stores, employees, SKUs, and credit cards before you worry about hashmaps, protocols, tables, and networks.

Be careful to consider all of you users, not just your target customers. Often times internal corporate, or governmental, customers are just as important. That means you need to consider security, regulatory restrictions, data privacy, administration, reporting, and metrics as primary pieces of the domain design.

## Persona Roll Play

Sometimes it is helpful to assign personas to your primary actors. Creating a persona that gives a name and backstory to an actor allows you to walk through a story with them to validates the assumptions of your design. It changes the conversation from

> "A user buys a car"

to

> "John is a student from rural Utah who is short on cash. He needs to buy a car so that he can get to his part time job. He is willing to spend a lot of time finding and negotiating the best deal possible. However, he finds interacting with sales people intimidating and would prefer automated processes. He is going to need to finance his car with a cosigner on the loan."

Being thoughtful about the background of your customer will make it easier to avoid incorrect assumptions in your design.

## Core Technologies

Even though you want to design for you domain first, that does not mean that you give no thought to your technology infrastructure. Carefully consider core technologies that are expensive to change once an application is deployed. This includes things such as programming languages, data schemas, protocols, databases, deployment processes, and hosting locations. Some criteria that you should consider when choosing core technology includes:

1. Security
1. Cost
1. Availability
1. Redundancy
1. Stability
1. Market acceptance
1. Support
1. Performance
1. Elasticity

## Iterative Design

It is important to realize that software is sufficiently complex that you should build it in a way that allows iterate on its construction. First consider the design for some foundational piece of the application. You then build the simplest implementation that satisfies the design. For example, a nonfunctional client than displays hardcoded placeholders. As part of this iteration you make sure that there is sufficient test coverage to establish confidence in the implementation. You then solicit user feedback that the implementation of the design is correct. You then repeat the process.

1. Design
1. Implement
1. Verify

This is important because it will break the application down in to manageable pieces, incrementally introduce complexity, and allow you to correct bad design decisions early in the process.

The size of your iteration will depend up on the size of your team and the complexity of the project, but work that can be completed in one to two weeks is a common measure. Iterating for more than four weeks will often lead to wasted or inefficient efforts.

## Abstraction

## Names

## Single-Responsibility Principle

Another form of simplicity is represented by the [Single Responsibility Principle](https://en.wikipedia.org/wiki/Single-responsibility_principle). The idea here is that an object does one thing and does it well. You don't have a `Person` class that has a method to `driveCarByRoute` that performs the actions of a car in motion. You would have a `Person` class, a `Car` class, and a `Route` class. You would then pass the `Person` and `Route` to the car's `drive` method, and send it on its way.

![frankenobject](frankenObject.jpg)

Following the Single Responsibility Principle makes it so there is only one reason to manipulate the class. You manipulate the `Person` class to represent the person and the `Route` class to represent the route. If you find yourself making a `Frankenobject` that represents multiple real world objects then you need to refactor your code into multiple classes.

## Decomposition

## Encapsulation - Information Hiding

Information Hiding

## DRY - Avoiding Code Duplication

## Things to Understand

- The goals of software design
- Design is an iterative process
- Abstraction
- Good naming
- Single Responsibility Principle
- Decomposition
- Good algorithm and data structure selection
- Encapsulation - Information hiding
- DRY - Avoiding code duplication
