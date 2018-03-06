package net.netau.vasyoid;

import org.jetbrains.annotations.Nullable;

/**
 * Interface representing lazy computation.
 * @param <T> stored object type.
 */
public interface Lazy<T> {

    /**
     * First invocation causes computation and returns the result.
     * All the following invocations return the same object as the first one.
     * @return computation result.
     */
    @Nullable
    T get();
}
