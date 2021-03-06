package net.netau.vasyoid.servers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public abstract class BlockingServer extends Server {

    protected static final InetAddress ADDRESS = InetAddress.getLoopbackAddress();

    protected ServerSocket acceptor;

    @Override
    public TestResult run(int clientsCount, int requestsCount) {
        testResult = new TestResult();
        completedRequests = new CountDownLatch(clientsCount * requestsCount);
        try {
            for (int i = 0; i < clientsCount; ++i) {
                Socket socket = acceptor.accept();
                proceed(socket);
            }
        } catch (IOException e) {
            System.out.println("Could not establish a connection: " + e.getMessage());
        }
        try {
            completedRequests.await();
        } catch (InterruptedException ignored) { }
        return testResult;
    }

    protected abstract void proceed(Socket socket);

    @Override
    public void close() {
        try {
            acceptor.close();
        } catch (IOException e) {
            System.out.println("Could not close a server socket: " + e.getMessage());
        }
    }
}
