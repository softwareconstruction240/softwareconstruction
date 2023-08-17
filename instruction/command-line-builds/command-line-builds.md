# Command-line Builds

🖥️ [Slides](https://docs.google.com/presentation/d/1Li3p-74-4LoosHmyB_u2b5kpi_vkNgz6/edit#slide=id.p1)

📖 **Required Reading**:

- [Maven in 5 Minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)

📖 **Optional Reading**:

- OPTIONAL: [Maven Getting Started Guide](https://maven.apache.org/guides/getting-started/index.html)

When developing a software project, you must frequently "build" the software. The "build process" includes activities such as compiling the code, compiling and running automated tests cases, packaging the code into distributable files, generating code and test quality reports, etc. IDEs such as IntelliJ are great, but using them to build a project can be a partially manual and error-prone process. For many reasons, it can be advantageous to create a "command-line build" for your project, which allows anyone to compile, run, test, package, deploy, and validate the software using simple shell (ie, command-line) commands. Fortunately, there are many tools available to help create a command-line build for a project, such as Apache Maven. This lecture discusses concepts relevant to command-line builds, and demonstrates those concepts by using Maven to create a command-line build for the Chess project.

## Things to Understand

- The benefits of using command-line builds
- All popular language environments have tools for creating command-line builds
- How to run a command-line build
- Typical features of command-line build tools, such as dependency management
- Have a general understanding of how Maven can be used to create a command-line build for a Java project
- Have a general understanding of how Maven can be used with IntelliJ

## Demonstration code

📁 [Building Chess with Maven](example-code/)
