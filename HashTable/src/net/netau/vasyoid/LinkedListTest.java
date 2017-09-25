package net.netau.vasyoid;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LinkedListTest {

    @Test
    void testFirstKey() {
        LinkedList list = new LinkedList();
        assertNull(list.firstKey());
        list.put("key", "value");
        assertEquals("key", list.firstKey());
        list.put("key2", "value");
        assertEquals("key2", list.firstKey());
    }

    @Test
    void testEmpty() {
        LinkedList list = new LinkedList();
        assertTrue(list.empty());
        list.put("key", "value");
        assertFalse(list.empty());
    }

    @Test
    void testContains() {
        LinkedList list = new LinkedList();
        assertFalse(list.contains("key"));
        list.put("key", "value");
        assertTrue(list.contains("key"));
    }

    @Test
    void testGet() {
        LinkedList list = new LinkedList();
        assertNull(list.get("key"));
        list.put("key", "value");
        assertEquals("value", list.get("key"));
    }

    @Test
    void testPut() {
        LinkedList list = new LinkedList();
        assertNull(list.put("key", "value"));
        assertTrue(list.contains("key"));
        assertEquals("value", list.get("key"));
        assertEquals("value", list.put("key", "value2"));
    }

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