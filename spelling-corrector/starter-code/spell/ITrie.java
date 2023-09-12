package spell;

/**
 * Note: Create your Trie class based on this interface.
 * 
 * You can add to the interface, but do not you alter or remove anything.
 */
public interface ITrie {

	/**
	 * Adds the specified word to the trie (if necessary) and increments the word's
	 * frequency count.
	 *
	 * @param word the word being added to the trie
	 */
	void add(String word);

	/**
	 * Searches the trie for the specified word.
	 * 
	 * @param word the word being searched for.
	 * 
	 * @return a reference to the trie node that represents the word,
	 *         or null if the word is not in the trie
	 */
	INode find(String word);

	/**
	 * Returns the number of unique words in the trie.
	 * 
	 * @return the number of unique words in the trie
	 */
	int getWordCount();

	/**
	 * Returns the number of nodes in the trie.
	 * 
	 * @return the number of nodes in the trie
	 */
	int getNodeCount();

	/**
	 * The toString specification is as follows:
	 * For each word, in alphabetical order:
	 * <word>\n
	 * MUST BE RECURSIVE.
	 */
	@Override
	String toString();

	/**
	 * Returns the hashcode of this trie.
	 * MUST be constant time.
	 * 
	 * @return a uniform, deterministic identifier for this trie.
	 */
	@Override
	int hashCode();

	/**
	 * Checks if an object is equal to this trie.
	 * MUST be recursive.
	 * 
	 * @param o Object to be compared against this trie
	 * @return true if o is a Trie with same structure and node count for each node
	 *         false otherwise
	 */
	@Override
	boolean equals(Object o);
}
