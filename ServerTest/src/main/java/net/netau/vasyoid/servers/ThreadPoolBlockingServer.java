package net.netau.vasyoid.servers;

import net.netau.vasyoid.utils.Protocol;
import net.netau.vasyoid.utils.Utils;

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
            serverSocket = new ServerSocket(ServerType.THREAD_POOL.getPort(),
                    Integer.MAX_VALUE, ADDRESS);
        } catch (IOException e) {
            System.out.println("Could not create a server socket: " + e.getMessage());
        }
    }

    @Override
    public TestResult run(int clientsCount, int requestsCount) {
        TestResult result = super.run(clientsCount, requestsCount);
        threadPool.shutdown();
        return result;
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
                while (true) {
                    Protocol.Array array = Utils.readArray(input);
                    if (array == null) {
                        break;
                    }
                    testResult.addTest();
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
