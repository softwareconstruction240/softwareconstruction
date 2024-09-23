# Chess Game - Getting Started

📁 [Starter code](starter-code)

Take the following steps to set up your Chess project.

⚠ Make sure you have already completed the deliverable for creating your [Chess GitHub Repository](../chess-github-repository/chess-github-repository.md).

## Adding the Template Chess Project

1. Unzip the [starter-code/chess.zip](starter-code/chess.zip) file to the directory where you cloned the Chess GitHub Repository. You should run this command from inside of your your personal projects directory. 

   ```sh
   unzip <class-repo-dir>/chess/0-chess-moves/starter-code/chess.zip
   ```

   ⚠ Note: `unzip` does not exist on Windows. You will need to unzip the file using a different utility, such as using the Windows File Explorer. However, if you use the Windows File Explorer it may create an duplicate parent `chess` directory. Make sure you move the files out of the extra `chess` directory and then delete the extra `chess` directory before you proceed. Make sure you are also moving hidden files (files that start with a .) there should be a .idea and .gitignore file that are with the other project files. 

   With the chess starter code unzipped, your chess repository top level directory structure should look like the following:

   ```
   chess   (This should be your GitHub repository root directory)
   ├── .idea
   ├── .git
   ├── client
   ├── server
   ├── shared
   └── starter-code
   ```

   ⚠ If you do not see all of these files in your folder go make sure that you moved all the hidden files.

1. Check that git registers all the files
   ```sh
   git status
   ```
   You should see all the new files in a long list marked as untracked.
   
1. Commit and push the changes to GitHub
   ```sh
   git add .
   git commit -am "init(phase0)"
   git push
   ```

## Open With IntelliJ

Open the project directory in IntelliJ to start developing, running, and debugging your code using an IDE. Make sure you **OPEN** the project rather than creating a new project. The contents of the folder already contain IntelliJ configuration files and creating a new project rather than opening an existing one will cause various errors.

 ⚠ If you get a prompt asking you to build with Maven, then make sure you skip that action.

![build with Maven](build-with-maven-prompt.png)

Your project should look like this when it opens with the client, server, and shared folders having a blue square and being at the root level. There should be no folder called chess showing up in your Intelij project. 

![open intellij](open-intellij.png)


