package net.netau.vasyoid;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Assuming FtpServer works well.
 */
public class FtpClientTest {

    private Thread serverThread;
    private FtpServer server;
    private FtpClient client;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void initialize() throws Exception {
        server = new FtpServer(InetAddress.getLoopbackAddress(), 11111);
        serverThread = new Thread(server);
        serverThread.start();
        client = new FtpClient(InetAddress.getLoopbackAddress(), 11111);
    }

    @After
    public void close() throws Exception {
        server.close();
        client.close();
        serverThread.join();
    }

    @Test
    public void listWrongPathTest() throws Exception {
        assertTrue(client.list("...folderthatdoesnotexist...").isEmpty());
    }

    @Test
    public void listEmptyDirectoryTest() throws Exception {
        File folder = temporaryFolder.newFolder("...emptyfolder...");
        assertTrue(client.list(folder.getAbsolutePath()).isEmpty());
    }

    @Test
    public void listNormal() throws Exception {
        File folder = temporaryFolder.newFolder("...normalfolder...");
        temporaryFolder.newFile(folder.getName() + "/file1");
        temporaryFolder.newFile(folder.getName() + "/file2");
        temporaryFolder.newFolder(folder.getName(), "subfolder");
        List<MyFile> expected = new ArrayList<>();
        expected.add(new MyFile("file1", false));
        expected.add(new MyFile("file2", false));
        expected.add(new MyFile("subfolder", true));
        assertEquals(expected, client.list(folder.getAbsolutePath()));
    }

    @Test
    public void getWrongPathTest() throws Exception {
        assertEquals(null, client.get("...filethatdoesnotexist..."));
    }

    @Test
    public void getEmptyFileTest() throws Exception {
        File file = temporaryFolder.newFile("...emptyfile...");
        assertEquals(null, client.get(file.getAbsolutePath()));
    }

    private byte[] createFile(File file, int size) throws Exception {
        byte[] bytes = new byte[size];
        new Random().nextBytes(bytes);
        Files.write(file.toPath(), bytes, StandardOpenOption.WRITE);
        return bytes;
    }

    @Test
    public void getSmallFileTest() throws Exception {
        File file = temporaryFolder.newFile("...file...");
        byte[] expected = createFile(file, 1024);
        assertEquals(file.getName(), client.get(file.getAbsolutePath()).getPath());
        assertArrayEquals(expected, Files.readAllBytes(new File(file.getName()).toPath()));
        Files.delete(new File(file.getName()).toPath());
    }

}