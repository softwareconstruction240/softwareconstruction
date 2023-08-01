import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ScannerExample1 {
    public static void main(String[] args) throws FileNotFoundException {
        ScannerExample1 scannerExample = new ScannerExample1();

        if(args.length == 1) {
            scannerExample.processFile(args[0]);
        } else {
            scannerExample.usage();
        }
    }

    public void processFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);

        try(Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String str = scanner.next();
                System.out.println(str);
            }
        }
    }

    private void usage() {
        System.out.println("\nUSAGE: java ScannerExample2 <input-file>");
    }
}
