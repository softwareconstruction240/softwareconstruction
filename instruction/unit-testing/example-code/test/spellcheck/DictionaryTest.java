
package spellcheck;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class DictionaryTest {

    private Dictionary dict;

    @BeforeEach
    public void setUp() throws IOException {
        dict = new Dictionary("dict.txt");
    }

    @Test
    public void testValidWords() {
        Assertions.assertTrue(dict.isValidWord("computer"));
        Assertions.assertTrue(dict.isValidWord("programming"));
        Assertions.assertTrue(dict.isValidWord("is"));
        Assertions.assertTrue(dict.isValidWord("fun"));
    }

    @Test
    public void testInvalidWords() {
        Assertions.assertFalse(dict.isValidWord(""));
        Assertions.assertFalse(dict.isValidWord("google"));
        Assertions.assertFalse(dict.isValidWord("gonculator"));
        Assertions.assertFalse(dict.isValidWord("webmaster"));
    }
}