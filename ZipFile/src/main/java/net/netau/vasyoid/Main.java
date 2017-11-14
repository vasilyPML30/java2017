package net.netau.vasyoid;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * This class implements the main project functionality.
 */
public class Main {


    /**
     * Searches for all zip files in a specialized directory
     * and extracts files with names matching a specialized regular expression.
     * @param args String array containing two parameters:
     * path to folder with zip files, pattern for files to extract.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("There must be two arguments:\n" +
                               "path to folder with zip files,\n" +
                               "pattern for files to extract.");
            return;
        }
        File directory = new File(args[0]);
        for (File file : ZipWorker.listArchives(directory, new ArrayList<>())) {
            ZipWorker.unzipByRegex(file, directory, Pattern.compile(args[1]));
        }
    }
}
