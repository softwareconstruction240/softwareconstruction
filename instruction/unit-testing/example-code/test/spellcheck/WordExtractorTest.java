
package spellcheck;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class WordExtractorTest {

    private WordExtractor extractor;
    private List<String> expected;

    @BeforeEach
    public void setUp() {
        extractor = new WordExtractor();
        expected = new ArrayList<>();
    }

    @Test
    public void testEmptyContent() {
        String content = "";
        List<String> actual = extractor.extract(content);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testOneShortWord() {
        String content = "a";

        expected.add("a");

        List<String> actual = extractor.extract(content);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testOneLongWord() {
        String content = "thisisareallylongword";

        expected.add("thisisareallylongword");

        List<String> actual = extractor.extract(content);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testOneShortWordWithSurroundingWhitespace() {
        String content = " \t\n\ra \t\n\r";

        expected.add("a");

        List<String> actual = extractor.extract(content);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testOneLongWordWithSurroundingWhitespace() {
        String content = " \t\n\rthisisareallylongword \t\n\r";

        expected.add("thisisareallylongword");

        List<String> actual = extractor.extract(content);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testTypicalContent() {
        String content = "\nfour score\nand seven\n\nyears ago\nour fathers\n";

        expected.add("four");
        expected.add("score");
        expected.add("and");
        expected.add("seven");
        expected.add("years");
        expected.add("ago");
        expected.add("our");
        expected.add("fathers");

        List<String> actual = extractor.extract(content);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testNonAlphaCharacters() {
        String content = "\n4 ??score\nand*** -- 7\n\nyears ago<><>\nour fathers\n";

        expected.add("score");
        expected.add("and");
        expected.add("years");
        expected.add("ago");
        expected.add("our");
        expected.add("fathers");

        List<String> actual = extractor.extract(content);
        Assertions.assertEquals(expected, actual);
    }
}