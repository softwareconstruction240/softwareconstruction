# Spelling Corrector

🖥️ [Slides](https://docs.google.com/presentation/d/1FkxHpWkpQr-YIdtIqQeYwnAOekQ75Slg/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

As a preparatory step towards mastering the art of constructing software you will build a spelling corrector. This will help you learn the basics of building a Java program before we move to the more complex chess program.

```sh
➜  java spell/Main dictionary.txt truble

Suggestion: trouble
```

You are familiar with spell checkers. For most spell checkers, a candidate word is considered to be spelled correctly if it is found in a long list of valid words called a dictionary. Google provides a more powerful spell corrector for validating the keywords we type into the input text box. It not only checks against a dictionary, but, if it doesn’t find the keyword in the dictionary, it suggests a most likely replacement. To do this it associates with every word in the dictionary a frequency, the percent of the time that word is expected to appear in a large document. When a word is misspelled (i.e. it is not found in the dictionary) Google suggests a `similar` word (`similar` will be defined later) whose frequency is larger or equal to any other `similar` word.

In this project you will create such a spell corrector. There is one major difference. Our spell checker will only validate a single word rather than each word in a list of words.

## Work to do

1. Clone the course repository so that you can have easy access to the starter code.

   ```sh
   git clone https://github.com/softwareconstruction240/softwareconstruction.git
   ```

1. Thoroughly review the [specification](specification.md).
1. Set up your [IntelliJ project](setup/setup.md).
1. Implement the spelling corrector by running and implementing the tests. Take careful notes for what you did.
1. Pass off the project with a TA.
1. Review your notes and then, without referencing them, reimplement the spelling corrector. Record how long it took you to complete the project. If you can complete the project in the required amount of time, then you are ready for the exam.

## Motivation

The propose of writing the `Spelling Corrector` is to give you experience with writing a small Java program before we jump into the larger `Chess` project. If you understand everything that the `Spelling Corrector` teaches and can write it in the time limit required by the exam, then you should feel confident that you will be successful with the rest of the class.

## Concepts

The concepts we will cover as part of your work on the spelling corrector include the following.

1. What the `toString()` method does and how to override it
1. What the `equals(...)` method does and how to override it
1. How hash tables work and why we need a `hashcode()` method
1. How to override the `hashcode()` method
1. How the Spelling Corrector algorithm works
1. How a `Trie` represents words
1. How to implement a `Trie`
1. The requirements of the assignment

## Java Videos

- 🎥 [Classes and Objects Overview](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=8d7440ec-313d-45d1-891f-ad5f01307ab8&start=0)
- 🎥 [The `Object` Super Class](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=1de40809-379f-44fd-8ffe-ad5f01307a86&start=0)
- 🎥 [Overriding toString()](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=cc129f1b-ae0f-44b0-a424-ad5f01307ae4&start=0)
- 🎥 [Overriding equals()](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=7ecb0a44-16bc-4fa7-b924-ad5f01307b29&start=0)
- 🎥 [Hash Tables](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=42265b8a-aced-457d-a494-ad5f0130d9a9&start=0)
- 🎥 [Static Variables and Methods](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=d7e4aa43-754c-494a-9638-ad5f01310a45&start=0)

## Spelling Corrector Videos

- 🎥 [Spelling Corrector Overview](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=6e35d575-e624-4d21-b7bb-ad5f0131ade2&start=0)
- 🎥 [Algorithm](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=c3ca97d8-7449-4b2b-b97f-ad5f0134ae18&start=0)
- 🎥 [`Trie` Data Structure](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=1daf0cdd-e9ef-437c-a21a-ad5f01083a91&start=0)
- 🎥 [Specification and Requirements](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=f39bfc86-82cc-4e07-95d3-ad5f010d8c7e&start=0)
- 🎥 [Trie Implementation](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=440a9061-b72c-4118-9fc7-ad5f012fc62f&start=0)
