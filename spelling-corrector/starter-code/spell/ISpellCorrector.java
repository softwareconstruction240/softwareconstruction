package spell;

import java.io.IOException;

/**
 * Note: Create your SpellCorrector class based on this interface.
 * 
 * You can add to the interface, but do not you alter or remove anything.
 */
public interface ISpellCorrector {

	/**
	 * Tells this <code>SpellCorrector</code> to use the given file as its
	 * dictionary
	 * for generating suggestions.
	 * 
	 * @pre SpellCorrector will have had empty-param constructor called, but
	 *      dictionary has nothing in it.
	 * @param dictionaryFileName the file containing the words to be used
	 * @throws IOException If the file cannot be read
	 * @post SpellCorrector will have dictionary filled and be ready to
	 *       suggestSimilarWord any number of times.
	 */
	void useDictionary(String dictionaryFileName) throws IOException;

	/**
	 * Suggest a word from the dictionary that most closely matches
	 * <code>inputWord</code>.
	 * 
	 * @param inputWord the word we are trying to find or find a suggestion for
	 * @return the suggestion or null if there is no similar word in the dictionary
	 */
	String suggestSimilarWord(String inputWord);
}
