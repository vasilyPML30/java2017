package net.netau.vasyoid;

import org.junit.Test;

import static org.junit.Assert.*;

public class MyStackTest {

    @Test
    public void isEmptyTest() {
        MyStack<Integer> stack = new MyStack<>();
        assertTrue(stack.isEmpty());
        stack.push(1);
        assertFalse(stack.isEmpty());
        stack.pop();
        assertTrue(stack.isEmpty());
    }

    @Test
    public void peekTest() {
        MyStack<Integer> stack = new MyStack<>();
        stack.push(42);
        assertEquals((Integer) 42, stack.peek());
        stack.push(52);
        assertEquals((Integer) 52, stack.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void peekThrowsTest() {
        MyStack<Integer> stack = new MyStack<>();
        stack.peek();
    }

    @Test
    public void popTest() {
        MyStack<Integer> stack = new MyStack<>();
        stack.push(42);
        stack.push(52);
        assertEquals((Integer) 52, stack.pop());
        assertFalse(stack.isEmpty());
        assertEquals((Integer) 42, stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void popThrowsTest() {
        MyStack<Integer> stack = new MyStack<>();
        stack.pop();
    }

    @Test
    public void pushTest() {
        MyStack<Integer> stack = new MyStack<>();
        stack.push(42);
        assertFalse(stack.isEmpty());
        assertEquals((Integer) 42, stack.peek());
    }

}