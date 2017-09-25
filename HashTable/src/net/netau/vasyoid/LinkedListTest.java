package net.netau.vasyoid;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class implements tests for LinkedList class.
 * All public methods are tested.
 */
class LinkedListTest {

    /**
     * Tests LinkedList.size() method.
     * Cases tested: empty list, added one element, two elements.
     */
    @Test
    void testFirstKey() {
        LinkedList list = new LinkedList();
        assertNull(list.firstKey());
        list.put("key", "value");
        assertEquals("key", list.firstKey());
        list.put("key2", "value");
        assertEquals("key2", list.firstKey());
    }

    /**
     * Tests LinkedList.empty() method.
     * Cases tested: empty list, not empty list.
     */
    @Test
    void testEmpty() {
        LinkedList list = new LinkedList();
        assertTrue(list.empty());
        list.put("key", "value");
        assertFalse(list.empty());
    }

    /**
     * Tests LinkedList.contains() method.
     * Cases tested: not existing key, existing key.
     */
    @Test
    void testContains() {
        LinkedList list = new LinkedList();
        assertFalse(list.contains("key"));
        list.put("key", "value");
        assertTrue(list.contains("key"));
    }

    /**
     * Tests LinkedList.get() method.
     * Cases tested: not existing key, existing key.
     */
    @Test
    void testGet() {
        LinkedList list = new LinkedList();
        assertNull(list.get("key"));
        list.put("key", "value");
        assertEquals("value", list.get("key"));
    }

    /**
     * Tests LinkedList.put() method.
     * Cases tested: new key, existing key.
     */
    @Test
    void testPut() {
        LinkedList list = new LinkedList();
        assertNull(list.put("key", "value"));
        assertTrue(list.contains("key"));
        assertEquals("value", list.get("key"));
        assertEquals("value", list.put("key", "value2"));
    }

    /**
     * Tests LinkedList.remove() method.
     * Cases tested: not existing key, existing key, previously removed key.
     */
    @Test
    void testRemove() {
        LinkedList list = new LinkedList();
        list.put("key", "value");
        assertNull(list.remove("key2"));
        assertEquals("value", list.remove("key"));
        assertFalse(list.contains("key"));
        assertNull(list.remove("key"));
    }
}