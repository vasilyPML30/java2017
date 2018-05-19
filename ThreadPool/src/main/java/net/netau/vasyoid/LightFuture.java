package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Task accepted by a thread pool.
 * @param <T> result type.
 */
public interface LightFuture<T> {

    /**
     * Is task finished or not.
     * @return true if the task has been completed, false otherwise.
     */
    boolean isReady();

    /**
     * Task result.
     * Blocks the current thread until the result is available.
     * @return the result of the computation.
     * @throws LightExecutionException if an error occurs while computing the result
     * or the current thread is interrupted.
     */
    @Nullable
    T get() throws LightExecutionException;

    /**
     * Applies a function to the result of the task.
     * @param function function that will be applied.
     * @param <E> function return type.
     * @return a new LightFuture task that represents function application.
     */
    @NotNull
    <E> LightFuture<E> thenApply(@NotNull Function<? super T, E> function);
}
