package net.netau.vasyoid;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Reads numbers from input file and writes them squared to output file.
 */
public class Main {

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
                Maybe.writeDouble(Maybe.readDouble(input).map(x -> x * x), output);
            }
        } catch (IOException e) {
            System.out.println("Problem while reading or writing: " + e.getMessage());
        }
    }

}
