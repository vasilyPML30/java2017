package net.netau.vasyoid;

import java.net.InetAddress;

public class ServerMain {
    public static void main(String[] args) throws Exception {
        try (FtpServer server = new FtpServer(InetAddress.getLoopbackAddress(), 11111)) {
            server.run();
        }
    }
}
