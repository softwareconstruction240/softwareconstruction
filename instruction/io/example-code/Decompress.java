import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class Decompress {

    private static final int CHUNK_SIZE = 512;

    public static void main(String[] args) {
        Decompress compress = new Decompress();

        if(args.length == 2) {
            try {
                compress.decompressFile(args[0], args[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            compress.usage();
        }
    }

    public void decompressFile(String inputFilePath, String outputFilePath) throws IOException {
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);

        try(FileInputStream fis = new FileInputStream(inputFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            GZIPInputStream zipis = new GZIPInputStream(bis);

            FileOutputStream fos = new FileOutputStream(outputFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos)) {

           byte [] chunk = new byte[CHUNK_SIZE];
           int bytesRead;
           while((bytesRead = zipis.read(chunk)) > 0) {
               bos.write(chunk, 0, bytesRead);
           }
        }
    }

    private void usage() {
        System.out.println("\nUSAGE: java Compress <input-file> <output-file>");
    }
}