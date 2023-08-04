# Java Input and Output (IO)

🖥️ [Slides](https://docs.google.com/presentation/d/1V_tMHZGJMwlB2it1C-KY-AtSMeXGSOUD/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

📖 **Required Reading**: Core Java SE 9 for the Impatient

- Chapter 9: Processing Input and Output. _Only read:_
  - section 1: Input, Output Streams, Readers, and Writers
  - section 2: Paths, Files and Directories

Input and output refers to when you read or write data from a source outside of your program. Commonly this occurs from devices such as file storage, the network, a keyboard, or a printer. Here is a simple example that reads a file as `input` and writes the contents out to the screen as `output`.

```java
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadFile {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File(args[0]);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                System.out.println(scanner.next());
            }
        }
    }
}
```

## Things to Understand

- How input and output streams work
- The difference between streams and readers and writers
- How to chain streams, readers, or writers together to get complex behavior
- How to convert from an input stream to a reader using the InputStreamReader class
- How to convert from a writer to an output stream using the OutputStreamWriter class
- How to use the scanner class
- Uses of the file class

## Videos

- 🎥 [Overview](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=9c064639-8e05-4d4c-b458-ad64014cbb24&start=0)
- 🎥 [Streams](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=8db201b9-9a04-4cbf-8a99-ad64014ddd56&start=0)
- 🎥 [Readers and Writers](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=66d67329-cc52-4533-a2b4-ad64015237cf&start=0)
- 🎥 [The Scanner Class](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=7dcd4a30-0b07-4e6a-9341-ad640153f4b8&start=0)
- 🎥 [Other Ways to Read and Write Files](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=ef902529-f41c-402d-be3d-ad640156133a&start=0)

## Demonstration code

📁 [Compress](example-code/Compress.java)

📁 [CopyFileExample](example-code/CopyFileExample.java)

📁 [Decompress](example-code/Decompress.java)

📁 [LegacyCompress](example-code/LegacyCompress.java)

📁 [LegacyDecompress](example-code/LegacyDecompress.java)

📁 [ScannerExample1](example-code/ScannerExample1.java)

📁 [ScannerExample2](example-code/ScannerExample2.java)

📁 [ScannerExample3](example-code/ScannerExample3.java)
