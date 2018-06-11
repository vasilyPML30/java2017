package net.netau.vasyoid.servers;

import net.netau.vasyoid.utils.Protocol;
import net.netau.vasyoid.utils.Utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleBlockingServer extends BlockingServer {

    public SimpleBlockingServer() {
        try {
            serverSocket = new ServerSocket(ServerType.SIMPLE.getPort(),
                    Integer.MAX_VALUE, ADDRESS);
        } catch (IOException e) {
            System.out.println("Could not create a server socket: " + e.getMessage());
        }
    }

    @Override
    protected void proceed(Socket socket) {
        Worker worker = new Worker(socket);
        Thread workerThread = new Thread(worker);
        workerThread.start();
    }

    private class Worker implements Runnable {

        private Socket socket;

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
                    Protocol.Array result = Utils.sort(array, testResult);
                    testResult.addHandleTime((int) (System.currentTimeMillis() - startTime));
                    completedRequests.countDown();
                    Utils.writeMessage(result, output);
                }
            } catch (IOException e) {
                System.out.println("Could not communicate with a client: " + e.getMessage());
            }
        }
    }
}
