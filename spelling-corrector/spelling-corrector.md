# Spelling Corrector

You are familiar with spell checkers. For most spell checkers, a candidate word is considered to be spelled correctly if it is found in a long list of valid words called a dictionary. Google provides a more powerful spell corrector for validating the keywords we type into the input text box. It not only checks against a dictionary, but, if it doesn’t find the keyword in the dictionary, it suggests a most likely replacement. To do this it associates with every word in the dictionary a frequency, the percent of the time that word is expected to appear in a large document. When a word is misspelled (i.e. it is not found in the dictionary) Google suggests a `similar` word (`similar` will be defined later) whose frequency is larger or equal to any other `similar` word.

In this project you will create such a spell corrector. There is one major difference. Our spell checker will only validate a single word rather than each word in a list of words.

## Work to do

1. Thoroughly review the [specification](specification.md).
1. Set up your [IntelliJ project](setup/setup.md).
1. Implement the spelling corrector by running and implementing the tests. Take careful notes for what you did.
1. Pass off the project with a TA.
1. Review your notes and then, without referencing them, reimplement the spelling corrector. Record how long it took you to complete the project. If you can complete the project in the required amount of time, then you are ready for the exam.

## Motivation

The propose of writing the `Spelling Corrector` is to give you experience with writing a small Java program before we jump into the larger `Chess` project. If you understand everything that the `Spelling Corrector` teaches and can write it in the time limit required by the exam, then you should feel confident that you will be successful with the rest of the class.
