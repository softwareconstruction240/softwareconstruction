package generator.json;

import domain.CD;
import domain.CDFactory;
import domain.Catalog;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Requires the json library (.jar file) from json.org (https://github.com/stleary/JSON-java). Get the .jar from Maven or Gradle:
 *
 * Maven
 *
 * <dependency>
 *     <groupId>org.json</groupId>
 *     <artifactId>json</artifactId>
 *     <version>20180813</version>
 * </dependency>
 *
 *
 * Gradle
 *
 * compile group: 'org.json', name: 'json', version: '20180813'
 *
 */
public class JsonDomGenerationExample {

    public static void main(String [] args) {
        JsonDomGenerationExample example = new JsonDomGenerationExample();

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
        System.out.println("USAGE: java JsonDomGenerationExample outputFilePath");
    }


    private void generate(Catalog catalog, File file) throws IOException  {
        try(FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            JSONObject jsonCatalog = buildCatalog(catalog);
            String jsonString = jsonCatalog.toString(4);
            bufferedWriter.write(jsonString);

            System.out.println(jsonString);
        }
    }

    private JSONObject buildCatalog(Catalog catalog) {
        JSONObject jsonCatalog = new JSONObject();

        JSONArray jsonCdArray = new JSONArray();
        for (CD cd : catalog.getCds()) {
            jsonCdArray.put( buildCD(cd) );
        }

        jsonCatalog.put( "CATALOG", jsonCdArray );

        return jsonCatalog;
    }

    private JSONObject buildCD(CD cd) {
        JSONObject jsonCD = new JSONObject();

        JSONObject jsonCDAttributes = buildCDAttributes(cd);

        jsonCD.put("CD", jsonCDAttributes);

        return jsonCD;
    }

    private JSONObject buildCDAttributes(CD cd) {
        JSONObject jsonCDAttributes = new JSONObject();

        jsonCDAttributes.put("TITLE", cd.getTitle());
        jsonCDAttributes.put("ARTIST", cd.getArtist());
        jsonCDAttributes.put("COUNTRY", cd.getCountry());
        jsonCDAttributes.put("COMPANY", cd.getCompany());
        jsonCDAttributes.put("PRICE", cd.getPrice());
        jsonCDAttributes.put("YEAR", cd.getYear());

        return jsonCDAttributes;
    }
}
