package net.netau.vasyoid;

public class LinkedList {

    private ListNode head;

    public LinkedList() {
        head = new ListNode(null, null);
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

    public String firstKey() {
        return head.next.key;
    }

    public boolean empty() {
        return head.next == head;
    }

    public boolean contains(String key) {
        return findNode(key) != null;
    }

    public String get(String key) {
        ListNode node = findNode(key);
        return (node != null ? node.key : null);
    }

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

    private class ListNode {
        private ListNode previous, next;
        private String key, value;

        private ListNode(String key, String value) {
            this.key = key;
            this.value = value;
            this.previous = this.next = this;
        }

    }

}
