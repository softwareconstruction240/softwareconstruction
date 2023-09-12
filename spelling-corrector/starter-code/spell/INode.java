package spell;

/**
 * Note: Create your Trie Node class based on this interface.
 * 
 * You can add to the interface, but do not you alter or remove anything.
 */
public interface INode {

    /**
     * Returns the frequency count for the word represented by the node.
     *
     * @return the frequency count for the word represented by the node.
     */
    int getValue();

    /**
     * Increments the frequency count for the word represented by the node.
     */
    void incrementValue();

    /**
     * Returns the child nodes of this node.
     *
     * @return the child nodes.
     */
    INode[] getChildren();
}