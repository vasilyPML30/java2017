package net.netau.vasyoid;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class LazyFactoryTest {

    @Test
    public void createSingleThreadLazy() throws Exception {
        Lazy x = LazyFactory.createSingleThreadLazy(() -> 5);
        assertTrue(false);
    }

    @Test
    public void createMultiThreadLazy() throws Exception {
        Lazy x = LazyFactory.createMultiThreadLazy(() -> 5);
    }
}