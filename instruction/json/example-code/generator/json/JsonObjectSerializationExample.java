package generator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import domain.gson.CDFactory;
import domain.gson.Catalog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;

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
public class JsonObjectSerializationExample {

    public static void main(String [] args) {
        JsonObjectSerializationExample example = new JsonObjectSerializationExample();

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
        System.out.println("USAGE: java JsonObjectSerializationExample outputFilePath");
    }

    private void generate(Catalog catalog, File file) throws IOException {
        // Register a type adapter to convert float prices in the form 9.9 to 9.90
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Float.class, new PriceSerializer())
                .setPrettyPrinting()
                .create();
        String jsonString = gson.toJson(catalog);

        try(FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(jsonString);
        }

        System.out.println(jsonString);
    }

    private static class PriceSerializer implements JsonSerializer<Float> {
        @Override
        public JsonElement serialize(Float value, Type type, JsonSerializationContext jsonSerializationContext) {
            BigDecimal bdPrice = new BigDecimal(value);
            bdPrice = bdPrice.setScale(2, RoundingMode.HALF_UP);
            return new JsonPrimitive(bdPrice);
        }
    }
}
