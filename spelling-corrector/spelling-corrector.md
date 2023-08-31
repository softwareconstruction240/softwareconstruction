# Spelling Corrector

🖥️ [Slides](https://docs.google.com/presentation/d/1FkxHpWkpQr-YIdtIqQeYwnAOekQ75Slg/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

As a preparatory step towards mastering the art of constructing software you will build a spelling corrector. This will help you learn the basics of building a Java program before we move to the more complex chess program.

You are probably already familiar with spelling correctors. For most correctors, a candidate word is considered to be spelled correctly if it is found in a long list of valid words called a dictionary. Google provides a more powerful spelling corrector for validating the keywords we type into the input text box. It not only checks against a dictionary, but, if it doesn’t find the keyword in the dictionary, it suggests a most likely replacement. To do this it associates with every word in the dictionary a frequency, the percent of the time that word is expected to appear in a large document. When a word is misspelled (i.e. it is not found in the dictionary) Google suggests a `similar` word (`similar` will be defined later) whose frequency is larger or equal to any other `similar` word.

In this project you will create such a spell corrector. There is one major difference. Our spell checker will only validate a single word rather than each word in a list of words.

When you are done implementing your spelling corrector you will have a console application that takes a dictionary and a word as input. The corrector will then analyze the word and suggest a "corrected" replacement for the word. The following shows an example of a working spelling corrector.

```sh
➜  java spell.Main dictionary.txt truble

Suggestion: trouble
```

## Work to do

1. Clone the course repository so that you can have easy access for copying the [starter code](starter-code/).

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

1. How the Spelling Corrector algorithm works
1. How a `Trie` represents words
1. How to implement a `Trie`
1. The requirements of the assignment

## Spelling Corrector Videos

- 🎥 [Spelling Corrector Overview](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=6e35d575-e624-4d21-b7bb-ad5f0131ade2&start=0)
- 🎥 [Algorithm](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=c3ca97d8-7449-4b2b-b97f-ad5f0134ae18&start=0)
- 🎥 [`Trie` Data Structure](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=1daf0cdd-e9ef-437c-a21a-ad5f01083a91&start=0)
- 🎥 [Specification and Requirements](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=f39bfc86-82cc-4e07-95d3-ad5f010d8c7e&start=0)
- 🎥 [Trie Implementation](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=440a9061-b72c-4118-9fc7-ad5f012fc62f&start=0)
