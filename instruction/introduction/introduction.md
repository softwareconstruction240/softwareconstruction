# Introduction

Welcome to Computer Science 240: `Advanced Software Construction`. This course is primarily about learning how to build large programs by giving you experinece with a set of related technologies. This includes: Java, HTTP, relational databases, WebSocket, Testing, and console programming. You’ve probably built a number of smaller programs that were targeted at learning particular concepts. In this class, we will learn how to build programs that are realistic, real-world, bigger, and more complicated than the things you have previously experienced.

When you’re building a small program, you can beat it into submission and make it work without too much thought. However, when you build larger programs it requires careful design, engineering, and proper tooling. This class is an introduction on how to do that.

As software systems get larger, the complexity increases exponentially. It has more parts and pieces to it, and those parts and pieces have to work together to make the system actually work. There’s some techniques that need to be learned for how to take a large system and break it down and decompose it into smaller pieces. There are also engineering techniques on how to write your code in such a way that you can test it along the way, so that when all the code has been written, and integrated together, the whole system actually works.

In this class, you will have some experiences where you struggle, where things are hard, you get stuck and you bang your head against something for a while, and it can be frustrating. Sometimes people get discouraged because they think they are the only one that is struggling, and everybody else is having a much easier time. That is not really the case. Everybody struggles when they program. The struggle is part of the fun of programming. Don’t let that damage your self-confidence. You can do this. Just about anybody can learn how to do this, as long as they’re willing to put in the time and the effort. That really is the key in this course, is putting in the time. As long as you’re going to be willing to put in the time and work hard, then you’re very likely to be successful. When you struggle, don’t get discouraged, don’t assume that you should go find something else to do that you should change your major or quit, just know that that’s part of the learning process, and that as we struggle, we grow, we learn, and that’s okay.

This class is all about projects. There ins't really any homework other than programming projects. We program a lot in this class, and really the structure of the course is driven by the projects themselves. If you ever wonder why are we learning things in a particular order, it’s because it is driven by the projects.

We use the Java programming language in this class. Java has been a popular language for multiple decades. According to the [2023 Stack Overflow survey](https://survey.stackoverflow.co/2023/#most-popular-technologies-language-prof), Java is used by 30% of professional developers. Java has some nice properties. It is object oriented, compiled, has garbage collection, and is strongly typed. In short, Java is a good language help you round out your skill set and resume.

## Reading

In order to properly learn Java you will need to reference selected chapters of the book `Core Java for the Impatient`. This book is available for free on the Safari Books collection that’s available through the Harold B. Lee Library. You can also reference the many resources available on the web for mastering the different concepts found in the Java programming languages.

![Java for the Impatient](CoreJavaForTheImpatient3rdEdition.jpg)

⚠ Note that it is critical that you reserve a significant amount of your time to learn Java outside of class. In class we focus on concepts that are needed for the projects, hard concepts, and the things that tend to confuse students. It is assumed that you already learned the basics of Java on your own.

## Projects

There are two programming projects that you will complete for this course. The first program is a simple `spelling corrector`. You provide the program with a dictionary and a word to correct. It then returns a corrected suggestion.

```sh
➜  java spell/Main dictionary.txt truble

Suggestion: trouble
```

The purpose of the spelling corrector is to help you learn the basics of Java and get you familiar with the Intellij integrated development environment (IDE). After you have completed the spelling corrector you will take a timed exam during which, you will reimplement the program without referencing your previous work. Being able to efficiently write code under a deadline will demonstrate your mastery, and prepare you for the realities of programming professionally.

Once you have completed the spelling corrector, you can confidently assume that you have the basic skills necessary to complete the second, and much larger, `multi-player chess` program. This program consists of a chess server that allows multiple client player programs to connect, register users, and play games.

![Chess game](highlight-moves.png)

The chess program is broken up into [six different phases](../../chess/chess.md). Each phase teaches a different concept related to the construction of a large programming project. Once you have completed the final phase you will have created a fully functional program that you can use to play with friends.

## Enrichment lectures

Towards the end of the course, while you are hammering away on your chess program, the course topics will focus on enrichment material that you should be familiar with as a professional developer. These topics will not be reflected in your project work, but you will need to be familiar with their content since they will represent the bulk of the material covered by the final exam. These includes topics such as security, concurrency, and Java command line tools.
