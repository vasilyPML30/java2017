package net.netau.vasyoid;

import com.google.common.collect.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

public class SecondPartTasksTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private File prepareFile(String fileName, String string) {
        File file = null;
        try {
            file = testFolder.newFile(fileName);
            PrintWriter writer = new PrintWriter(file);
            writer.write(string);
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            fail();
        }
        return file;
    }

    @Test
    public void testFindQuotes() throws IOException {
        File file1 = prepareFile("f1", "");
        File file2 = prepareFile("f2", "hello world!\nhi world!\nhello...");
        File file3 = prepareFile("f4", "asdfg\nqwert\nzxcvb\nkjdhellsda");
        File file5 = prepareFile("f5", "WHAT THE HELL?!");
        File file6 = prepareFile("f6", "heaven & hell");
        List<String> expected = Arrays.asList(
                "hello world!",
                "hello...",
                "kjdhellsda",
                "heaven & hell");

        assertEquals(expected, SecondPartTasks.findQuotes(
                Arrays.asList(
                        file1 != null ? file1.getAbsolutePath() : "",
                        file2 != null ? file2.getAbsolutePath() : "",
                        file3 != null ? file3.getAbsolutePath() : "",
                        file5 != null ? file5.getAbsolutePath() : "",
                        file6 != null ? file6.getAbsolutePath() : ""
                    ),
                    "hell"));
    }

    @Test
    public void testPiDividedBy4() {
        for (int i = 0; i < 10; i++) {
            assertEquals(Math.PI / 4, SecondPartTasks.piDividedBy4(), 0.001);
        }
    }

    @Test
    public void testFindPrinter() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("a", Lists.newArrayList("aa"));
        assertEquals("a", SecondPartTasks.findPrinter(map));
        map.put("b", Lists.newArrayList("bb", "b"));
        assertEquals("b", SecondPartTasks.findPrinter(map));
        map.put("c", Lists.newArrayList("cccccc"));
        assertEquals("c", SecondPartTasks.findPrinter(map));
        map.put("d", Lists.newArrayList("d", "d", "d", "d", "d", "d", "d"));
        assertEquals("d", SecondPartTasks.findPrinter(map));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindPrinterFail() {
        SecondPartTasks.findPrinter(new HashMap<>());
    }

    @Test
    public void testCalculateGlobalOrder() {
        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();
        Map<String, Integer> map3 = new HashMap<>();
        List<Map<String, Integer>> orders = new ArrayList<>();
        Map<String, Integer> expected = new HashMap<>();
        map2.put("a", 0);
        map2.put("b", 1);
        map2.put("c", 2);
        map3.put("b", 0);
        map3.put("c", 1);
        map3.put("d", 2);
        orders.add(map1);
        orders.add(map2);
        orders.add(map3);
        expected.put("a", 0);
        expected.put("b", 1);
        expected.put("c", 3);
        expected.put("d", 2);
        assertEquals(expected, SecondPartTasks.calculateGlobalOrder(orders));
        assertEquals(map1, SecondPartTasks.calculateGlobalOrder(new LinkedList<>()));
    }
}