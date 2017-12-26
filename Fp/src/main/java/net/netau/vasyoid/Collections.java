package net.netau.vasyoid;

import static java.util.Collections.reverse;

import org.jetbrains.annotations.NotNull;
import java.util.LinkedList;
import java.util.List;

/**
 * The class implements various methods to work with Iterable collections
 * using functional style.
 */
public class Collections {

    /**
     * Gets a function and a collection of elements.
     * Applies the function to the elements and returns a List of the resulting values.
     * @param function function to apply.
     * @param arguments elements to apply function to.
     * @param <A> elements type.
     * @param <R> result values type.
     * @return List of resulting values.
     */
    @NotNull
    public static <A, R> List<R> map(@NotNull Function1<? super A, ? extends R> function,
                                     @NotNull Iterable<? extends A> arguments) {
        List<R> result = new LinkedList<>();
        for (A element : arguments) {
            result.add(function.apply(element));
        }
        return result;
    }

    /**
     * Gets a predicate and a collection of elements.
     * Returns a List of the the elements which satisfy the predicate.
     * @param predicate predicate to filter by.
     * @param arguments elements.
     * @param <A> elements type.
     * @return List of wanted elements.
     */
    @NotNull
    public static <A> List<A> filter(@NotNull Predicate<? super A> predicate,
                                     @NotNull Iterable<? extends A> arguments) {
        List<A> result = new LinkedList<>();
        for (A element : arguments) {
            if (predicate.apply(element)) {
                result.add(element);
            }
        }
        return result;
    }

    /**
     * Gets a predicate and a collection of elements.
     * Returns a List of the the elements before the first one that dissatisfies the predicate.
     * @param predicate predicate.
     * @param arguments elements.
     * @param <A> elements type.
     * @return List of wanted elements.
     */
    @NotNull
    public static <A> List<A> takeWhile(@NotNull Predicate<? super A> predicate,
                                        @NotNull Iterable<? extends A> arguments) {
        List<A> result = new LinkedList<>();
        for (A element : arguments) {
            if (!predicate.apply(element)) {
                break;
            }
            result.add(element);
        }
        return result;
    }

    /**
     * Gets a predicate and a collection of elements.
     * Returns a List of the the elements before the first one that satisfies the predicate.
     * @param predicate predicate.
     * @param arguments elements.
     * @param <A> elements type.
     * @return List of wanted elements.
     */
    @NotNull
    public static <A> List<A> takeUnless(@NotNull Predicate<? super A> predicate,
                                         @NotNull Iterable<? extends A> arguments) {
        return takeWhile(predicate.not(), arguments);
    }

    /**
     * Folds the elements from the given collection with a left-associative function.
     * @param function function to fold with.
     * @param init initial value.
     * @param arguments elements to fold.
     * @param <R> result type.
     * @param <A> elements type.
     * @return result value.
     */
    public static <R, A> R foldl(@NotNull Function2<? super R, ? super A, ? extends R> function,
                                 R init, @NotNull Iterable<? extends A> arguments) {
        R result = init;
        for (A element : arguments) {
            result = function.apply(result, element);
        }
        return result;
    }

    /**
     * Folds the elements from the given collection with a right-associative function.
     * @param function function to fold with.
     * @param init initial value.
     * @param arguments elements to fold.
     * @param <R> result type.
     * @param <A> elements type.
     * @return result value.
     */
    public static <R, A> R foldr(@NotNull Function2<? super A, ? super R, ? extends R> function,
                                 R init, @NotNull Iterable<? extends A> arguments) {
        List<? extends A> list = map((x -> x), arguments);
        reverse(list);
        return foldl(function.flip(), init, list);
    }
}
