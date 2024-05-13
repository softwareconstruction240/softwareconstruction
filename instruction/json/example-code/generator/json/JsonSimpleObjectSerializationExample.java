package generator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.CDFactory;
import domain.Catalog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Requires the gson library (.jar file). Get the .jar from Maven or Gradle:
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
 */
public class JsonSimpleObjectSerializationExample {

    public static void main(String [] args) {
        JsonSimpleObjectSerializationExample example = new JsonSimpleObjectSerializationExample();

        if(args.length == 1) {
            try {
                Catalog catalog = new Catalog(CDFactory.getCDs());
                example.generate(catalog, new File(args[0]));
            } catch (RuntimeException ex) {
                throw ex;
            } catch (Exception ex) {
                System.out.printf("Unable to generate file %s because of exception: %s\n", args[0], ex.toString());
            }
        } else {
            example.printUsage();
        }
    }

    private void printUsage() {
        System.out.println("USAGE: java JsonSimpleObjectSerializationExample outputFilePath");
    }

    private void generate(Catalog catalog, File file) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(catalog);

        try(FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(jsonString);
        }

        System.out.println(jsonString);
    }
}
