package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;

public interface Predicate<A> extends Function1<A, Boolean> {
    @NotNull
    default Predicate<A> or(@NotNull Predicate<? super A> p) {
        return (A argument) -> apply(argument) || p.apply(argument);
    }
    @NotNull
    default Predicate<A> and(@NotNull Predicate<? super A> p) {
        return (A argument) -> apply(argument) && p.apply(argument);
    }
    @NotNull
    default Predicate<A> not() {
        return (A argument) -> !apply(argument);
    }
    @NotNull
    static Predicate ALWAYS_TRUE() {
        return x -> true;
    }
    @NotNull
    static Predicate ALWAYS_FALSE() {
        return x -> false;
    }
}
