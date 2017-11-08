package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;

/**
 * Partial implementation od Function1 class.
 * Represents a function with Boolean as the return type.
 * @param <A> argument type.
 */
public interface Predicate<A> extends Function1<A, Boolean> {

    /**
     * Gets a predicate and returns a predicate which is true if and only if
     * the passed or the stored predicate is true.
     * @param p another predicate.
     * @return the resulting predicate.
     */
    @NotNull
    default Predicate<A> or(@NotNull Predicate<? super A> p) {
        return (A argument) -> apply(argument) || p.apply(argument);
    }

    /**
     * Gets a predicate and returns a predicate which is true if and only if
     * both the passed and the stored predicate is true.
     * @param p another predicate.
     * @return the resulting predicate.
     */
    @NotNull
    default Predicate<A> and(@NotNull Predicate<? super A> p) {
        return (A argument) -> apply(argument) && p.apply(argument);
    }

    /**
     * Inverts the stored predicate.
     * @return rhe resulting predicate.
     */
    @NotNull
    default Predicate<A> not() {
        return (A argument) -> !apply(argument);
    }

    /**
     * Generates a predicate which is true regardless of the passed argument.
     * @return the predicate.
     */
    @NotNull
    static Predicate ALWAYS_TRUE() {
        return x -> true;
    }

    /**
     * Generates a predicate which is false regardless of the passed argument.
     * @return the predicate.
     */
    @NotNull
    static Predicate ALWAYS_FALSE() {
        return x -> false;
    }
}
