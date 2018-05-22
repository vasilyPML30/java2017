package net.netau.vasyoid.classes;

import net.netau.vasyoid.annotations.AfterClass;
import net.netau.vasyoid.annotations.Test;

public class AfterClassTest {

    @Test
    public void simpleTest() {
        System.out.println("Hello");
    }

    @AfterClass
    public void afterClass() throws Exception {
        throw new Exception("ACM");
    }

}
