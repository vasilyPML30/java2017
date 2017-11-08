package net.netau.vasyoid;

import static org.junit.Assert.*;
import org.junit.Test;


public class Function1Test {

    @Test
    public void composeIntegerTest() {
        Function1<Integer, Integer> f = x -> x + 5;
        Function1<Integer, Integer> g = x -> x * 5;
        assertEquals((Integer)((2 + 5) * 5), f.compose(g).apply(2));
        assertEquals((Integer)((2 * 5) + 5), g.compose(f).apply(2));
    }

    @Test
    public void composeIntegerStringTest() {
        Function1<Integer, String> f = String::valueOf;
        Function1<String, String> g = x -> "Number: " + x;
        assertEquals("Number: 123", f.compose(g).apply(123));
    }
}
