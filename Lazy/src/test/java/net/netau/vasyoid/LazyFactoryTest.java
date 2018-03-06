package net.netau.vasyoid;

import org.junit.Test;

import java.util.function.Supplier;

import static org.junit.Assert.*;

public class LazyFactoryTest {

    private static int TEST_NUM = 100;

    private void runThreads(Runnable task) throws Exception {
        Thread[] threads = new Thread[TEST_NUM];
        for (int i = 0; i < TEST_NUM; i++) {
            threads[i] = new Thread(task);
            threads[i].start();
        }
        for (int i = 0; i < TEST_NUM; i++) {
            threads[i].join();
        }
    }

    @Test
    public void multipleComputationsAssertCheck() throws Exception {
        Lazy singleLazyCustom = LazyFactory.createSingleThreadLazy(new CustomSupplier());
        Lazy multiLazyCustom = LazyFactory.createMultiThreadLazy(new CustomSupplier());
        for (int i = 0; i < TEST_NUM; i++) {
            singleLazyCustom.get();
            multiLazyCustom.get();
        }
    }

    @Test
    public void multiThreadMultipleComputationsAssertCheck() throws Exception {
        Lazy lazyCustom = LazyFactory.createMultiThreadLazy(new CustomSupplier());
        runThreads(lazyCustom::get);
    }

    @Test
    public void multipleGetsReturnTheSameNewObject() throws Exception {
        Lazy singleLazyObject = LazyFactory.createSingleThreadLazy(Object::new);
        Lazy multiLazyObject = LazyFactory.createMultiThreadLazy(Object::new);
        Object singleObject = singleLazyObject.get();
        Object multiObject = multiLazyObject.get();
        for (int i = 0; i < TEST_NUM; i++) {
            assertTrue(singleObject == singleLazyObject.get());
            assertTrue(multiObject == multiLazyObject.get());
        }
    }

    @Test
    public void multiThreadMultipleGetsReturnTheSameNewObject() throws Exception {
        Lazy lazyObject = LazyFactory.createMultiThreadLazy(Object::new);
        Object object = lazyObject.get();
        runThreads(() -> assertTrue(object == lazyObject.get()));
    }

    @Test
    public void multipleGetsReturnTheSameOldObject() throws Exception {
        Object object = new Object();
        Lazy singleLazyObject = LazyFactory.createSingleThreadLazy(() -> object);
        Lazy multiLazyObject = LazyFactory.createMultiThreadLazy(() -> object);
        for (int i = 0; i < TEST_NUM; i++) {
            assertTrue(object == singleLazyObject.get());
            assertTrue(object == multiLazyObject.get());
        }
    }

    @Test
    public void multiThreadMultipleGetsReturnTheSameOldObject() throws Exception {
        Object object = new Object();
        Lazy lazyObject = LazyFactory.createMultiThreadLazy(() -> object);
        runThreads(() -> assertTrue(object == lazyObject.get()));
    }

    @Test
    public void nullObject() throws Exception {
        Lazy singleLazyNull = LazyFactory.createSingleThreadLazy(() -> null);
        Lazy multiLazyNull = LazyFactory.createMultiThreadLazy(() -> null);
        for (int i = 0; i < TEST_NUM; i++) {
            assertNull(singleLazyNull.get());
            assertNull(multiLazyNull.get());
        }
    }

    @Test
    public void multiThreadNullObject() throws Exception {
        Lazy lazyNull = LazyFactory.createMultiThreadLazy(() -> null);
        runThreads(() -> assertNull(lazyNull.get()));
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