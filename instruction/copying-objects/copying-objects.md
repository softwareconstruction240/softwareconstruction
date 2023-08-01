# Copying Objects

🖥️ [Slides](https://docs.google.com/presentation/d/1TAl9a41zLMyQmuQTYgxmYct6gsWgWopc/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

When you make a copy of an object you must consider the important difference between making a copy of the data and making a copy of a pointer to the data. When you make a copy of the data you make an independent duplicate of the data. When you copy a pointer to data, the copy can change when the data that the pointer references changes.

Things to understand:

- The difference between a shallow copy and a deep copy
- How to use `copy constructors` to implement deep copies
- How to use `clone` methods to implement deep copies

## Videos

- 🎥 [Copying Objects - Theory](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=9c3422bf-3b1e-40f0-b221-ad6b011daa82&start=0)
- 🎥 [Copying Objects - Implementation](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=102c1fdc-516f-4058-957b-ad6b011ff9f4&start=0)

## Demonstration code

### Copy

📁 [Course](example-code/Course.java)

📁 [Faculty](example-code/Faculty.java)

📁 [Person](example-code/Person.java)

📁 [Student](example-code/Student.java)

📁 [Test](example-code/Test.java)

📁 [YearInSchool](example-code/YearInSchool.java)

### Clone

📁 [Person](example-code/clone/Person.java)

📁 [Person2](example-code/clone/Person2.java)

📁 [Team](example-code/clone/Team.java)
