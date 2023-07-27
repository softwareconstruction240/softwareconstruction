# Chess Game - Getting Started

## Creating an IntelliJ Project

1. File -> New -> Project
2. Select `Java` and hit next
3. Continue through the prompts until you get to name your project. Choose a name for your project and hit finish
4. Right Click on `src` and select `Mark Directory as`->`Unmark as Sources Root`
5. In the `src` folder, create a subfolder named `main`. Right-click on `main` and select `Mark Directory as`->`Mark as Sources Root`. This folder will contain your chess game Java source code.
6. In the `src` folder, create a subfolder named `starter`. Right-click on `starter` and select `Mark Directory as`->`Mark as Sources Root`. This folder will contain the starter Java code that we provide to you.
7. In the `src` folder, create a subfolder named `test`. Right-click on `test` and select `Mark Directory as`->`Mark as Test Sources Root`. This folder will contain all Junit test cases, including the pass off test cases.
8. In a File Explorer (outside of IntelliJ), copy the `chess` folder from the provided [starter code](starter-code) into the `src/starter` folder.
9. In a File Explorer (outside of IntelliJ), copy the `passoffTests` folder from the provided [starter code](starter-code) into the `src/test` folder.

## Adding Dependencies

`Dependencies` are external libraries are used in your application. Java libraries are contained in JAR files (.jar). Maven provides an online repository of freely available Java libraries that you can include in your projects. You can go to [https://mvnrepository.com/](https://mvnrepository.com/) to browse all the libraries available through Maven. This section explains how to use Maven to add an external library dependency to your project.

1. Go to File -> Project Structure
2. In the pop-up go Modules -> Dependencies, and hit the `+` mark to add new dependency
3. Pick Library -> From Maven
4. In the resulting text box you can type the name of the library you want to add. If you know part of a dependency you can click the search button to look for matching dependencies. Add your dependency and click OK.
5. In the pop-up box you can just hit OK

### Dependencies Needed for Chess Game

- org.junit.jupiter:junit-jupiter:5.9.2
  - Scope: Test

After adding this dependency there should no longer be any red lines on any of the file names.
