package net.netau.vasyoid;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BlockingServer extends Server {

    BlockingServer() {
        try {
            serverSocket = new ServerSocket(BLOCKING_SERVER_PORT, Integer.MAX_VALUE, ADDRESS);
        } catch (IOException e) {
            System.out.println("Could not create a socket: " + e.getMessage());
        }
    }

    @Override
    protected void proceed(Socket socket) {
        new Thread(new Worker(socket)).start();
    }

    private static class Worker implements Runnable {

        Socket socket;

        Worker(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (InputStream input = socket.getInputStream();
                 OutputStream output = socket.getOutputStream()) {
                //noinspection InfiniteLoopStatement
                while (true) {
                    //noinspection ResultOfMethodCallIgnored
                    input.skip(4);
                    Request.Array array = Request.Array.parseFrom(input);
                    sort(array.getElementList());
                    array.writeTo(output);
                }
            } catch (IOException e) {
                System.out.println("Could not communicate with a client: " + e.getMessage());
            }
        }
    }

}
