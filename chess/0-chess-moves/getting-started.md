# Phase 0: Getting Started

At this point, you should have already made your own copy of the [Chess GitHub Repository](../chess-github-repository/chess-github-repository.md) and made changes from the command line. Now, we will open the project in an Integrated Development Environment (IDE). IDEs assist developers when working on large software projects by providing tools for writing, debugging, and testing code.


```masteryls
{"id":"57d595a3-6ae5-40a9-a512-041c1c1cd198", "title":"Phase 0: Getting started", "type":"multiple-choice" }

- [x] I used the [Chess GitHub Repository](../chess-github-repository/chess-github-repository.md) template to create a repository in my GitHub account and have cloned it locally.
  **You're ready to start coding!** Creating your repository from the template and cloning it locally gives you the starting code and a place to record your progress.

  Remember to commit and push often as you work. Your commit history is part of every phase's requirements.

- [ ] I was not able to successfully create the repo and am reaching out to a TA for help.
  Reaching out to a TA is exactly the right move. Repository setup problems are usually quick to fix with a second pair of eyes.

  While you wait, reread the Chess GitHub Repository instructions and note exactly where things went wrong, including any error messages. That detail will help your TA solve the problem quickly.
```



## Open With IntelliJ

Once you have cloned the chess repository locally, follow these steps to set up your Chess project.

Open the project directory in IntelliJ to start developing, running, and debugging your code. Make sure you **OPEN** the project rather than creating a new project.

1. Open IntelliJ. (We assume you have already installed the IDE for previous coursework.)
2. Choose **File > Open** and select the `chess` folder in the location where you cloned it.

The repository already contains IntelliJ configuration files. Creating a new project instead of opening the existing one will cause configuration errors.

When the project opens, it should look like the image below. The `client`, `server`, and `shared` folders should be at the root level and marked with a blue square icon (indicating they are modules).

![open intellij](open-intellij.png)

You can confirm that the modules are set up correctly by going to **File > Project Structure > Modules** and verifying that only `client`, `server`, and `shared` are listed. Feel free to ask a TA for help if your structure looks different.

![verify modules](verify-modules.png)

## Turn off AI

> [!WARNING]
>
> **Using AI-generated code in this course is not permitted.** Failing to follow the instructions in this section will be considered **cheating**.

All AI coding tools must be turned off for this project. We want you to understand the code you write; having AI author code for you can be a hindrance to your learning. If you have an AI coding assistant (such as GitHub Copilot) installed in your IDE, you must disable it. Using AI to write your code may flag our plagiarism detection system. If you are unsure about your use of AI, check the syllabus in Canvas or ask an instructor.

Specifically, IntelliJ Ultimate Edition includes a local deep learning model and potentially a cloud-based LLM that suggests code completions. (These features are not included in the Community Edition). To turn off **Full Line** code completion, follow these steps:

1. Open IntelliJ Settings by pressing `Ctrl` + `,` (Windows/Linux) or `⌘` + `,` (macOS), or by navigating to **File > Settings**.
2. In the *Settings* window, select **Editor > General > Inline Completion** from the left-hand sidebar.
3. Uncheck **Enable local Full Line completion suggestions**. If there is a checkbox for cloud-based completion, disable that as well.
4. Click **OK** to save your changes.

![inline completion settings](inline-completion.png)


```masteryls
{"id":"d6399d4e-74c3-4eb3-bdf4-799a1e18f01e", "title":"Disabled AI", "type":"multiple-choice" }
I confirm that I have:

- [x] Disabled AI in my development environment for this class
  **Thank you for confirming.** Working through the problems yourself builds the debugging and design skills this course is meant to develop.

  When you get stuck, use the course's help resources: TAs, peers, and the instructor. Struggling through a problem with the right support is where most of the learning happens.

- [ ] Not disabled AI and have chosen not to take this class
  Thanks for being honest about your choice. This course requires that AI assistance be disabled so that you build these skills yourself.

  If you've reconsidered and want to stay in the class, disable the AI features in your development environment and return to this question. If you have concerns about the policy, talk with the instructor.
```
