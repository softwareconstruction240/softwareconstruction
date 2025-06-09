package parser.json;

import domain.CD;
import jakarta.json.Json;
import jakarta.json.stream.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Requires the jakarta.json-api and jakarta.json libraries. Get the .jars from Maven or Gradle:
 *
 * Maven
 *
 * <dependency>
 *     <groupId>jakarta.json</groupId>
 *     <artifactId>jakarta.json-api</artifactId>
 *     <version>2.1.3</version>
 * </dependency>
 *
 * <dependency>
 *     <groupId>org.eclipse.parsson</groupId>
 *     <artifactId>jakarta.json</artifactId>
 *     <version>1.1.6</version>
 * </dependency>
 *
 *
 * Gradle
 *
 * implementation group: 'jakarta.json', name: 'jakarta.json-api', version: '2.1.3'
 * implementation group: 'org.eclipse.parsson', name: 'jakarta.json', version: '1.1.6'
 *
 */
public class JsonStreamParserExample {

    private boolean inCdAttribute = false;

    public static void main(String[] args) {
        JsonStreamParserExample example = new JsonStreamParserExample();

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
        System.out.println("USAGE: java JsonStreamParserExample inputFilePath");
    }

    private List<CD> parse(File file) throws IOException {
        List<CD> cds = new ArrayList<>();

        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            JsonParser parser = Json.createParser(bufferedReader);

            String title = null;
            String artist = null;
            String country = null;
            String company = null;
            float price = 0.0f;
            int year = 0;

            boolean inCd = false;

            while (parser.hasNext()) {
                JsonParser.Event event = parser.next();

                if (event.equals(JsonParser.Event.KEY_NAME)) {
                    String key = parser.getString();

                    switch (key.toUpperCase()) {
                        case "CD":
                            inCd = true;
                            break;
                        case "TITLE":
                            title = processAttribute(parser);
                            break;
                        case "ARTIST":
                            artist = processAttribute(parser);
                            break;
                        case "COUNTRY":
                            country = processAttribute(parser);
                            break;
                        case "COMPANY":
                            company = processAttribute(parser);
                            break;
                        case "PRICE":
                            price = Float.parseFloat(processAttribute(parser));
                            break;
                        case "YEAR":
                            year = Integer.parseInt(processAttribute(parser));
                            break;
                    }
                } else if (event.equals(JsonParser.Event.END_OBJECT)) {
                    if (inCdAttribute) {
                        inCdAttribute = false;
                    } else if (inCd) {
                        inCd = false;
                        cds.add(new CD(title, artist, country, company, price, year));

                        title = null;
                        artist = null;
                        country = null;
                        company = null;
                        price = 0.0f;
                        year = 0;
                    }
                }
            }
        }

        return cds;
    }

    private String processAttribute(JsonParser parser) {
        inCdAttribute = true;
        parser.next();
        return parser.getString();
    }
}
