package net.netau.vasyoid;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BlockingThreadPoolServer extends Server {

    private final ExecutorService threadPool;

    BlockingThreadPoolServer() {
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

    }

    private class Worker extends Thread {

        private final Socket socket;

        Worker(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (InputStream input = socket.getInputStream();
                 OutputStream output = socket.getOutputStream()) {
                ExecutorService responser = Executors.newSingleThreadExecutor();
                //noinspection InfiniteLoopStatement
                while (true) {
                    //noinspection ResultOfMethodCallIgnored
                    input.skip(4);
                    Request.Array array = Request.Array.parseFrom(input);
                    Future<?> task = threadPool.submit(() -> sort(array.getElementList()));
                    task.get();
                    responser.submit(() -> {
                        try {
                            array.writeTo(output);
                        } catch (IOException e) {
                            System.out.println("Could not write a response: " + e.getMessage());
                        }
                    });
                }
            } catch (IOException e) {
                System.out.println("Could not communicate with a client: " + e.getMessage());
            } catch (ExecutionException e) {
                System.out.println("Could not sort an array: " + e.getMessage());
            } catch (InterruptedException e) {
                System.out.println("Execution has been interrupted: ");
            }
        }
    }
}
