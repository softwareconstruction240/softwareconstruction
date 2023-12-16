# Chess Game - Getting Started

📁 [Starter code](starter-code)

Take the following steps to set up your Chess project.

## Creating Your Chess GitHub Repository

1. Create a GitHub repository for your chess project

   1. Name it `chess`.
   1. Mark the repository as `public` so that it can be reviewed by the TAs and instructors.
   1. Select the option to create a license file.

      ![create repo](create-repo.png)

1. Open a command line console window.
1. Clone the repository to your development environment. Make sure you put the clone of the repository in a directory that you use for this class's coursework. These commands will look something like the following.

   ```sh
   cd ~/byu/cs240
   git clone https://github.com/YOURACCOUNTHERE/chess.git
   cd chess
   ```

## Adding the Template Chess Project

1. Unzip the [starter-code/chess.zip](starter-code/chess.zip) file into your newly created chess project directory.
   ```sh
   unzip <class-repo-dir>/chess/0-chess-moves/starter-code/chess.zip
   ```
1. Commit and push the changes to GitHub
   ```sh
   git add .
   git commit -am "init(phase0)"
   git push
   ```

## Open With IntelliJ

Open the project directory in IntelliJ start developing, running, and debugging your code using an IDE.

![open intellij](open-intellij.png)

If you get a prompt asking you to build with Maven, then make sure you skip that action.

![build with Maven](build-with-maven-prompt.png)
