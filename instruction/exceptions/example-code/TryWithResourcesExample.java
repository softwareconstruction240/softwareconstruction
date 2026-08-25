import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TryWithResourcesExample {

    public static void main(String [] args) throws IOException {
        TryWithResourcesExample example = new TryWithResourcesExample();
        System.out.println(example.readFile(args[0]));
    }

    public String readFile(String fileName) throws IOException {
        File file = new File(fileName);
        String fileContents = "";

        try(FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr)) {

            String line;
            while((line = br.readLine()) != null) {
                fileContents += line;
                fileContents += "\n";
            }

            return fileContents;
        }
    }
}
