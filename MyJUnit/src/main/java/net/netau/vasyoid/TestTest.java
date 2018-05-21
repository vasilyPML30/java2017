package net.netau.vasyoid;

import net.netau.vasyoid.annotations.Test;

public class TestTest {

    @Test
    public void okTest() throws Exception {

    }

    @Test
    public void failTest() throws Exception {
        throw new Exception("message about error");
    }

    @Test(ignore = "no need to test")
    public void ignoreTest() throws Exception {

    }

}
