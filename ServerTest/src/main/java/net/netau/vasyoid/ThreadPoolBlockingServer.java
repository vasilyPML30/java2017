package net.netau.vasyoid;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolBlockingServer extends BlockingServer {

    private static final int THREAD_POOL_SIZE = 4;

    private final ExecutorService threadPool;

    public ThreadPoolBlockingServer() {
        threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        try {
            serverSocket = new ServerSocket(BLOCKING_THREAD_POOL_SERVER_PORT,
                    Integer.MAX_VALUE, ADDRESS);
        } catch (IOException e) {
            System.out.println("Could not create a socket: " + e.getMessage());
        }
    }

    @Override
    protected void proceed(Socket socket) {
        Worker worker = new Worker(socket);
        worker.start();
    }

    private class Worker extends Thread {

        private final Socket socket;
        private final ExecutorService responser = Executors.newSingleThreadExecutor();

        Worker(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (DataInputStream input = new DataInputStream(socket.getInputStream());
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                //noinspection InfiniteLoopStatement
                while (true) {
                    Request.Array array = readMessage(input);
                    threadPool.submit(() -> {
                        Request.Array result = sort(array);
                        responser.submit(() -> {
                            try {
                                writeMessage(result, output);
                            } catch (IOException e) {
                                System.out.println("Could not write a response: " + e.getMessage());
                            }
                        });

                    });
                }
            } catch (IOException e) {
                System.out.println("Could not communicate with a client: " + e.getMessage());
            }
        }
    }
}
