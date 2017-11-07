package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;

import static java.util.Collections.reverse;

import java.util.LinkedList;
import java.util.List;

public class Collections {
    @NotNull
    public static <A, R> List<R> map(@NotNull Function1<? super A, ? extends R> function,
                                     @NotNull Iterable<? extends A> arguments) {
        List<R> result = new LinkedList<>();
        for (A element : arguments) {
            result.add(function.apply(element));
        }
        return result;
    }
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
    @NotNull
    public static <A> List<A> takeUnless(@NotNull Predicate<? super A> predicate,
                                         @NotNull Iterable<? extends A> arguments) {
        return takeWhile(predicate.not(), arguments);
    }

    public static <R, A> R foldl(@NotNull Function2<? super R, ? super A, ? extends R> function,
                                 R init, @NotNull Iterable<? extends A> arguments) {
        R result = init;
        for (A element : arguments) {
            result = function.apply(result, element);
        }
        return result;
    }
    public static <R, A> R foldr(@NotNull Function2<? super A, ? super R, ? extends R> function,
                                 R init, @NotNull Iterable<? extends A> arguments) {
        List<? extends A> list = map((x -> x), arguments);
        reverse(list);
        return foldl(function.swapArguments(), init, list);
    }
}
