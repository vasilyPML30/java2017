package net.netau.vasyoid.classes;

import net.netau.vasyoid.annotations.Test;

import java.io.IOException;

public class FailingTests {

    @Test
    public void unexpectedExceptionTest() throws Exception {
        throw new NullPointerException("NPE");
    }

    @Test(expected = NullPointerException.class)
    public void expectedExceptionNotThrownTest() throws Exception {
        System.out.println("Hello");
    }

    @Test(expected = NullPointerException.class)
    public void expectedWrongExceptionTest() throws Exception {
        throw new IOException("IOE");
    }

    @Test
    private void privateTestMethod() {
        System.out.println("Hello");
    }

}
