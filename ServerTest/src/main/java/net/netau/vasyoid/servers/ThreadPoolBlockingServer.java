package net.netau.vasyoid.servers;

import net.netau.vasyoid.Protocol;
import net.netau.vasyoid.Utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class ThreadPoolBlockingServer extends BlockingServer {

    private static final int THREAD_POOL_SIZE = 4;

    private final ExecutorService threadPool;

    public ThreadPoolBlockingServer() {
        threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        try {
            serverSocket = new ServerSocket(BLOCKING_THREAD_POOL_SERVER_PORT,
                    Integer.MAX_VALUE, ADDRESS);
        } catch (IOException e) {
            System.out.println("Could not create a server socket: " + e.getMessage());
        }
    }

    @Override
    protected void proceed(Socket socket, int requestsCount) {
        Worker worker = new Worker(socket, requestsCount);
        worker.start();
    }

    private class Worker extends Thread {

        private final Socket socket;
        private final ExecutorService responser = Executors.newSingleThreadExecutor();
        private final int requeststCount;

        Worker(Socket socket, int requestsCount) {
            this.socket = socket;
            this.requeststCount = requestsCount;
        }

        @Override
        public void run() {
            try (DataInputStream input = new DataInputStream(socket.getInputStream());
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                for (int i = 0; i < requeststCount; ++i) {
                    Protocol.Array array = Utils.readArray(input);
                    long startTime = System.currentTimeMillis();
                    threadPool.submit(() -> {
                        Protocol.Array result = Utils.sort(array, testResult);
                        responser.submit(() -> {
                            testResult.addHandleTime(
                                    (int) (System.currentTimeMillis() - startTime));
                            try {
                                Utils.writeMessage(result, output);
                            } catch (IOException e) {
                                System.out.println("Could not write a response: "
                                        + e.getMessage());
                            }
                        });
                    });
                    completedRequests.countDown();
                }
            } catch (IOException e) {
                System.out.println("Could not communicate with a client: " + e.getMessage());
            }
        }
    }
}
