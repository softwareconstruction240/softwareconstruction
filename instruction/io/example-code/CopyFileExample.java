import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CopyFileExample {

    public static void main(String [] args) throws IOException {
        if(args.length != 2) {
            printUsage();
        } else {
            CopyFileExample fileCopier = new CopyFileExample();
            fileCopier.copy(args[0], args[1]);
        }
    }

    private static void printUsage() {
        System.out.println("USAGE: java CopyFileExample fromFile toFile");
    }

    private void copy(String from, String to) throws IOException {
        File fromFile = new File(from);
        File toFile = new File(to);

        try(FileReader fr = new FileReader(fromFile);
            BufferedReader br = new BufferedReader(fr);

            FileWriter fw = new FileWriter(toFile);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw)) {

            String line;
            while((line = br.readLine()) != null) {
                pw.println(line);
            }
        }
    }
}
