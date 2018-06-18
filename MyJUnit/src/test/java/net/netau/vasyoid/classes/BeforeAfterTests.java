package net.netau.vasyoid.classes;

import net.netau.vasyoid.annotations.After;
import net.netau.vasyoid.annotations.Before;
import net.netau.vasyoid.annotations.BeforeClass;
import net.netau.vasyoid.annotations.Test;

public class BeforeAfterTests {

    private String beforeClassMessage;
    private String beforeMessage;
    private String afterMessage;

    @BeforeClass
    public void beforeClass() {
        beforeClassMessage = "BCM";
    }

    @Before
    public void before() {
        beforeMessage = "BM";
    }

    @After
    public void after() {
        afterMessage = "AM";
    }

    @Test
    public void beforeClassTest() throws Exception {
        throw new Exception(beforeClassMessage);
    }

    @Test
    public void beforeTest() throws Exception {
        throw new Exception(beforeMessage);
    }

    @Test
    public void afterTest() throws Exception {
        throw new Exception(afterMessage);
    }

}
