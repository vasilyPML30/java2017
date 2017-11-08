package net.netau.vasyoid;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class CollectionsTest {

    @Test
    public void mapTest() {
        Function1<Integer, String> f = String::valueOf;
        List<Integer> list = new LinkedList<>();
        List<String> result = new LinkedList<>();
        for (int i = 10; i < 30; i++) {
            list.add(i);
            result.add(String.valueOf(i));
        }
        assertEquals(result, Collections.map(f, list));
    }

    @Test
    public void mapEmptyTest() {
        Function1<Integer, String> f = String::valueOf;
        List<Integer> list = new LinkedList<>();
        assertTrue(Collections.map(f, list).isEmpty());
    }

    @Test
    public void filterTest() {
        Predicate<Integer> f = x -> x % 3 == 0;
        Predicate<Integer> g = x -> x > 0;
        List<Integer> list = new LinkedList<>();
        List<Integer> result = new LinkedList<>();
        for (int i = 10; i < 30; i++) {
            list.add(i);
            if (i % 3 == 0) {
                result.add(i);
            }

        }
        assertEquals(result, Collections.filter(f, list));
        assertEquals(list, Collections.filter(g, list));
    }

    @Test
    public void filterEmptyTest() {
        Predicate<Integer> f = x -> x % 3 == 0;
        List<Integer> list = new LinkedList<>();
        assertTrue(Collections.filter(f, list).isEmpty());
    }

    @Test
    public void takeWhileTest() {
        Predicate<Integer> f = x -> x < 20;
        Predicate<Integer> g = x -> x > 0;
        List<Integer> list = new LinkedList<>();
        List<Integer> result = new LinkedList<>();
        for (int i = 10; i < 30; i++) {
            list.add(i);
            if (i < 20) {
                result.add(i);
            }

        }
        assertEquals(result, Collections.takeWhile(f, list));
        assertEquals(list, Collections.takeWhile(g, list));
    }

    @Test
    public void takeWhileEmptyTest() {
        Predicate<Integer> f = x -> x % 3 == 0;
        List<Integer> list = new LinkedList<>();
        assertTrue(Collections.takeWhile(f, list).isEmpty());
    }

    @Test
    public void takeUnlessTest() {
        Predicate<Integer> f = x -> x >= 20;
        Predicate<Integer> g = x -> x < 0;
        List<Integer> list = new LinkedList<>();
        List<Integer> result = new LinkedList<>();
        for (int i = 10; i < 30; i++) {
            list.add(i);
            if (i < 20) {
                result.add(i);
            }

        }
        assertEquals(result, Collections.takeUnless(f, list));
        assertEquals(list, Collections.takeUnless(g, list));
    }

    @Test
    public void takeUnlessEmptyTest() {
        Predicate<Integer> f = x -> x % 3 == 0;
        List<Integer> list = new LinkedList<>();
        assertTrue(Collections.takeUnless(f, list).isEmpty());
    }

    @Test
    public void foldlIntegerTest() {
        Function2<Integer, Integer, Integer> f = (x, y) -> x - y;
        List<Integer> list = new LinkedList<>();
        for (int i = 5; i > 0; i--) {
            list.add(i);
        }
        assertEquals((Integer)0, Collections.foldl(f, 15, list));
    }

    @Test
    public void foldlStringTest() {
        Function2<String, Integer, String> f = (x, y) -> x + String.valueOf(y);
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        assertEquals("str: 0123456789", Collections.foldl(f, "str: ", list));
    }

    @Test
    public void foldrIntegerTest() {
        Function2<Integer, Integer, Integer> f = (x, y) -> y - x;
        List<Integer> list = new LinkedList<>();
        for (int i = 5; i > 0; i--) {
            list.add(i);
        }
        assertEquals((Integer)0, Collections.foldr(f, 15, list));
    }

    @Test
    public void foldrStringTest() {
        Function2<Integer, String, String> f = (x, y) -> String.valueOf(x) + y;
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        assertEquals("0123456789: str", Collections.foldr(f, ": str", list));
    }
}
