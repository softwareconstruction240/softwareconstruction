# ♕ Phase 2: Chess Server Design

- [Chess Application Overview](../chess.md)
- [Getting Started](getting-started.md)
- [Starter Code](starter-code)

🖥️ [Slides](https://docs.google.com/presentation/d/12zsEJ-at5DsbKNy7a0Eac0D1ZWa4RBIC/edit?usp=sharing&ouid=117271818978464480745&rtpof=true&sd=true)

In this part of the Chess Project, you will create the class and package structure for your Chess Server. Read the [web-api.md](../3-web-api/web-api.md) document for phase 3 of the project to learn about the functionality and web apis you need to include in your design. Create the Java packages you will need for your server implementation. Then, populate each package with the classes you will need. For this assignment you need not create fully-functional classes. Rather, for each class you only need to declare the public methods, constructors, and fields the class will need. Add minimal bodies to each method and constructor sufficient to make the class compile. If a method has no return value(void), its body may be empty. If a method has a return value, add a return statement so the method will compile (e.g., return null or zero or false or whatever works). The code needs to compile so that we can use the Javadoc tool to generate nicely-formatted documentation for your design that you can submit for grading.

⚠ Carefully read the specifications for the next Chess deliverable: [Web API](../3-web-api/web-api.md). This will help you understand the purpose and structure of the classes you are designing in this phase.

The following diagram represents one possible design that you might consider.

![sever design architecture](server-design-architecture.png)

You need to create classes for each Service, each Request and Result, each Model object, and each Data Access Object (DAO). At this point in the project, you do not need to create the Client, the Server class, the Handler classes, or the relational Database. You do not need to code any of the algorithms, just create the structure of the packages, classes, methods, and fields.

## Javadoc

[Javadoc](https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html) is a tool that parses your code and converts specially formatted comments in to HTML documentation. This is the same tool that is used to generate the official Java API documentation.

Your deliverable for this phase is to write Javadoc comments for each class, constructor, and method in your server design. Your Javadocs will be used by the instructor to determine the validity of your design. Each class, constructor, and method Javadoc comment should supply @param, @return, @throws, @link, and @see tags as appropriate. You do need not write Javadoc comments for getters, setters, or methods that are documented on an interface. You do not need to write Javadoc for code that is outside of your service design.

After creating and commenting your classes, use the following instructions to generate your Javadocs.

1. Select packages you want Javadocs for
1. Select Tools -> Generate JavaDoc...
1. Create an empty directory that will contain the files created by Javadoc, and Select it as the output directory
1. Select "private" on the private/package/protected/public scale
1. Deselect "Include test sources"
1. Click OK. Javadocs will be in the specified output directory.
1. Compress Javadocs into a zip archive

## Submission and Grading

Submit your Javadocs zip archive to the `Chess Server Design` Canvas Assignment.

### Grading Rubric

When initially graded, your design will be given one of three scores:

| Score | Criteria                                                                                                                                                                                                                                          |
| ----: | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
|  100% | Your design is mostly correct with only minor adjustments needed. Read TA suggestions for improvement in Canvas.                                                                                                                                  |
|   50% | Your design has significant deficiencies. Meet with a TA to discuss your design, ideally the same TA who originally graded your design. Improve and resubmit your design within one week of initial grading, and receive a maximum score of 100%. |
|    0% | The submitted design was not a serious attempt at doing the assignment correctly. Resubmit your design within one week of initial grading and receive a maximum score of 50%.                                                                     |
