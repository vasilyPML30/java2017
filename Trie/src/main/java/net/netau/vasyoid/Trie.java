package net.netau.vasyoid;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.IOException;
import java.util.HashMap;

/**
 * Trie of strings implemented using HashTable of child nodes.
 * Supports serialization/deserialization.
 */
public class Trie implements Serializable {

    private TrieNode root;

    /**
     * Constructor.
     * Initialises root with void node.
     */
    public Trie() {
        root = new TrieNode(null);
        root.startsWithPrefix = 0;
    }

    /**
     * Counts how many strings are stored in the trie.
     * @return number of stored strings.
     */
    public int size() {
        return root.startsWithPrefix;
    }

    private TrieNode findEnd(String element) {
        TrieNode currentNode = root;
        for (char symbol : element.toCharArray()) {
            if (!currentNode.next.containsKey(symbol)) {
                return null;
            }
            currentNode = currentNode.next.get(symbol);
        }
        return currentNode;
    }

    /**
     * Checks if the trie contains the passed element.
     * @param element element to check.
     * @return true if the element is stored, false otherwise.
     */
    public boolean contains(String element) {
        TrieNode endNode = findEnd(element);
        return endNode != null && endNode.isTerminal;
    }

    /**
     * Add an element into the trie.
     * @param element element to add
     * @return false if the element already exists, true otherwise.
     */
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }
        TrieNode currentNode = root;
        for (char symbol : element.toCharArray()) {
            currentNode.startsWithPrefix++;
            if (!currentNode.next.containsKey(symbol)) {
                currentNode.next.put(symbol, new TrieNode(currentNode));
            }
            currentNode = currentNode.next.get(symbol);
        }
        currentNode.isTerminal = true;
        return true;
    }

    private void remove(TrieNode currentNode) {
        currentNode.isTerminal = false;
        while (currentNode != null) {
            currentNode.startsWithPrefix--;
            if (currentNode.startsWithPrefix == 0) {
                currentNode.next.clear();
            }
            currentNode = currentNode.previous;
        }
    }

    /**
     * Remove an element from the trie.
     * @param element element to remove
     * @return false if the element does not exist, true otherwise.
     */
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }
        remove(findEnd(element));
        return true;
    }

    /**
     * Counts how many strings in the trie start with the passed prefix.
     * @param prefix prefix to check.
     * @return number of string with the prefix.
     */
    public int howManyStartsWithPrefix(String prefix) {
        TrieNode endNode = findEnd(prefix);
        return (endNode == null ? 0 : endNode.startsWithPrefix);
    }

    /**
     * Converts the trie into byte array and outputs it to the passed stream.
     * @param out output stream for the bytes.
     * @throws IOException
     */
    public void serialize(OutputStream out) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(out);
        outputStream.writeObject(this);
        outputStream.close();
    }

    /**
     * Replaces the trie with the one read from the passed stream.
     * @param in input stream to read the new trie from.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void deserialize(InputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(in);
        root = ((Trie)(inputStream.readObject())).root;
        inputStream.close();
    }


    private static class TrieNode implements Serializable {
        private TrieNode previous;
        private HashMap<Character, TrieNode> next;
        private boolean isTerminal;
        private int startsWithPrefix;

        private TrieNode(TrieNode previous) {
            this.previous = previous;
            next = new HashMap<>();
            startsWithPrefix = 1;
        }

    }
}
