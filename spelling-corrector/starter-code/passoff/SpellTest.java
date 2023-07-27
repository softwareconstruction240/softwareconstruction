package passoff;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spell.ISpellCorrector;
import spell.SpellCorrector;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class SpellTest {

    private static final String WORD_FILENAME = "word.txt";
    private static final String WORDS_FILENAME = "words.txt";
    private static final String BIG_FILENAME = "notsobig.txt";
    private static final String WORD = "yea";
    private ISpellCorrector studentCorrector;

    @BeforeEach
    public void setup(){
        try{
            studentCorrector = new SpellCorrector();
        }
        catch(Throwable t){
            fail(t.getClass() + ". Make sure class name is spell.spell.SpellCorrector.");
        }
    }

    @Test
    @DisplayName("Valid Word Testing")
    public void testValidWord(){
        String suggestedWord = test(WORD_FILENAME, WORD);
        assertEquals(WORD, suggestedWord, "Same spelling of expected word.");

        suggestedWord = test(WORD_FILENAME, WORD.toLowerCase());
        assertEquals(WORD, suggestedWord, "Lower case of expected word.");

        suggestedWord = test(WORD_FILENAME, WORD.toUpperCase());
        assertEquals(WORD, suggestedWord, "Upper case of expected word.");

        suggestedWord = test(WORD_FILENAME, WORD);
        assertEquals(WORD, suggestedWord, "Same spelling of expected word.");
    }

    @Test
    @DisplayName("Edit Distance One: Insertion")
    public void testInsertion(){
        String guess = "ye";
        String suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "ea";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "ya";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));
    }

    @Test
    @DisplayName("Edit Distance One: Deletion")
    public void testDeletion(){
        String guess = "yeaz";
        String suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "yaea";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "ybea";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "ryea";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "ygea";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));
    }

    @Test
    @DisplayName("Edit Distance One: Alteration")
    public void testAlteration(){
        String guess = "flobt";
        String suggestedWord = test(WORDS_FILENAME, guess);
        assertEquals("float", suggestedWord, createErrorMessage(guess, "float", suggestedWord));

        guess = "bloat";
        suggestedWord = test(WORDS_FILENAME, guess);
        assertEquals("float", suggestedWord, createErrorMessage(guess, "float", suggestedWord));

        guess = "reah";
        suggestedWord = test(WORDS_FILENAME, guess);
        assertEquals("yeah", suggestedWord, createErrorMessage(guess, "yeah", suggestedWord));
    }

    @Test
    @DisplayName("Edit Distance One: Transposition")
    public void testTransposition(){
        String guess = "yaeh";
        String suggestedWord = test(WORDS_FILENAME, guess);
        assertEquals("yeah", suggestedWord, createErrorMessage(guess, "yeah", suggestedWord));

        guess = "flaot";
        suggestedWord = test(WORDS_FILENAME, guess);
        assertEquals("float", suggestedWord, createErrorMessage(guess, "float", suggestedWord));
    }

    @Test
    @DisplayName("Edit Distance Two: Insertion then Insertion")
    public void testInsertionInsertion(){
        String guess = "e";
        String suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "a";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "y";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));
    }

    @Test
    @DisplayName("Edit Distance Two: Insertion then Deletion")
    public void testInsertionDeletion(){
        String guess = "yez";
        String suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "efa";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "rya";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));
    }

    @Test
    @DisplayName("Edit Distance Two: Insertion then Alteration")
    public void testInsertionAlteration() {
        String guess = "er";
        String suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "qa";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "yf";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));
    }

    @Test
    @DisplayName("Edit Distance Two: Insertion then Transposition")
    public void testInsertionTransposition() {
        String guess = "ae";
        String suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "ey";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));
    }

    @Test
    @DisplayName("Edit Distance Two: Deletion then Insertion")
    public void testDeletionInsertion() {
        String guess = "yar";
        String suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "fya";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "yad";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));
    }

    @Test
    @DisplayName("Edit Distance Two: Deletion then Deletion")
    public void testDeletionDeletion() {
        String guess = "ydela";
        String suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "yeakg";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "jkyea";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "vyfea";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "cyean";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));
    }

    @Test
    @DisplayName("Edit Distance Two: Deletion then Alteration")
    public void testDeletionAlteration(){
        String guess = "ydef";
        String suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "vyga";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "ymca";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));
    }

    @Test
    @DisplayName("Edit Distance Two: Deletion then Transposition")
    public void testDeletionTransposition(){
        String guess = "yade";
        String suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "epya";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "yame";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));
    }

    @Test
    @DisplayName("Edit Distance Two: Alteration then Insertion")
    public void testAlterationInsertion(){
        String guess = "fe";
        String suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "va";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "yy";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));
    }

    @Test
    @DisplayName("Edit Distance Two: Alteration then Deletion")
    public void testAlterationDeletion(){
        String guess = "feia";
        String suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "yqex";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "yqax";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));
    }

    @Test
    @DisplayName("Edit Distance Two: Alteration then Alteration")
    public void testAlterationAlteration(){
        String guess = "vda";
        String suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "xel";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "yhb";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));
    }

    @Test
    @DisplayName("Edit Distance Two: Alteration then Transposition")
    public void testAlterationTransposition(){
        String guess = "yac";
        String suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "gya";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "eja";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));
    }

    @Test
    @DisplayName("Edit Distance Two: Transposition then Insertion")
    public void testTranspositionInsertion(){
        String guess = "ay";
        String suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "ae";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "ey";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));
    }

    @Test
    @DisplayName("Edit Distance Two: Transposition then Deletion")
    public void testTranspositionDeletion(){
        String guess = "ycae";
        String suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "eyae";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "eyma";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));
    }

    @Test
    @DisplayName("Edit Distance Two: Transposition then Alteration")
    public void testTranspositionAlteration(){
        String guess = "yac";
        String suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "bya";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "yle";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));
    }

    @Test
    @DisplayName("Edit Distance Two: Transposition then Transposition")
    public void testTranspositionTransposition(){
        String guess = "eay";
        String suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));

        guess = "aye";
        suggestedWord = test(WORD_FILENAME, guess);
        assertEquals(WORD, suggestedWord, createErrorMessage(guess, WORD, suggestedWord));
    }

    @Test
    @DisplayName("No Similar Words")
    public void testDNoSimilarWords(){
        String guess = "";
        String suggestedWord = test(WORD_FILENAME, guess);
        assertNull(suggestedWord, "Guessed empty string.");

        guess = "lol";
        suggestedWord = test(WORD_FILENAME, guess);
        assertNull(suggestedWord, "Guessed dissimilar string of same length");

        guess = "abcdefghijklmnopqrstuvwxyz";
        suggestedWord = test(WORD_FILENAME, guess);
        assertNull(suggestedWord, "Guessed dissimilar string of much longer length");
    }

    @Test
    @DisplayName("Choose Closest Word")
    public void testChooseClosestWord(){
        String suggestedWord = test(WORDS_FILENAME, "ye");
        assertEquals("yea", suggestedWord, "Choosing edit distance one before two");

        suggestedWord = test(WORDS_FILENAME, "yes");
        assertEquals("yea", suggestedWord, "Choosing edit distance one before two");

        suggestedWord = test(WORDS_FILENAME, "yeaz");
        assertEquals("yeah", suggestedWord, "Choosing word with higher frequency");

        suggestedWord = test(WORDS_FILENAME, "yeahj");
        assertEquals("yeah", suggestedWord, "Choosing first word alphabetically when equal frequency");
    }

    @Test
    @DisplayName("Big File")
    public void testBigFile(){
        String guess = "Jason";
        String suggestedWord = test(BIG_FILENAME, guess);
        assertEquals("jason", suggestedWord, createErrorMessage(guess, "jason", suggestedWord));

        guess = "is";
        suggestedWord = test(BIG_FILENAME, guess);
        assertEquals("is", suggestedWord, createErrorMessage(guess, "is", suggestedWord));

        guess = "zupem";
        suggestedWord = test(BIG_FILENAME, guess);
        assertEquals("super", suggestedWord, createErrorMessage(guess, "super", suggestedWord));

        guess = "cooool";
        suggestedWord = test(BIG_FILENAME, guess);
        assertEquals("cool", suggestedWord, createErrorMessage(guess, "cool", suggestedWord));

        guess = "absolustly";
        suggestedWord = test(BIG_FILENAME, guess);
        assertEquals("absolutely", suggestedWord, createErrorMessage(guess, "absolutely", suggestedWord));
    }

    private String test(String filename, String word) {
        String suggestion = null;
        try {
            studentCorrector.useDictionary(filename);
        } catch (IOException e) {
            fail("Student spell corrector could not load dictionary. It threw: " + e.toString());
        }
        try {
            suggestion = studentCorrector.suggestSimilarWord(word);
        } catch (Throwable t) {
            fail("Student code threw: " + t.getMessage());
        }
        return suggestion;
    }

    private String createErrorMessage(String guess, String expected, String suggested){
        return String.format("Guessed: %s Expected: %s Actual: %s", guess, expected, suggested);
    }

}
