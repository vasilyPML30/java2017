package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;

/**
 * Class represents a function of one argument.
 * @param <A> argument type.
 * @param <R> return type.
 */
public interface Function1<A, R> {

    /**
     * Function call method.
     * @param argument function argument.
     * @return function value.
     */
    R apply(A argument);

    /**
     * Function composition operator. Gets a function and returns it applied to the stored one.
     * @param g function to compose.
     * @param <S> function return type.
     * @return function representing the composition.
     */
    @NotNull
    default <S> Function1<A, S> compose(@NotNull Function1<? super R, ? extends S> g) {
        return (A argument) -> g.apply(apply(argument));
    }
}
