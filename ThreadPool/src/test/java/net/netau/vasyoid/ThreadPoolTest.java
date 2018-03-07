package net.netau.vasyoid;

import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

public class ThreadPoolTest {

    @SuppressWarnings({"StatementWithEmptyBody"})
    @Test
    public void shutdownInterruptsAllThreadsTest() throws Exception {
        LightFuture[] tasks = new LightFuture[10];
        ThreadPoolImpl threadPool = new ThreadPoolImpl(tasks.length * 3);
        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = threadPool.add(() -> {
                while (!Thread.interrupted());
                Thread.currentThread().interrupt();
                return 0;
            });
        }
        threadPool.shutdown();
        for (LightFuture task : tasks) {
            task.get();
        }
    }

    @SuppressWarnings({"InfiniteLoopStatement", "StatementWithEmptyBody"})
    @Test
    public void thenApplyDoesNotBlockCurrentThreadTest() throws Exception {
        ThreadPoolImpl threadPool = new ThreadPoolImpl(1);
        threadPool.add(() -> {while (true) {}}).thenApply(x -> 0);
    }

    @Test
    public void isReadyTest() throws Exception {
        ThreadPoolImpl threadPool = new ThreadPoolImpl(1);
        LightFuture task = threadPool.add(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) { }
            return 0;
        });
        assertFalse(task.isReady());
        task.get();
        assertTrue(task.isReady());
    }

    @Test
    public void thenApplyInARowTest() throws Exception {
        ThreadPoolImpl threadPool = new ThreadPoolImpl(4);
        LightFuture<Integer> task = threadPool.add(() -> 42)
                .thenApply(x -> x - 2)
                .thenApply(x -> x / 4)
                .thenApply(x -> x + 1)
                .thenApply(x -> x * 3);
        assertEquals((Integer) (33), task.get());
    }

    @Test
    public void thenApplyMultipleTimes() throws Exception {
        ThreadPoolImpl threadPool = new ThreadPoolImpl(4);
        LightFuture<Integer> task = threadPool.add(() -> 42);
        for (int i = 0; i < 10; ++i) {
            int value = i;
            assertEquals((Integer) (42 + value), task.thenApply(x -> x + value).get());
        }
    }

    @Test(expected = LightExecutionException.class)
    public void taskThrowsExceptionTest() throws Exception {
        ThreadPoolImpl threadPool = new ThreadPoolImpl(1);
        threadPool.add(() -> {throw new RuntimeException();}).get();
    }

    @Test
    public void differentTypeTasksTest() throws Exception {
        ThreadPoolImpl threadPool = new ThreadPoolImpl(5);
        LightFuture<Integer> integerTask = threadPool.add(() -> 42);
        LightFuture<String> stringTask = threadPool.add(() -> "Hello world");
        Object object = new Object();
        LightFuture<Object> objectTask = threadPool.add(() -> object);
        LightFuture nullTask = threadPool.add(() -> null);
        assertEquals((Integer) 42, integerTask.get());
        assertEquals("Hello world", stringTask.get());
        assertEquals(object, objectTask.get());
        assertNull(nullTask.get());
    }

    @Test
    public void threadsNumberTest() throws Exception {
        ThreadPoolImpl threadPool = new ThreadPoolImpl(10);
        LightFuture[] tasks = new LightFuture[20];
        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = threadPool.add(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) { }
                return Thread.currentThread().getId();
            });
        }
        HashSet<Long> threadIds = new HashSet<>();
        for (LightFuture task : tasks) {
            threadIds.add((Long) task.get());
        }
        assertEquals(10, threadIds.size());
    }

}