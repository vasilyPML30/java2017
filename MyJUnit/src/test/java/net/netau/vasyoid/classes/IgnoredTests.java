package net.netau.vasyoid.classes;

import net.netau.vasyoid.annotations.Test;

public class IgnoredTests {

    @Test(ignore = "Test is ignored")
    public void simpleIgnoredTest() {
        System.out.println("Hello");
    }

    @Test(ignore = "Test is ignored", expected = NullPointerException.class)
    public void expectedExceptionIgnoredTest() {
        System.out.println("Hello");
    }

    @Test(ignore = "Test is ignored")
    public void failingIgnoredTest() {
        throw new NullPointerException();
    }

}
