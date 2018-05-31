package net.netau.vasyoid;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

public abstract class Server {


    protected static final int THREAD_POOL_SIZE = 4;
    protected static final int BLOCKING_SERVER_PORT = 1111;
    protected static final int BLOCKING_THREAD_POOL_SERVER_PORT = 2222;
    protected static final int NONBLOCKING_THREAD_POOL_SERVER_PORT = 3333;
    protected static final InetAddress ADDRESS = InetAddress.getLoopbackAddress();
    protected ServerSocket serverSocket;

    public void run() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                proceed(socket);
            }
        } catch (IOException e) {
            System.out.println("could not establish a connection: " + e.getMessage());
        }
    }

    protected abstract void proceed(Socket socket);

    protected static void sort(List<Integer> list) {
        for (int i = 0; i < list.size(); ++i) {
            for (int j = i + 1; j < list.size(); ++j) {
                if (list.get(i) > list.get(j)) {
                    Collections.swap(list, i, j);
                }
            }
        }
    }
}
