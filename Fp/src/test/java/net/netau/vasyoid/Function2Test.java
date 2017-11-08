package net.netau.vasyoid;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests for Function2 interface.
 */
public class Function2Test {

    @Test
    public void composeIntegerIntegerTest() {
        Function2<Integer, Integer, Integer> f = (x, y) -> x + y;
        Function1<Integer, Integer> g = (x -> x * 5);
        assertEquals((Integer)((2 + 5) * 5), f.compose(g).apply(2, 5));
    }

    @Test
    public void composeIntegerStringTest() {
        Function2<Integer, Integer, String> f = (x, y) -> String.valueOf(x + y);
        Function1<String, String> g = (x -> "Result: " + x);
        assertEquals("Result: 9", f.compose(g).apply(4, 5));
    }

    @Test
    public void bind1Test() {
        Function2<Integer, Integer, Integer> f = (x, y) -> x - y;
        assertEquals((Integer)(10 - 3), f.bind1(10).apply(3));
    }

    @Test
    public void bind2Test() {
        Function2<Integer, Integer, Integer> f = (x, y) -> x - y;
        assertEquals((Integer)(10 - 3), f.bind2(3).apply(10));
    }

    @Test
    public void curryTest() {
        Function2<Integer, Integer, Integer> f = (x, y) -> x - y;
        assertEquals((Integer)(10 - 3), f.curry().apply(3).apply(10));
    }

    @Test
    public void flipIntegerTest() {
        Function2<Integer, Integer, Integer> f = (x, y) -> x - y;
        assertEquals((Integer)(10 - 3), f.flip().apply(3, 10));
    }

    @Test
    public void flipIntegerStringTest() {
        Function2<Integer, String, String> f = (x, y) -> y + String.valueOf(x);
        assertEquals("Number: 30", f.flip().apply("Number: ", 30));
    }
}
