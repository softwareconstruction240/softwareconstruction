import java.io.File;
import java.io.IOException;

public class FileReadingWithExceptions {

    public String readFile(String fileName) throws IOException {
        File file = new File(fileName);
        String fileContents = "<Insert code to read the file here>";
        return fileContents;
    }
}
