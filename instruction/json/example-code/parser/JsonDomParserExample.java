package parser;

import domain.CD;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class JsonDomParserExample {

    public static void main(String[] args) {
        JsonDomParserExample example = new JsonDomParserExample();

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
        System.out.println("USAGE: java JsonDomParserExample inputFilePath");
    }

    private List<CD> parse(File file) throws IOException {
        List<CD> cds = new ArrayList<>();

        try(FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            JSONTokener tokener = new JSONTokener(bufferedReader);
            JSONObject rootObj = new JSONObject(tokener);

            JSONArray cdArr = rootObj.getJSONArray("CATALOG");
            for (int i = 0; i < cdArr.length(); ++i) {

                JSONObject elemObj = cdArr.getJSONObject(i);
                JSONObject cdObj = elemObj.getJSONObject("CD");

                String title = cdObj.getString("TITLE");
                String artist = cdObj.getString("ARTIST");
                String country = cdObj.getString("COUNTRY");
                String company = cdObj.getString("COMPANY");
                float price = cdObj.getFloat("PRICE");
                int year = cdObj.getInt("YEAR");

                cds.add(new CD(title, artist, country, company, price, year));
            }
        }

        return cds;
    }
}
