package net.netau.vasyoid;

import java.util.*;

public class LinkedHashMap<K, V> extends AbstractMap<K, V> {

    private int size = 0;
    private LinkedList<MyNode>[] data;
    private MyNode first = null, last = null;
    private static final int DATA_SIZE = 1000;

    public LinkedHashMap() {
        data = new LinkedList[DATA_SIZE];
        for (int i = 0; i < data.length; i++) {
            data[i] = new LinkedList<>();
        }
    }

    @Override
    public V put(K key, V value) {
        for (MyNode node : data[key.hashCode()]) {
            if (node.getKey().equals(key)) {
                V prev = node.getValue();
                node.setValue(value);
                return prev;
            }
        }
        if (last != null) {
            last.next = new MyNode(key, value);
            last = last.next;
        } else {
            first = last = new MyNode(key, value);
        }
        data[key.hashCode()].add(last);
        return null;
    }

    @Override
    public V get(Object key) {
        for (MyNode node : data[key.hashCode()]) {
            if (node.getKey().equals(key)) {
                return node.getValue();
            }
        }
        return null;
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public Set<Map.Entry<K,V>> entrySet() {
        return new MySet();
    }

    public class MyNode implements Map.Entry<K, V> {

        MyNode next = null;
        private K key;
        private V value;

        MyNode(K key, V value) {
            this.key = key;
            this.value = value;
        }

        MyNode getNext() {
            return next;
        }

        public void setNext(MyNode next) {
            this.next = next;
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            return hashCode() == other.hashCode();
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V tmp = this.value;
            this.value = value;
            return tmp;
        }

    }

    private class MySet extends AbstractSet<Map.Entry<K, V>> {

        @Override
        public int size() {
            return size;
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new MyIterator();
        }

        private class MyIterator implements Iterator<Map.Entry<K, V>> {
            MyNode currentNode;

            public MyIterator() {
                currentNode = first;
            }

            @Override
            public boolean hasNext() {
                return currentNode != null && currentNode.getNext() != null;
            }

            @Override
            public Map.Entry<K, V> next() {
                if (currentNode == null) {
                    return null;
                }
                MyNode nextNode = currentNode.getNext();
                currentNode = nextNode;
                return currentNode;
            }

        }
    }

}
