package net.netau.vasyoid.classes;

import net.netau.vasyoid.annotations.Test;

public class FailingTestClass {

    private FailingTestClass() { }

    @Test
    public void simpleTest() {
        System.out.println("Hello");
    }

}
