# Style Checking

🖥️ [Slides](https://docs.google.com/presentation/d/1xy5WXrwQuZLEOtAX7W9B9ZgICc047wQz/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

🖥️ [Lecture Videos](#videos)

Adhering to best practices and being consistent in your code structure, styles, and formatting is a hallmark of a professional developer. Historically, a proficient development team, would develop a document that outlined the standards that the team was expected to adhere to. This included common idioms to follow, anti-patterns to avoid, and a description of proper formatting and coding layout. As an example, you can reference [Google's style guide for Java](https://google.github.io/styleguide/javaguide.html). You will see that it describes very basic things like how you indent your code, how you order your import statements, and what is the limit on line length.

## Drop the Ego

Style guides are usually very opinionated. This is because they are attempting to create a common representation of code that enables the team to focus on what the code does, and not what the code looks like. Being willing to put the team first is an important part of what is called [Egoless Development](https://blog.codinghorror.com/the-ten-commandments-of-egoless-programming/), and should be a quality that you strive to refine as part of your professional development.

One of the easiest ways to drop the ego, and conform to the team, is to simply use automated tools for organizing, formatting, and styling your code. Modern programming languages such as `Go` and `Rust` actually deploy linters and formatters as part of their language toolkit. This makes it so that any code written in that language is always formatted in a similar way. `Python` takes it a step further and actually makes the formatting part of the language.

## Leveraging Tools

IntelliJ is a powerful development tool. Not only does it help you write and debug your code, it has functionality for formatting, suggesting style/idiomatic changes, and refactoring your code. Tools like this also help you clean up your code and discover features of Java that you might not be familiar with.

### Autoformatting

You can either use the format command to format the current file, or you can enable automatic formatting in IntelliJ so that it formats a file whenever it is saved. The following video show the use of both these formatting methods.

![Auto format](autoFormat.gif)

### Idiomatic Suggestions

To have IntelliJ check your files for idiomatic problems and suggest ways to clean up your code, you can invoke the `Problem` checker, or click on the yellow light bulbs when they appear next to your code. In the top right corner of the editor is a control that will help you step through all the problems in the file, or display a green checkmark if everything looks good. IntelliJ will also automatically run the `Problem` checker when you push your code to GitHub.

![Style Checker](styleChecker.gif)

It is important to get in the habit of consistently using these tools while you are developing your code. Otherwise, you end up with hundreds of problems that need to be corrected and the temptation will increase to just ignore them all.

### Refactoring Tools

IntelliJ also contains many tools for refactoring your code. Using these tools will improve style of your code. The following demonstrates extracting a method.

![Refactor method](refactorTool.gif)

## <a name="videos"></a>Videos (5:44)

- 🎥 [Code Style Checker (5:44)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=20337bb3-64b5-4203-942f-b1a001136761&start=0) - [[transcript]](https://github.com/user-attachments/files/17752074/CS_240_Code_Style_Checking_Transcript.pdf)
