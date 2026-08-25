import java.io.File;

public class FileReadingWithoutExceptions {

    public boolean readFile(String fileName, String fileContents) {
        boolean success = true;

        File file = new File(fileName);

        if(!file.exists()) {
            success = false;
        }

        if(success) {
            fileContents = "<Insert code to read the file here>";

            if(fileContents == "") {
                success = false;
            }
        }

        return success;
    }
}
