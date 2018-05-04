package net.netau.vasyoid;

import java.net.InetAddress;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        try (FtpServer server = new FtpServer(InetAddress.getLoopbackAddress(), 11111);
             FtpClient client = new FtpClient(InetAddress.getLoopbackAddress(), 11111)) {
            Thread serverThread = new Thread(server);
            serverThread.start();
            List<MyFile> list = client.list("testDir/");
            for (MyFile file : list) {
                System.out.println(file);
            }
            System.out.println("success: " + client.get("testDir/lines.mp4"));
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }

}
