package net.netau.vasyoid.classes;

import net.netau.vasyoid.annotations.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

public class PassingTests {

    @Test
    public void simpleTest() {
        System.out.println("Hello");
    }

    @Test(expected = NullPointerException.class)
    public void expectedExceptionTest() throws Exception {
        throw new NullPointerException();
    }

    @Test(expected = IOException.class)
    public void expectedParentExceptionTest() throws Exception {
        throw new FileNotFoundException();
    }

    @SuppressWarnings("DefaultAnnotationParam")
    @Test(ignore = "")
    public void emptyIgnoreTest() {
        System.out.println("Hello");
    }

}
