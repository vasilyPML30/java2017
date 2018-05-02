package net.netau.vasyoid;

import java.io.File;
import java.net.InetAddress;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        try (FtpServer server = new FtpServer(InetAddress.getLoopbackAddress(), 11111);
             FtpClient client = new FtpClient(InetAddress.getLoopbackAddress(), 11111)) {
            Thread serverThread = new Thread(server);
            serverThread.start();
            List<String> list = client.list("testDir/");
            for (String fileName : list) {
                System.out.println(fileName);
            }
            System.out.println("success: " + client.get("testDir/file.txt"));
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }

}
