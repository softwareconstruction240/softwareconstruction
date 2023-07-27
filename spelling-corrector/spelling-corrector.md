# Spelling Corrector

You are familiar with spell checkers. For most spell checkers, a candidate word is considered to be spelled correctly if it is found in a long list of valid words called a dictionary. Google provides a more powerful spell corrector for validating the keywords we type into the input text box. It not only checks against a dictionary, but, if it doesn’t find the keyword in the dictionary, it suggests a most likely replacement. To do this it associates with every word in the dictionary a frequency, the percent of the time that word is expected to appear in a large document. When a word is misspelled (i.e. it is not found in the dictionary) Google suggests a “similar” word (“similar” will be defined later) whose frequency is larger or equal to any other “similar” word.

In this project you will create such a spell corrector. There is one major difference. Our spell checker will only validate a single word rather than each word in a list of words.

For this program we need a dictionary similar to Google’s. Our dictionary is generated using a large text file. The text file contains a large number of unsorted words. Every time your program runs it will create the dictionary from this text file. The dictionary will contain every word in the text file and a frequency for that word. Instead of storing a percent, your program need only store the number of times the word appears in the text file.

When storing or looking up a word in the dictionary we want the match to be case insensitive. That is, a new or candidate word matches a word in the dictionary independent of the case of the letters in the respective words. Thus, the word “move” matches “Move”. If the strings “apple”, “Apple”, and “APPLE” each appear once in the input file, then your representation of the word (it could be any of the three words or some other variation) would appear once and would have a frequency of 3 associated with it.

## Work to do

1. Thoroughly review [specification](specification.md).
1. Set up your [IntelliJ project](setup.md).
1. Implement the spelling corrector by running and implementing the tests. Take careful notes for what you did.
1. Pass of the project with a TA.
1. Review your notes and then, without referencing them, reimplement the spelling corrector. Record how long it took you to complete the project. If you can complete the project in the required amount of time you are ready for the Exam.

## Motivation

The propose of writing the `Spelling Corrector` is to give you experience with writing a small Java program before we jump into the larger `Chess` project. If you understand everything that the `Spelling Corrector` teaches and can write it in the time limit required by the exam, then you should feel confident that you will be successful with the rest of the class.
