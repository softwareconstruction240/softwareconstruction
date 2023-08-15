# Concurrency and Multi-threaded Programming

🖥️ [Slides](https://docs.google.com/presentation/d/1ibtqBjYEzx45Nh9eLP5xq6jWKfVjVMpv/edit#slide=id.p1)

📖 **Optional Reading**: Core Java SE 9 for the Impatient

- OPTIONAL: Chapter 10: Concurrent Programming.

Programs can use threads to do multiple things at once. This can make programs more efficient and the user experience better. For example, a "server" program like the Chess server must be able to process multiple incoming client requests at the same time. Or, programs with user interfaces often need to perform tasks in the background while the user continues working (e.g., printing documents, backing up files, calling external web APIs, etc.). While threads make doing multiple things at once possible, they also making coding more complicated, and introduce bugs called "race conditions" (or race hazards) that occur when multiple threads access the same resources (data structures, file, etc.). This lecture provides a brief overview of concurrency, multi-threaded programming in Java, and some basic techniques on how to avoid race conditions. The Chess project is used as the primary example. Much more about this topic can be learned by taking CS 324.

## Things to Understand

- What is a thread?
- Creating and executing a simple thread in Java
- Using a thread pool (ExecutorService in Java) to run multiple threads
- What is a race condition (or race hazard)?
- How to use database transactions to avoid race conditions
- How to use "synchronized" methods and code blocks in Java to avoid race conditions
- How to avoid race conditions in the Chess server and client programs

## Demonstration code

📁 [Java Thread Example](example-code/src/demo/JavaThreadExample.java)

📁 [Java Thread Pool Example](example-code/src/demo/JavaThreadPoolExample.java)

📁 [File Race Condition Example](example-code/src/demo/FileRaceConditionExample.java)

📁 [Synchronized Stack Example](example-code/src/demo/Stack.java)
