package net.netau.vasyoid;

import org.junit.Test;

import static org.junit.Assert.*;

public class MyFileTest {

    @Test
    public void toStringTest() throws Exception {
        MyFile file = new MyFile("file", false);
        assertEquals("file", file.toString());
        file = new MyFile("directory", true);
        assertEquals("directory/", file.toString());
    }

    @Test
    public void equalsTest() throws Exception {
        MyFile file1 = new MyFile("file1", false);
        MyFile file2 = new MyFile("file2", false);
        MyFile file3 = new MyFile("file1", false);
        MyFile dir1 = new MyFile("directory1", true);
        MyFile dir2 = new MyFile("directory2", true);
        MyFile dir3 = new MyFile("directory1", true);
        assertEquals(file1, file3);
        assertNotEquals(file1, file2);
        assertEquals(dir1, dir3);
        assertNotEquals(dir1, dir2);
        assertEquals(file1, file1);
        assertEquals(dir1, dir1);
        assertNotEquals(file1, new Object());
    }

}