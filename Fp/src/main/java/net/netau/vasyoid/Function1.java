package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;

public interface Function1<A, R> {
    R apply(A argument);

    @NotNull
    default <S> Function1<A, S> compose(@NotNull Function1<? super R, ? extends S> g) {
        return (A argument) -> g.apply(apply(argument));
    }
}
