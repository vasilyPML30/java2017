package net.netau.vasyoid.clients;

import net.netau.vasyoid.Protocol;
import net.netau.vasyoid.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientsManager {

    public static int CLIENT_MANAGER_PORT = 111111;
    private static long averageClientTime;

    public static void addClientTime(long time) {
        averageClientTime += time;
    }

    private static void proceed(Socket socket) {
        try (DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            Protocol.TestTask task = Utils.readTestTask(input);
            Thread[] threads = new Thread[task.getClientsCount()];
            averageClientTime = 0;
            for (int i = 0; i < task.getClientsCount(); ++i) {
                Client client = new Client(socket.getInetAddress(),
                        task.getPort(), task.getArraySize(), task.getDelta(),
                        task.getRequestsCount());
                threads[i] = new Thread(client);
            }
            averageClientTime /= task.getClientsCount();
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException ignored) { }
            }
            output.writeInt((int) averageClientTime);
        } catch (IOException e) {
            System.out.println("Could not communicate: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket =
                     new ServerSocket(CLIENT_MANAGER_PORT, Integer.MAX_VALUE,
                             InetAddress.getLoopbackAddress())) {
            //noinspection InfiniteLoopStatement
            while (true) {
                Socket socket = serverSocket.accept();
                proceed(socket);
            }
        } catch (IOException e) {
            System.out.println("Could not create a server socket: " + e.getMessage());
        }
    }
}
