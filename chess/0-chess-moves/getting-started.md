# Chess Game - Getting Started

📁 [Starter code](https://github.com/softwareconstruction240/chess)

At this point, you have already made your own copy of the [Chess GitHub Repository](../chess-github-repository/chess-github-repository.md) and made changes from the command line. Now, we will open the project in an Integrated Development Environment (IDE). IDE's greatly help and assist the developer while working on large software projects.

Take the following steps to set up your Chess project.

## Open With IntelliJ

Open the project directory in IntelliJ to start developing, running, and debugging your code using an IDE. Make sure you **OPEN** the project rather than creating a new project.

1. Open IntelliJ. We assume you already have the IDE downloaded from previous required coursework.
2. Choose File > Open > and select the chess folder in the location you created it earlier.

The contents of the folder already contain IntelliJ configuration files and creating a new project rather than opening an existing one will cause various errors.

> [!NOTE]
> If you get a prompt asking you to build with Maven, then make sure you skip that action.

![build with Maven](build-with-maven-prompt.png)

Your project should look like this when it opens with the client, server, and shared folders having a blue square and being at the root level. There should be no folder called chess showing up in your Intelij project, only the items inside of the chess folder should be visible.

![open intellij](open-intellij.png)

## Turn off IntelliJ Ultimate AI

If you are using IntelliJ Ultimate Edition, it comes with a local deep learning model that will help finish lines. (This feature does not come with the Community edition). While this is normally a helpful feature, we want you to understand the code you write. In addition, because the programming exam repeats the code you write in this phase, the line completion AI will suggest code you will write in Phase 0. **It will be considered cheating if you leave this feature on.** To turn off Full Line code completion, follow these steps:

1. Go to IntelliJ Settings