package generator.json;

import domain.CDFactory;
import domain.Catalog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonStreamGenerationExample {

    public static void main(String [] args) {
        JsonStreamGenerationExample example = new JsonStreamGenerationExample();

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
        System.out.println("USAGE: java JsonStreamGenerationExample outputFilePath");
    }


    private void generate(Catalog catalog, File file) throws IOException  {
        try(FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            // TODO: Implement
            System.out.println("Not Implemented");
        }
    }
}
