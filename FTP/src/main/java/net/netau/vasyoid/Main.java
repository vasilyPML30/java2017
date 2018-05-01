package net.netau.vasyoid;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try (FtpServer server = new FtpServer(InetAddress.getLoopbackAddress(), 11111);
             FtpClient client = new FtpClient(InetAddress.getLoopbackAddress(), 11111)) {
            Thread worker = new Thread(server);
            worker.start();
            List<String> list = client.list(".");
            for (String fileName : list) {
                System.out.println(fileName);
            }
            worker.interrupt();
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }

}
