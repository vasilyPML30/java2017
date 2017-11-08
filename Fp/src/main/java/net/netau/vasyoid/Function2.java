package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;

/**
 * Class represents a function of two arguments.
 * @param <A> first argument type.
 * @param <B> second argument type.
 * @param <R> return type.
 */
public interface Function2<A, B, R> {

    /**
     * Function call method.
     * @param firstArgument first function argument.
     * @param secondArgument second function argument.
     * @return function value.
     */
    R apply(A firstArgument, B secondArgument);

    /**
     * Function composition operator. Gets a function of one argument
     * and returns it applied to the stored one.
     * @param g function to compose.
     * @param <S> function return type.
     * @return function representing the composition.
     */
    @NotNull
    default <S> Function2<A, B, S> compose(@NotNull Function1< ? super R, ? extends S> g) {
        return (A firstArgument, B secondArgument) -> g.apply(apply(firstArgument, secondArgument));
    }

    /**
     * Binds the first argument of the stored function.
     * Function of one argument is returned.
     * @param firstArgument the value to bound the argument to.
     * @return the resulting function.
     */
    @NotNull
    default Function1<B, R> bind1(A firstArgument) {
        return (B secondArgument) -> apply(firstArgument, secondArgument);
    }

    /**
     * Binds the second argument of the stored function.
     * Function of one argument is returned.
     * @param secondArgument the value to bound the argument to.
     * @return the resulting function.
     */
    @NotNull
    default Function1<A, R> bind2(B secondArgument) {
        return (A firstArgument) -> apply(firstArgument, secondArgument);
    }

    /**
     * Converts the function into Function1.
     * @return bind2 operator for the function.
     */
    @NotNull
    default Function1<B, Function1<A, R>> curry() {
        return this::bind2;
    }

    /**
     * Flips the order the arguments are substituted to the function.
     * @return the resulting function.
     */
    @NotNull
    default Function2<B, A, R> flip() {
        return (firstArgument, secondArgument) -> apply(secondArgument, firstArgument);
    }

}
