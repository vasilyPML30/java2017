import org.junit.Test;

import static org.junit.Assert.*;

public class MD5Test {

    @Test
    public void singleThreadedDummyTest() throws Exception {
        new MD5SingleThread().get(".");
    }

    @Test
    public void multyThreadedDummyTest() throws Exception {
        new MD5SingleThread().get(".");
    }

    @Test
    public void compareResultsSameDirectory() throws Exception {
        assertArrayEquals(new MD5MultiThread().get("."), new MD5SingleThread().get("."));
    }

    private static void assertArrayNotEquals(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return;
        }
        boolean flag = true;
        for (int i = 0; i < a.length; i++) {
            flag &= (a[i] == b[i]);
        }
        assertFalse(flag);
    }

    @Test
    public void compareResultsDifferentDirectoriesSingleThread() throws Exception {
        assertArrayNotEquals(new MD5SingleThread().get("."), new MD5SingleThread().get("../"));
    }

    @Test
    public void compareResultsDifferentDirectoriesMultiThread() throws Exception {
        assertArrayNotEquals(new MD5MultiThread().get("."), new MD5SingleThread().get("../"));
    }


}