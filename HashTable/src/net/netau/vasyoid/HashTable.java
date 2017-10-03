package net.netau.vasyoid;

/**
 * HashTable implemented using doubly linked list.
 * Supports automatic expansion.
 */
public class HashTable {

    private LinkedList[] data;
    private int size, capacity;

    /**
     * Constructor.
     * Creates 8 lists for data storage.
     */
    public HashTable() {
        capacity = 8;
        clear();
    }

    private void rebuild() {
        capacity *= 2;
        LinkedList[] oldData = data;
        clear();
        for (LinkedList list : oldData) {
            while (!list.empty()) {
                String key = list.firstKey();
                String value = list.remove(key);
                put(key, value);
            }
        }
    }

    private LinkedList getList(String key) {
        return data[Math.abs(key.hashCode()) % capacity];
    }

    /**
     * Table size.
     * @return number of elements in the table.
     */
    public int size() {
        return size;
    }

    /**
     * Checks whether a key is stored in the table.
     * @param key key to check.
     * @return true if key is stored, false otherwise.
     */
    public boolean contains(String key) {
        return getList(key).contains(key);
    }

    /**
     * Returns a value by a key.
     * @param key - key of the value.
     * @return if the key exists, its value is returned, null otherwise.
     */
    public String get(String key) {
        return getList(key).get(key);
    }

    /**
     * Adds an element to the table or changes its value.
     * Rebuilds the table is its size is too big.
     * @param key key of the element.
     * @param value value of the element.
     * @return if the element already exists, its previous value is returned, null otherwise.
     */
    public String put(String key, String value) {
        if (size > capacity) {
            rebuild();
        }
        if (!contains(key)) {
            size++;
        }
        return getList(key).put(key, value);
    }

    /**
     * Removes an element from the table.
     * @param key key to remove.
     * @return if the element exists, its value is returned, null otherwise.
     */
    public String remove(String key) {
        if (contains(key)) {
            size--;
        }
        return getList(key).remove(key);
    }

    /**
     * Removes all elements from the table.
     */
    public void clear() {
        data = new LinkedList[capacity];
        for (int i = 0; i < data.length; i++) {
            data[i] = new LinkedList();
        }
        size = 0;
    }
}
