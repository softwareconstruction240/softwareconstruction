import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ScannerExample {
    public static void main(String[] args) throws FileNotFoundException {
        ScannerExample scannerExample = new ScannerExample();

        if(args.length == 1) {
            scannerExample.processFile(args[0]);
        } else {
            scannerExample.usage();
        }
    }

    public void processFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);

        while(scanner.hasNext()) {
            String str = scanner.next();
            System.out.println(str);
        }
    }

    private void usage() {
        System.out.println("\nUSAGE: java ScannerExample2 <input-file>");
    }
}
