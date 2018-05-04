package net.netau.vasyoid;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class FtpServerTest {

    private Thread serverThread;
    private FtpServer server;
    private Socket socket;
    private DataInputStream is;
    private DataOutputStream os;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void initialize() throws Exception {
        server = new FtpServer(InetAddress.getLoopbackAddress(), 11111);
        socket = new Socket(InetAddress.getLoopbackAddress(), 11111);
        serverThread = new Thread(server);
        serverThread.start();
        is = new DataInputStream(socket.getInputStream());
        os = new DataOutputStream(socket.getOutputStream());
    }

    @After
    public void close() throws Exception {
        server.close();
        is.close();
        os.close();
        socket.close();
        serverThread.join();
    }

    @Test
    public void listWrongPathTest() throws Exception {
        os.writeInt(FtpServer.LIST_QUERY_TYPE);
        os.writeUTF("...folderthatdoesnotexist...");
        os.flush();
        int actual = is.readInt();
        assertEquals(0, actual);
    }

    @Test
    public void listEmptyDirectoryTest() throws Exception {
        File folder = temporaryFolder.newFolder("...emptyfolder...");
        os.writeInt(FtpServer.LIST_QUERY_TYPE);
        os.writeUTF(folder.getAbsolutePath());
        os.flush();
        assertEquals(0, is.readInt());
    }

    @Test
    public void listNormal() throws Exception {
        File folder = temporaryFolder.newFolder("...normalfolder...");
        temporaryFolder.newFile(folder.getName() + "/file1");
        temporaryFolder.newFile(folder.getName() + "/file2");
        temporaryFolder.newFolder(folder.getName(), "subfolder");
        os.writeInt(FtpServer.LIST_QUERY_TYPE);
        os.writeUTF(folder.getAbsolutePath());
        os.flush();
        List<MyFile> files = new ArrayList<>();
        files.add(new MyFile("file1", false));
        files.add(new MyFile("file2", false));
        files.add(new MyFile("subfolder", true));
        assertEquals(files.size(), is.readInt());
        for (MyFile expected : files) {
            String name = is.readUTF();
            boolean isDir = is.readBoolean();
            MyFile actual = new MyFile(name, isDir);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void getWrongPathTest() throws Exception {
        os.writeInt(FtpServer.GET_QUERY_TYPE);
        os.writeUTF("...filethatdoesnotexist...");
        os.flush();
        assertEquals(0, is.readLong());
    }

    @Test
    public void getEmptyFileTest() throws Exception {
        File file = temporaryFolder.newFile("...emptyfile...");
        os.writeInt(FtpServer.GET_QUERY_TYPE);
        os.writeUTF(file.getAbsolutePath());
        os.flush();
        assertEquals(0, is.readLong());
    }

    private byte[] createFileAndSendQuery(int size) throws Exception {
        File file = temporaryFolder.newFile("...file...");
        byte[] bytes = new byte[size];
        new Random().nextBytes(bytes);
        Files.write(file.toPath(), bytes, StandardOpenOption.WRITE);
        os.writeInt(FtpServer.GET_QUERY_TYPE);
        os.writeUTF(file.getAbsolutePath());
        os.flush();
        return bytes;
    }

    @Test
    public void getSmallFileTest() throws Exception {
        byte[] expected = createFileAndSendQuery(1024);
        assertEquals(expected.length, is.readLong());
        byte[] actual = new byte[expected.length];
        assertEquals(expected.length, is.read(actual));
        assertArrayEquals(expected, actual);
    }

    @Test
    public void getLargeFileTest() throws Exception {
        byte[] expected = createFileAndSendQuery(1 << 20);
        assertEquals(expected.length, is.readLong());
        byte[] actual = new byte[expected.length];
        for (int i = 0; i < expected.length / 1024; ++i) {
            assertEquals(1024, is.read(actual, i * 1024, 1024));
        }
        assertArrayEquals(expected, actual);
    }

}