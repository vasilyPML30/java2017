package net.netau.vasyoid;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MyStackTest {

    private MyStack<Integer> stack;

    @Before
    public void init() {
        stack = new MyStack<>();
    }

    @Test
    public void isEmptyTest() {
        assertTrue(stack.isEmpty());
        stack.push(1);
        assertFalse(stack.isEmpty());
        stack.pop();
        assertTrue(stack.isEmpty());
    }

    @Test
    public void peekTest() {
        stack.push(42);
        assertEquals((Integer) 42, stack.peek());
        stack.push(52);
        assertEquals((Integer) 52, stack.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void peekThrowsTest() {
        stack.peek();
    }

    @Test
    public void popTest() {
        stack.push(42);
        stack.push(52);
        assertEquals((Integer) 52, stack.pop());
        assertFalse(stack.isEmpty());
        assertEquals((Integer) 42, stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void popThrowsTest() {
        stack.pop();
    }

    @Test
    public void pushTest() {
        stack.push(42);
        assertFalse(stack.isEmpty());
        assertEquals((Integer) 42, stack.peek());
    }

}