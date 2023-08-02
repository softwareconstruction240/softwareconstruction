
package spellcheck;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.SortedMap;
import java.util.TreeMap;

public class SpellingCheckerTest {

    private static final String URL_BASE =
            "https://faculty.cs.byu.edu/~rodham/unit-testing-demo/";

    private SpellingChecker checker;

    @BeforeEach
    public void setUp() {
        checker = new SpellingChecker();
    }

    @Test
    public void testOne() throws IOException {
        URL url = new URL(URL_BASE + "one.txt");

        SortedMap<String, Integer> expected = new TreeMap<>();
        expected.put("forrrrr", 2);
        expected.put("scorrrrre", 2);
        expected.put("annnnnd", 1);
        expected.put("sevvvvin", 3);

        SortedMap<String, Integer> actual = checker.check(url);
        Assertions.assertEquals(expected, actual);
    }
}