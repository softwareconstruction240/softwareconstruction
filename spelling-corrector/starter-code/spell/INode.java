package spell;

/**
 * DO NOT MODIFY THIS FILE!
 * Your trie node class should implement the INode interface.
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