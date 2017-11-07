package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;

public interface Function2<A, B, R> {
    R apply(A firstArgument, B secondArgument);

    @NotNull
    default <S> Function2<A, B, S> compose(@NotNull Function1< ? super R, ? extends S> g) {
        return (A firstArgument, B secondArgument) -> g.apply(apply(firstArgument, secondArgument));
    }

    @NotNull
    default Function1<B, R> bind1(A firstArgument) {
        return (B secondArgument) -> apply(firstArgument, secondArgument);
    }

    @NotNull
    default Function1<A, R> bind2(B secondArgument) {
        return (A firstArgument) -> apply(firstArgument, secondArgument);
    }

    @NotNull
    default Function2<B, A, R> swapArguments() {
        return (firstArgument, secondArgument) -> apply(secondArgument, firstArgument);
    }

    @NotNull
    default Function1<B, Function1<A, R>> curry() {
        return this::bind2;
    }
}
