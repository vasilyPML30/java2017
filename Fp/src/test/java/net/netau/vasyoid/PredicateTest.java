package net.netau.vasyoid;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Random;

public class PredicateTest {

    @Test
    public void orTest() {
        Predicate<Integer> f = x -> x > 20;
        Predicate<Integer> g = x -> x < 10;
        assertTrue(f.or(g).apply(5));
        assertTrue(f.or(g).apply(25));
        assertFalse(f.or(g).apply(15));
    }

    @Test
    public void orLazyOkTest() {
        Predicate<Integer> f = x -> x > 20;
        Predicate<Integer> g = x -> {throw new IllegalArgumentException();};
        assertTrue(f.or(g).apply(30));
    }

    @Test (expected = IllegalArgumentException.class)
    public void orLazyExceptionTest() {
        Predicate<Integer> f = x -> x > 20;
        Predicate<Integer> g = x -> {throw new IllegalArgumentException();};
        assertTrue(g.or(f).apply(30));
    }

    @Test
    public void andTest() {
        Predicate<Integer> f = x -> x < 20;
        Predicate<Integer> g = x -> x > 10;
        assertFalse(f.and(g).apply(5));
        assertFalse(f.and(g).apply(25));
        assertTrue(f.and(g).apply(15));
    }

    @Test
    public void andLazyOkTest() {
        Predicate<Integer> f = x -> x > 20;
        Predicate<Integer> g = x -> {throw new IllegalArgumentException();};
        assertFalse(f.and(g).apply(10));
    }

    @Test (expected = IllegalArgumentException.class)
    public void andLazyExceptionTest() {
        Predicate<Integer> f = x -> x > 20;
        Predicate<Integer> g = x -> {throw new IllegalArgumentException();};
        assertFalse(g.and(f).apply(10));
    }

    @Test
    public void notTest() {
        Predicate<Integer> f = x -> x > 20;
        assertTrue(f.not().apply(10));
        assertFalse(f.not().apply(30));
    }

    @Test
    public void alwaysTrueTest() {
        Predicate<Integer> f = Predicate.ALWAYS_TRUE();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            assertTrue(f.apply(random.nextInt()));
        }
    }

    @Test
    public void alwaysFalseTest() {
        Predicate<Integer> f = Predicate.ALWAYS_FALSE();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            assertFalse(f.apply(random.nextInt()));
        }
    }
}
