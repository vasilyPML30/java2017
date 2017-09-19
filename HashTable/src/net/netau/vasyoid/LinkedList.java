package net.netau.vasyoid;

/**
 * Doubly linked list implemented using links.
 * Stores pairs of keys and values.
 */

public class LinkedList {

    private ListNode head;

    private class ListNode {
        private ListNode previous, next;
        private String key, value;

        private ListNode(String key, String value) {
            this.key = key;
            this.value = value;
            this.previous = this.next = this;
        }

    }

    private ListNode findNode(String key) {
        for (ListNode node = head.next; node.key != null; node = node.next) {
            if (key.equals(node.key)) {
                return node;
            }
        }
        return null;
    }

    private void putNode(ListNode node) {
        head.next.previous = node;
        node.next = head.next;
        head.next = node;
        node.previous = head;
    }

    private void removeNode(ListNode node) {
        node.previous.next = node.next;
        node.next.previous = node.previous;
    }

    /**
     * Constructor.
     * Creates a fake 'head' node.
     */
    public LinkedList() {
        head = new ListNode(null, null);
    }

    /**
     * Gets the key of the first node in the list.
     * @return key, if the list is not empty, null otherwise.
     */
    public String firstKey() {
        return head.next.key;
    }

    /**
     * Checks whether the list is empty.
     * @return true if the list is empty, false otherwise.
     */
    public boolean empty() {
        return head.next == head;
    }

    /**
     * Checks whether a key is stored in the list.
     * @param key key to check.
     * @return true if key is stored, false otherwise.
     */
    public boolean contains(String key) {
        return findNode(key) != null;
    }

    /**
     * Returns a value by a key.
     * @param key - key of the value.
     * @return if the key exists, its value is returned, null otherwise.
     */
    public String get(String key) {
        ListNode node = findNode(key);
        return (node != null ? node.key : null);
    }

    /**
     * Adds an element to the list or changes its value.
     * @param key key of the element.
     * @param value value of the element.
     * @return if the element already exists, its previous value is returned, null otherwise.
     */
    public String put(String key, String value) {
        ListNode node = findNode(key);
        if (node != null) {
            String nodeValue = node.value;
            node.value = value;
            return nodeValue;
        } else {
            putNode(new ListNode(key, value));
            return null;
        }
    }

    /**
     * Removes an element from the list.
     * @param key key to remove.
     * @return if the element exists, its value is returned, null otherwise.
     */
    public String remove(String key) {
        ListNode node = findNode(key);
        if (node != null) {
            String nodeValue = node.value;
            removeNode(node);
            return nodeValue;
        } else {
            return null;
        }
    }
}
