package net.netau.vasyoid;

import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Tests all public methods of Maybe class.
 */
public class MaybeTest {

    /**
     * Checks whether constructors work.
     */
    @Test
    public void notNullTest() {
        assertNotNull(Maybe.nothing());
        assertNotNull(Maybe.just(5));
        assertNotNull(Maybe.just("asd"));
    }

    /**
     * Checks Maybe.isPresent() method.
     */
    @Test
    public void isPresentTest() {
        assertTrue(Maybe.just(5).isPresent());
        assertTrue(Maybe.just("asd").isPresent());
        assertFalse(Maybe.nothing().isPresent());
    }

    /**
     * Checks Maybe.get() method.
     * Case: non-empty container.
     */
    @Test
    public void getJustTest() throws GetFromNothingException {
        assertEquals(new Integer(5), Maybe.just(5).get());
        assertEquals("asd", Maybe.just("asd").get());
    }

    /**
     * Checks Maybe.get() method.
     * Case: empty container.
     */
    @Test(expected = GetFromNothingException.class)
    public void getNothingTest() throws GetFromNothingException {
        Maybe.nothing().get();
    }

    /**
     * Checks Maybe.map() method.
     * Cases: empty/non-empty container.
     */
    @Test
    public void testMap() throws GetFromNothingException {
        assertEquals(new Integer(4), Maybe.just(2).map(x -> x * x).get());
        assertEquals("asd", Maybe.just("fghasd").map(s -> s.substring(3)).get());
        assertFalse(Maybe.nothing().map(x -> x).isPresent());
    }

    /**
     * Checks Maybe.readDouble() and Maybe.writeDouble() methods.
     */
    @Test
    public void testReadWriteDouble() throws IOException,
                                 GetFromNothingException {
        Scanner input = new Scanner(
                "1.2\n" +
                "-0.5\n" +
                "asd\n" +
                "\n" +
                "0002\n" +
                "3as\n");
        StringWriter stringWriter = new StringWriter();
        PrintWriter output = new PrintWriter(stringWriter);
        ArrayList<Maybe<Double>> actual = new ArrayList<>();
        while (input.hasNext()) {
            Maybe<Double> num = Main.readDouble(input);
            actual.add(num);
            Main.writeDouble(num, output);
        }
        assertEquals(new Double(1.2), actual.get(0).get());
        assertEquals(new Double(-0.5), actual.get(1).get());
        assertEquals(new Double(2.0), actual.get(4).get());
        assertFalse(actual.get(2).isPresent());
        assertFalse(actual.get(3).isPresent());
        assertFalse(actual.get(5).isPresent());
        assertEquals("1.2\n-0.5\nnull\nnull\n2.0\nnull\n", stringWriter.toString());
    }
}
