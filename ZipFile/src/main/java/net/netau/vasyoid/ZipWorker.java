package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * This class contains methods that find zip archives and extract specified entries from them.
 */
public class ZipWorker {
    private static final int BUFFERSIZE = 1024;

    /**
     * Recursively finds all zip archives in a specified directory.
     * @param directory place where to search.
     * @param result initial list.
     * @return list of found file descriptors.
     */
    public static @NotNull ArrayList<File> listArchives(@NotNull File directory,
                                                        @NotNull ArrayList<File> result) {
        File[] files = directory.listFiles();
        if (files == null) {
            return result;
        }
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".zip")) {
                result.add(file);
            } else if (file.isDirectory() && file.exists()) {
                listArchives(file, result);
            }
        }
        return result;
    }

    /**
     * Extracts a specified entry from an specified input to a specified directory.
     * @param input zip input stream which contains the entry.
     * @param entry entry to extract.
     * @param outFolder directory where to extract.
     */
    private static void unzipEntry(@NotNull ZipInputStream input,
                                  @NotNull ZipEntry entry,
                                  @NotNull File outFolder) {
        File outFile = new File(outFolder, entry.getName());
        outFile.getParentFile().mkdirs();
        try (OutputStream output = new FileOutputStream(outFile)) {
            byte[] buffer = new byte[BUFFERSIZE];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException exception) {
            System.out.println("Error extracting file: " + exception.getMessage());
        }
    }

    /**
     * Extracts all files matching a given pattern.
     * @param inFile archive with files to extract.
     * @param outFolder directory where to extract.
     * @param regex only files matching this pattern will be extracted.
     */
    public static void unzipByRegex(@NotNull File inFile,
                                    @NotNull File outFolder,
                                    @NotNull Pattern regex) {
        try (ZipInputStream input = new ZipInputStream(
                new BufferedInputStream(
                        new FileInputStream(inFile)))) {
            ZipEntry entry;
            while ((entry = input.getNextEntry()) != null) {
                if (entry.isDirectory() || !regex.matcher(entry.getName()).matches()) {
                    continue;
                }
                unzipEntry(input, entry, outFolder);
            }
        } catch (IOException exception) {
            System.out.println("Error unzipping archive: " + exception.getMessage());
        }
    }


}
