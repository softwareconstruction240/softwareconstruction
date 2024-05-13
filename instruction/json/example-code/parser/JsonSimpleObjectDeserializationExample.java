package parser;

import com.google.gson.Gson;
import domain.CD;
import domain.Catalog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Requires the gson library (.jar file) from google.com. Get the .jar from Maven or Gradle:
 *
 * Maven
 *
 * <dependency>
 *     <groupId>com.google.code.gson</groupId>
 *     <artifactId>gson</artifactId>
 *     <version>2.8.5</version>
 * </dependency>
 *
 *
 * Gradle
 *
 * compile group: 'com.google.code.gson', name: 'gson', version: '2.8.5'
 *
 */
public class JsonSimpleObjectDeserializationExample {

    public static void main(String[] args) {
        JsonSimpleObjectDeserializationExample example = new JsonSimpleObjectDeserializationExample();

        if (args.length == 1) {
            try {
                List<CD> cds = example.parse(new File(args[0]));
                for (CD cd : cds) {
                    System.out.println(cd);
                }
            } catch (RuntimeException ex) {
                throw ex;
            } catch (Exception ex) {
                System.out.printf("Unable to parse file %s because of exception: %s\n", args[0], ex.toString());
            }
        } else {
            example.printUsage();
        }
    }

    private void printUsage() {
        System.out.println("USAGE: java JsonSimpleObjectDeserializationExample inputFilePath");
    }

    private List<CD> parse(File file) throws IOException {
        try(FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            Gson gson = new Gson();
            Catalog catalog = gson.fromJson(bufferedReader, Catalog.class);
            return catalog.getCds();
        }
    }
}
