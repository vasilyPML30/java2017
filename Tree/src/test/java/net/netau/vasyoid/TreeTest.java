package net.netau.vasyoid;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests for Tree class.
 */
public class TreeTest {

    /**
     * Checks that empty tree size is zero.
     */
    @Test
    public void testEmptySize() {
        assertEquals(0, new Tree().size());
    }

    /**
     * Tests Tree.add() method by adding different values.
     */
    @Test
    public void testAddDifferent() {
        int[] values = new int[]{10, 5, 3, 7, 8, 2, 1, 9, 4, 6};
        Tree<Integer> tree = new Tree<>();
        for (int i = 0; i < values.length; i++) {
            assertTrue(tree.add(values[i]));
            assertEquals(i + 1, tree.size());
        }
    }

    /**
     * Tests Tree.add() method by adding repetitive values.
     */
    @Test
    public void testAddEqual() {
        int[] values = new int[]{5, 4, 6};
        Tree<Integer> tree = new Tree<>();
        for (int i = 0; i < values.length; i++) {
            tree.add(values[i]);
            assertEquals(i + 1, tree.size());
        }
        for (int value : values) {
            assertFalse(tree.add(value));
            assertEquals(values.length, tree.size());
        }

    }

    /**
     * Tests Tree.contains() method by checking existing and non-existing elements.
     */
    @Test
    public void testContains() {
        Tree<Integer> tree = new Tree<>();
        assertFalse(tree.contains(42));
        tree.add(42);
        assertTrue(tree.contains(42));
    }
}
