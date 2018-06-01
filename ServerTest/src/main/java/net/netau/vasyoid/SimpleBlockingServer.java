package net.netau.vasyoid;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleBlockingServer extends BlockingServer {

    public SimpleBlockingServer() {
        try {
            serverSocket = new ServerSocket(SIMPLE_BLOCKING_SERVER_PORT, Integer.MAX_VALUE, ADDRESS);
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
            try (DataInputStream input = new DataInputStream(socket.getInputStream());
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                //noinspection InfiniteLoopStatement
                while (true) {
                    //noinspection ResultOfMethodCallIgnored
                    Request.Array array = readMessage(input);
                    Request.Array result = sort(array);
                    writeMessage(result, output);
                }
            } catch (IOException e) {
                System.out.println("Could not communicate with a client: " + e.getMessage());
            }
        }
    }

}
