import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ScannerExample3 {
    public static void main(String[] args) throws IOException {
        ScannerExample3 scannerExample = new ScannerExample3();

        if(args.length == 1) {
            scannerExample.processFile(args[0]);
        } else {
            scannerExample.usage();
        }
    }

    public void processFile(String filePath) throws IOException {
        File file = new File(filePath);

        try(FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            Scanner scanner = new Scanner(br)) {
            scanner.useDelimiter("((#[^\\n]*\\n)|(\\s+))+");

            while (scanner.hasNext()) {
                String str = scanner.next();
                System.out.println(str);
            }
        }
    }

    private void usage() {
        System.out.println("\nUSAGE: java ScannerExample3 <input-file>");
    }
}
