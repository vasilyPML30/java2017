package net.netau.vasyoid;

public class HashTable {
    private LinkedList[] data;
    private int size, capacity;

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
        return data[key.hashCode() % capacity];
    }

    public int size() {
        return size;
    }

    public boolean contains(String key) {
        return getList(key).contains(key);
    }

    public String get(String key) {
        return getList(key).get(key);
    }

    public String put(String key, String value) {
        if (size > capacity) {
            rebuild();
        }
        if (!contains(key)) {
            size++;
        }
        return getList(key).put(key, value);
    }

    public String remove(String key) {
        if (contains(key)) {
            size--;
        }
        return getList(key).remove(key);
    }

    public void clear() {
        data = new LinkedList[capacity];
        for (int i = 0; i < data.length; i++) {
            data[i] = new LinkedList();
        }
        size = 0;
    }
}
