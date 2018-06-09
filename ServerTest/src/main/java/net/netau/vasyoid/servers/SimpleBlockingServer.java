package net.netau.vasyoid.servers;

import net.netau.vasyoid.Protocol;
import net.netau.vasyoid.Utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleBlockingServer extends BlockingServer {

    public SimpleBlockingServer() {
        try {
            serverSocket = new ServerSocket(SIMPLE_BLOCKING_SERVER_PORT, Integer.MAX_VALUE, ADDRESS);
        } catch (IOException e) {
            System.out.println("Could not create a server socket: " + e.getMessage());
        }
    }

    @Override
    protected void proceed(Socket socket, int requestsCount) {
        Worker worker = new Worker(socket, requestsCount);
        Thread workerThread = new Thread(worker);
        workerThread.start();
    }

    private class Worker implements Runnable {

        private Socket socket;
        private final int requestsCount;

        Worker(Socket socket, int requestsCount) {
            this.socket = socket;
            this.requestsCount = requestsCount;
        }

        @Override
        public void run() {
            try (DataInputStream input = new DataInputStream(socket.getInputStream());
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                for (int i = 0; i < requestsCount; ++i) {
                    Protocol.Array array = Utils.readArray(input);
                    long startTime = System.currentTimeMillis();
                    Protocol.Array result = Utils.sort(array, testResult);
                    testResult.addHandleTime((int) (System.currentTimeMillis() - startTime));
                    Utils.writeMessage(result, output);
                    completedRequests.countDown();
                }
            } catch (IOException e) {
                System.out.println("Could not communicate with a client: " + e.getMessage());
            }
        }
    }
}
