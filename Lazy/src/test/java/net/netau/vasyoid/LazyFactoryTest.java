package net.netau.vasyoid;

import org.junit.Test;

import java.util.function.Supplier;

import static org.junit.Assert.*;

public class LazyFactoryTest {

    @Test
    public void multipleComputationsAssertCheck() throws Exception {
        Lazy singleLazyCustom = LazyFactory.createSingleThreadLazy(new CustomSupplier());
        Lazy multiLazyCustom = LazyFactory.createMultiThreadLazy(new CustomSupplier());
        for (int i = 0; i < 10; i++) {
            singleLazyCustom.get();
            multiLazyCustom.get();
        }
    }

    @Test
    public void multipleGetsReturnTheSameNewObject() throws Exception {
        Lazy singleLazyObject = LazyFactory.createSingleThreadLazy(Object::new);
        Lazy multiLazyObject = LazyFactory.createSingleThreadLazy(Object::new);
        Object singleObject = singleLazyObject.get();
        Object multiObject = multiLazyObject.get();
        for (int i = 0; i < 10; i++) {
            assertTrue(singleObject == singleLazyObject.get());
            assertTrue(multiObject == multiLazyObject.get());
        }
    }

    @Test
    public void multipleGetsReturnTheSameOldObject() throws Exception {
        Object object = new Object();
        Lazy singleLazyObject = LazyFactory.createSingleThreadLazy(() -> object);
        Lazy multiLazyObject = LazyFactory.createSingleThreadLazy(() -> object);
        for (int i = 0; i < 10; i++) {
            assertTrue(object == singleLazyObject.get());
            assertTrue(object == multiLazyObject.get());
        }
    }

    @Test
    public void nullObject() throws Exception {
        Lazy singleLazyNull = LazyFactory.createSingleThreadLazy(() -> null);
        Lazy multiLazyNull = LazyFactory.createSingleThreadLazy(() -> null);
        for (int i = 0; i < 10; i++) {
            assertNull(singleLazyNull.get());
            assertNull(multiLazyNull.get());
        }
    }

    private static class CustomSupplier implements Supplier<Object> {

        boolean firstTime = true;

        @Override
        public Object get() {
            if (!firstTime) {
                assertTrue(false);
            }
            firstTime = false;
            return new Object();
        }
    }
}