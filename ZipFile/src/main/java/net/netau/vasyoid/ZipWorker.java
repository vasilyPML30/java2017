package net.netau.vasyoid;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipWorker {
    private static final int BUFFERSIZE = 1024;

    public static ArrayList<File> listArchives(File directory, ArrayList<File> result) {
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

    public static void unzipEntry(ZipInputStream input, ZipEntry entry, File outFolder)
            throws IOException {
        File outFile = new File(outFolder, entry.getName());
        outFile.getParentFile().mkdirs();
        try (OutputStream output = new FileOutputStream(outFile)) {
            byte[] buffer = new byte[BUFFERSIZE];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
    }

    public static void unzipByRegex(File inFile, File outFolder, Pattern regex) {
        try (ZipInputStream input = new ZipInputStream(
                new BufferedInputStream(
                        new FileInputStream(inFile)))) {
            ZipEntry entry;
            while ((entry = input.getNextEntry()) != null) {
                if (entry.isDirectory() || !regex.matcher(entry.getName()).matches()) {
                    continue;
                }
                try {
                    unzipEntry(input, entry, outFolder);
                } catch (IOException exception) {
                    System.out.println("Error extracting file: " + exception.getMessage());
                }
            }
        } catch (IOException exception) {
            System.out.println("Error unzipping archive: " + exception.getMessage());
        }
    }


}
