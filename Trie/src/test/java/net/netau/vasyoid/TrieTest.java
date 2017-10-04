package net.netau.vasyoid;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * This class implements tests for Trie class.
 * All public methods are tested.
 */
public class TrieTest {

    /**
     * Tests Trie.add() method.
     */
    @Test
    public void testAdd() {
        Trie trie = new Trie();
        assertTrue(trie.add("first"));
        assertTrue(trie.add("fir"));
        assertTrue(trie.add("sec"));
        assertTrue(trie.add("second"));
        assertTrue(trie.add("secret"));
        assertFalse(trie.add("first"));
        assertFalse(trie.add("fir"));
    }

    /**
     * Tests Trie.contains() method.
     * Cases tested: empty string, nonexistent element, existent element, removed element.
     */
    @Test
    public void testContains() {
        Trie trie = new Trie();
        assertFalse(trie.contains("first"));
        assertFalse(trie.contains("second"));
        assertFalse(trie.contains(""));
        trie.add("");
        trie.add("first");
        trie.add("fir");
        trie.add("sec");
        trie.add("second");
        trie.add("secret");
        assertTrue(trie.contains(""));
        assertTrue(trie.contains("first"));
        assertTrue(trie.contains("fir"));
        assertTrue(trie.contains("sec"));
        assertTrue(trie.contains("second"));
        assertTrue(trie.contains("secret"));
        trie.remove("sec");
        assertFalse(trie.contains("sec"));
        trie.remove("second");
        assertFalse(trie.contains("second"));
    }

    /**
     * Tests Trie.remove() method.
     * Cases tested: empty string, nonexistent element, existent element.
     */
    @Test
    public void testRemove() {
        Trie trie = new Trie();
        trie.add("first");
        trie.add("fir");
        trie.add("sec");
        trie.add("second");
        trie.add("secret");
        assertFalse(trie.remove(""));
        assertTrue(trie.remove("first"));
        assertTrue(trie.remove("fir"));
        assertTrue(trie.remove("sec"));
        assertTrue(trie.remove("second"));
        assertTrue(trie.remove("secret"));
        assertFalse(trie.remove("first"));
        assertFalse(trie.remove("fir"));
        assertFalse(trie.remove("sec"));
        assertFalse(trie.remove("second"));
        assertFalse(trie.remove("secret"));
    }

    /**
     * Tests Trie.size() method.
     * Cases tested: empty trie, some added elements, removed elements.
     */
    @Test
    public void testSize() {
        Trie trie = new Trie();
        assertEquals(0, trie.size());
        trie.add("first");
        assertEquals(1, trie.size());
        trie.add("fir");
        assertEquals(2, trie.size());
        trie.add("sec");
        assertEquals(3, trie.size());
        trie.add("second");
        assertEquals(4, trie.size());
        trie.add("secret");
        assertEquals(5, trie.size());
        trie.remove("first");
        assertEquals(4, trie.size());
        trie.remove("fir");
        assertEquals(3, trie.size());
        trie.remove("sec");
        assertEquals(2, trie.size());
        trie.remove("second");
        assertEquals(1, trie.size());
        trie.remove("secret");
        assertEquals(0, trie.size());
    }

    /**
     * Tests Trie.howManyStartsWithPrefix() method.
     */
    @Test
    public void testHowManyStartsWithPrefix(){
        Trie trie = new Trie();
        trie.add("first");
        trie.add("fir");
        trie.add("sec");
        trie.add("second");
        trie.add("secret");
        assertEquals(5, trie.howManyStartsWithPrefix(""));
        assertEquals(1, trie.howManyStartsWithPrefix("first"));
        assertEquals(2, trie.howManyStartsWithPrefix("fir"));
        assertEquals(3, trie.howManyStartsWithPrefix("sec"));
        assertEquals(0, trie.howManyStartsWithPrefix("third"));
    }


    /**
     * Tests Trie.serialize() and Trie.deserialize() methods.
     */
    @Test
    public void testSerialize() throws Exception {
        Trie trie = new Trie();
        trie.add("first");
        trie.add("fir");
        trie.add("sec");
        trie.add("second");
        trie.add("secret");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        trie.serialize(out);
        Trie trie2 = new Trie();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        trie2.deserialize(in);
        assertEquals(5, trie2.size());
        assertTrue(trie2.contains("first"));
        assertTrue(trie2.contains("fir"));
        assertTrue(trie2.contains("sec"));
        assertTrue(trie2.contains("second"));
        assertTrue(trie2.contains("secret"));
    }
}
