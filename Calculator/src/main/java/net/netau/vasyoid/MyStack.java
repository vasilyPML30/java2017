package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Stack storing @NotNull generic objects.
 * @param <T> objects' type.
 */
public class MyStack<T> {

    private Node<T> head;

    /**
     * Checks whether the stack is empty.
     * @return true if the stack is empty, false otherwise.
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Looks up the top element.
     * @return the value of the top element.
     */
    @NotNull
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        } else {
            return head.value;
        }
    }

    /**
     * Removes the top element.
     * @return the value of the removed element.
     */
    @NotNull
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        } else {
            T result = peek();
            head = head.next;
            return result;
        }
    }

    /**
     * Puts a new element on top of the stack.
     * @param element value of the element to add.
     */
    public void push(@NotNull T element) {
        head = new Node<>(head, element);
    }

    private static class Node<T> {
        private Node<T> next;
        private T value;

        Node(@Nullable Node<T> next, @NotNull T value) {
            this.value = value;
            this.next = next;
        }
    }
}
