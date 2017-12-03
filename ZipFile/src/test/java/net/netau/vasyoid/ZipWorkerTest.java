package net.netau.vasyoid;

import org.junit.Test;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * This class tests all public methods of the ZipWorker class.
 */
public class ZipWorkerTest {

    /**
     * ZipFile.listArchives() method is tested.
     */
    @Test
    public void TestListArchives() {
        File[] expected = new File[2];
        expected[0] = new File("testFolder/file2.zip");
        expected[1] = new File("testFolder/folder2/file4.zip");
        File[] actual = ZipWorker.listArchives(new File("testFolder"), new ArrayList<>()).toArray(new File[]{});
        Arrays.sort(actual, Comparator.comparing(File::getPath));
        assertArrayEquals(expected, actual);
    }

    /**
     * ZipFile.unzipByRegex() method is tested.
     * @throws IOException this method has to clean up after its work.
     */
    @Test
    public void TestUnzipByRegex() throws IOException {
        String[] expected = (new File("testFolder")).list();
        Arrays.sort(expected);
        File unzippedFolder = new File("testFolder/unzipped");
        unzippedFolder.mkdir();
        ZipWorker.unzipByRegex(new File("testFolder/file2.zip"), unzippedFolder, Pattern.compile(".*"));
        String[] actual = unzippedFolder.list();
        Arrays.sort(actual);
        for (int i = 0; i < actual.length; i++) {
            actual[i] = actual[i].replace("/unzipped/", "/");
        }
        FileUtils.deleteDirectory(unzippedFolder);
        assertArrayEquals(expected, actual);
    }
}
