package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Reads numbers from input file and writes them squared to output file.
 */
public class Main {

    /**
     * Reads double number from input stream.
     * @param input where to read from.
     * @return Just number if read successfully, nothing otherwise.
     * @throws IOException if any problems with reading happen.
     */
    public static @NotNull
    Maybe<Double> readDouble(@NotNull Scanner input) throws IOException {
        String line = input.nextLine();
        try {
            return Maybe.just(Double.parseDouble(line));
        } catch (NumberFormatException e) {
            return Maybe.nothing();
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

    /**
     * Main method.
     * @param args input file and output file.
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Must be two parameters: input file, output file.");
            return;
        }
        try (Scanner input = new Scanner(new File(args[0]));
             PrintWriter output = new PrintWriter(new File(args[1]))) {
            while (input.hasNext()) {
                writeDouble(readDouble(input).map(x -> x * x), output);
            }
        } catch (IOException e) {
            System.out.println("Problem while reading or writing: " + e.getMessage());
        }
    }

}
