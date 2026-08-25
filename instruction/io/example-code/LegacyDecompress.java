import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class LegacyDecompress {

    private static final int CHUNK_SIZE = 512;

    public static void main(String[] args) {
        LegacyDecompress compress = new LegacyDecompress();

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

        GZIPInputStream zipis = null;
        BufferedOutputStream bos = null;

        try {
            FileInputStream fis = new FileInputStream(inputFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            zipis = new GZIPInputStream(bis);

            FileOutputStream fos = new FileOutputStream(outputFile);
            bos = new BufferedOutputStream(fos);

            byte[] chunk = new byte[CHUNK_SIZE];
            int bytesRead;
            while ((bytesRead = zipis.read(chunk)) > 0) {
                bos.write(chunk, 0, bytesRead);
            }
        } finally {
            if(zipis != null) {
                zipis.close();
            }

            if(bos != null) {
                bos.close();
            }
        }
    }

    private void usage() {
        System.out.println("\nUSAGE: java LegacyCompress <input-file> <output-file>");
    }
}