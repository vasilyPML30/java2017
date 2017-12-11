package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Set based on unbalanced BST.
 * @param <Type> type of stored values.
 */
public class Tree<Type extends Comparable<Type>> {

    private MyNode root = null;
    private int size = 0;

    /**
     * Counts number of stored elements.
     * @return number of elements.
     */
    public int size() {
        return size;
    }

    private @Nullable MyNode lowerBound(@NotNull Type value, @Nullable MyNode node) {
        if (node == null) {
            return null;
        } else if (node.value.compareTo(value) < 0) {
                return lowerBound(value, node.right);
        } else if (node.value.compareTo(value) > 0) {
            MyNode result = lowerBound(value, node.left);
            if (result == null) {
                return node;
            } else {
                return result;
            }
        } else {
            return node;
        }
    }

    /**
     * Checks whether value exists.
     * @param value value to check.
     * @return true if value is stored, false otherwise.
     */
    public boolean contains(Type value) {
        MyNode node = lowerBound(value, root);
        return node != null && node.value.compareTo(value) == 0;
    }

    private void addRightmost(Type value, MyNode node) {
        if (node.right == null) {
            node.right = new MyNode(value);
        } else {
            addRightmost(value, node.right);
        }
    }

    /**
     * Adds an element into set.
     * @param value value to add.
     * @return true if value was added, false if it had already been stored.
     */
    public boolean add(Type value) {
        if (root == null) {
            root = new MyNode(value);
            size = 1;
            return true;
        }
        if (contains(value)) {
            return false;
        }
        MyNode parent = lowerBound(value, root);
        if (parent == null) {
            addRightmost(value, root);
        } else if (parent.left != null) {
            addRightmost(value, parent.left);
        } else {
            parent.left = new MyNode(value);
        }
        size++;
        return true;
    }

    private class MyNode {
        private Type value;
        private MyNode left = null, right = null;

        private MyNode(Type value) {
            this.value = value;
        }
    }
}
