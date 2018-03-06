package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class LazyFactory {

    public static <T> Lazy<T> createSingleThreadLazy(@NotNull Supplier<T> task) {
        return new SingleThreadLazy<>(task);
    }

    public static <T> Lazy<T> createMultiThreadLazy(@NotNull Supplier<T> task) {
        return new MultiThreadLazy<>(task);
    }

    private static class SingleThreadLazy<T> implements Lazy<T> {

        private Supplier<T> task;
        private T result;

        SingleThreadLazy(@NotNull Supplier<T> task) {
            this.task = task;
        }

        @Override
        public T get() {
            if (task != null) {
                result = task.get();
                task = null;
            }
            return result;
        }
    }

    private static class MultiThreadLazy<T> implements Lazy<T> {

        private volatile Supplier<T> task;
        private T result;

        MultiThreadLazy(@NotNull Supplier<T> task) {
            this.task = task;
        }

        @Override
        public T get() {
            if (task != null) {
                synchronized (this) {
                    if (task != null) {
                        result = task.get();
                        task = null;
                    }
                }
            }
            return result;
        }
    }
}
