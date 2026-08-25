import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FinallyExample {

    public static void main(String [] args) {
        FinallyExample example = new FinallyExample();
        System.out.println(example.readFile(args[0]));
    }

    public String readFile(String fileName) {
        File file = new File(fileName);
        String fileContents = "";

        FileReader fr = null;
        BufferedReader br = null;

        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String line;
            while((line = br.readLine()) != null) {
                fileContents += line;
                fileContents += "\n";
            }

            return fileContents;

        } catch(IOException ex) {
            ex.printStackTrace();
            return fileContents;
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {}
            }

            if(fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {}
            }
        }
    }
}
