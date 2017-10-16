package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Container for one element.
 * Contains either one element or nothing.
 * @param <T> type of stored element.
 */
public class Maybe<T> {
    private T value;
    private boolean isPresent;

    private Maybe(@Nullable T value) {
        this.value = value;
        isPresent = (value != null);
    }

    /**
     * Wraps element into Maybe container.
     * @param t value to store.
     * @param <T> type of stored element.
     * @return element wrapped into Maybe container.
     */
    public static @NotNull <T> Maybe<T> just(@NotNull T t) {
        return new Maybe<>(t);
    }

    /**
     * Creates Maybe container without an element.
     * @param <T> expected type.
     * @return container without an element.
     */
    public static @NotNull <T> Maybe<T> nothing() {
        return new Maybe<>(null);
    }

    /**
     * Gets value from non empty Maybe container.
     * @return value stored inside container.
     * @throws GetFromNothingException is thrown when trying to get
     * a value from an empty container.
     */
    public @NotNull T get() throws GetFromNothingException {
        if (!isPresent) {
            throw new GetFromNothingException();
        }
        return value;
    }

    /**
     * Checks if there is an element inside.
     * @return true if there is an element, false otherwise.
     */
    public boolean isPresent() {
        return isPresent;
    }

    /**
     * Applies mapper function to the value.
     * @param mapper function to apply.
     * @param <U> function return type.
     * @return application result.
     */
    public @NotNull <U> Maybe<U> map(@NotNull Function<? super T, ? extends U> mapper) {
        if (!isPresent) {
            return nothing();
        }
        return new Maybe<>(mapper.apply(value));
    }

    /**
     * Reads double number from input stream.
     * @param input where to read from.
     * @return Just number if read successfully, nothing otherwise.
     * @throws IOException if any problems with reading happen.
     */
    public static @NotNull Maybe<Double> readDouble(@NotNull Scanner input) throws IOException {
        String line = input.nextLine();
        try {
            return just(Double.parseDouble(line));
        } catch (NumberFormatException e) {
            return nothing();
        }
    }

    /**
     * Writes double number to output stream (or "null" if number == nothing)
     * @param number Maybe container with number to write.
     * @param output where to write.
     * @throws IOException if any problems with writing happen.
     */
    public static void writeDouble(@NotNull Maybe<Double> number,
                                   @NotNull PrintWriter output) throws IOException {
        try {
            output.println(String.valueOf(number.get()));
        } catch (GetFromNothingException e) {
            output.println("null");
        }
    }

}
