package parser.json;

import domain.CD;

import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Requires the javax.json library (.jar file) from glassfish.org. Get the .jar from Maven or Gradle:
 *
 * Maven
 *
 * <dependency>
 *     <groupId>org.glassfish</groupId>
 *     <artifactId>javax.json</artifactId>
 *     <version>1.1.4</version>
 * </dependency>
 *
 *
 * Gradle
 *
 * compile group: 'org.glassfish', name: 'javax.json', version: '1.1.4'
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
