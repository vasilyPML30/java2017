package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Factory class producing Lazy instances.
 */
public class LazyFactory {

    /**
     * Creates a Lazy instance that correctly works with a single thread.
     * @param task supplier that produces the object to store.
     * @param <T> stored object type.
     * @return Lazy instance.
     */
    @NotNull
    public static <T> Lazy<T> createSingleThreadLazy(@NotNull Supplier<T> task) {
        return new SingleThreadLazy<>(task);
    }

    /**
     * Creates a Lazy instance that correctly works with multiple threads.
     * @param task supplier that produces the object to store.
     * @param <T> stored object type.
     * @return Lazy instance.
     */
    @NotNull
    public static <T> Lazy<T> createMultiThreadLazy(@NotNull Supplier<T> task) {
        return new MultiThreadLazy<>(task);
    }

    /**
     * Lazy implementation for single-thread purposes.
     * @param <T> stored object type.
     */
    private static class SingleThreadLazy<T> implements Lazy<T> {

        private Supplier<T> task;
        private T result;

        SingleThreadLazy(@NotNull Supplier<T> task) {
            this.task = task;
        }

        /**
         * {@inheritDoc}
         */
        @Nullable
        @Override
        public T get() {
            if (task != null) {
                result = task.get();
                task = null;
            }
            return result;
        }
    }

    /**
     * Lazy implementation for multi-thread purposes.
     * @param <T> stored object type.
     */
    private static class MultiThreadLazy<T> implements Lazy<T> {

        private volatile Supplier<T> task;
        private T result;

        MultiThreadLazy(@NotNull Supplier<T> task) {
            this.task = task;
        }

        /**
         * {@inheritDoc}
         * This method is implemented using double-checking idiom to minimize the number of synchronizations.
         */
        @Nullable
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
