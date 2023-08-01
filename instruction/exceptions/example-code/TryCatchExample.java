import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TryCatchExample {

    public static void main(String [] args) {
        TryCatchExample example = new TryCatchExample();
        System.out.println(example.readFile(args[0]));
    }

    public String readFile(String fileName) {
        File file = new File(fileName);
        String fileContents = "";

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line;
            while((line = br.readLine()) != null) {
                fileContents += line;
                fileContents += "\n";
            }

            return fileContents;

        } catch(IOException ex) {
            ex.printStackTrace();
            return fileContents;
        }
    }
}
