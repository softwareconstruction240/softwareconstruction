
package spellcheck;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;

public class URLFetcherTest {

    private URLFetcher fetcher;

    @BeforeEach
    public void setUp() {
        fetcher = new URLFetcher();
    }

    @Test
    public void testValidURL()
            throws IOException {

        URL validURL = new URL("https://faculty.cs.byu.edu/~rodham/unit-testing-demo/DataStructureExample.txt");
        String actual = fetcher.fetch(validURL);

        String expected = readFileAsString("DataStructureExample.txt");

        Assertions.assertEquals(expected, actual);
    }

    private String readFileAsString(String fileName) throws IOException {

        StringBuilder builder = new StringBuilder();

        try (FileInputStream input = new FileInputStream(fileName)) {
            int c;
            while ((c = input.read()) >= 0) {
                builder.append((char) c);
            }
        }

        return builder.toString();
    }

    @Test
    public void testInvalidURL() throws IOException {
        URL invalidURL = new URL("http://www.adflkjhadj.com/fkls.html");
        Assertions.assertThrows(UnknownHostException.class, () -> fetcher.fetch(invalidURL));
    }
}