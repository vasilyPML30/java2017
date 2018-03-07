package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Thread pool with a fixed number of threads.
 */
public class ThreadPoolImpl {

    private Thread[] threads;
    private final Queue<ThreadPoolTask> tasks = new ArrayDeque<>();

    /**
     * Public constructor.
     * @param maxThreads number of work threads.
     */
    public ThreadPoolImpl(int maxThreads) {
        threads = new Thread[maxThreads];
        for (int i = 0; i < maxThreads; i++) {
            threads[i] = new Thread(new ThreadPoolWorker());
            threads[i].start();
        }
    }

    /**
     * Add a new task to evaluate.
     * @param task task to evaluate.
     * @param <R> task result type.
     * @return LightFuture object representing the task.
     */
    @NotNull
    public <R> LightFuture<R> add(@NotNull Supplier<R> task) {
        ThreadPoolTask<R> acceptedTask = new ThreadPoolTask<>(task);
        synchronized (tasks) {
            tasks.add(acceptedTask);
            tasks.notify();
        }
        return acceptedTask;
    }

    /**
     * Kill all threads.
     */
    public void shutdown() {
        for (Thread thread : threads) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException ignored) { }
        }
    }

    private class ThreadPoolWorker implements Runnable {

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            ThreadPoolTask currentTask;
            try {
                while (!Thread.interrupted()) {
                    synchronized (tasks) {
                        while (tasks.isEmpty()) {
                                tasks.wait();
                        }
                        currentTask = tasks.poll();
                    }
                    currentTask.run();
                }
            } catch (InterruptedException ignored) { }
        }
    }

    private class ThreadPoolTask<T> implements LightFuture<T> {

        private boolean ready = false;
        private LightExecutionException error = null;
        private Supplier<T> task;
        private T result = null;

        public ThreadPoolTask(@NotNull Supplier<T> task) {
            this.task = task;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isReady() {
            return ready;
        }

        private synchronized void run() {
            if (ready) {
                return;
            }
            try {
                result = task.get();
            } catch (Exception e) {
                error = new LightExecutionException("Evaluation failed");
                error.addSuppressed(e);
            }
            ready = true;
            notifyAll();
        }

        /**
         * {@inheritDoc}
         */
        @Nullable
        @Override
        public synchronized T get() throws LightExecutionException {
            while (!ready) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new LightExecutionException("Interrupted");
                }
            }
            if (error != null) {
                throw error;
            }
            return result;
        }

        /**
         * {@inheritDoc}
         */
        @NotNull
        @Override
        public <E> LightFuture<E> thenApply(@NotNull Function<T, E> function) {
            return add(() -> {
                try {
                    return function.apply(get());
                } catch (LightExecutionException e) {
                    throw new RuntimeException(e.getMessage(), e.getCause());
                }
            });
        }
    }
}
