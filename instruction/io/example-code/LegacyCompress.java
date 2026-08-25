import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class LegacyCompress {

    private static final int CHUNK_SIZE = 512;

    public static void main(String[] args) {
        LegacyCompress compress = new LegacyCompress();

        if(args.length == 2) {
            try {
                compress.compressFile(args[0], args[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            compress.usage();
        }
    }

    public void compressFile(String inputFilePath, String outputFilePath) throws IOException {
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);

        BufferedInputStream bis = null;
        GZIPOutputStream zipos = null;

        try {
            FileInputStream fis = new FileInputStream(inputFile);
            bis = new BufferedInputStream(fis);

            FileOutputStream fos = new FileOutputStream(outputFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            zipos = new GZIPOutputStream(bos);

            byte[] chunk = new byte[CHUNK_SIZE];
            int bytesRead;
            while ((bytesRead = bis.read(chunk)) > 0) {
                zipos.write(chunk, 0, bytesRead);
            }
        } finally {
            if(bis != null) {
                bis.close();
            }

            if(zipos != null) {
                zipos.close();
            }
        }
    }

    private void usage() {
        System.out.println("\nUSAGE: java LegacyCompress <input-file> <output-file>");
    }
}