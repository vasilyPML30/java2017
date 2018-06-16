package net.netau.vasyoid.clients;

import net.netau.vasyoid.utils.Protocol;
import net.netau.vasyoid.utils.Utils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Client implements Runnable {

    private static int index = 0;
    private final InetAddress address;
    private final int port;
    private final int elementsCount;
    private final int delta;
    private final int queriesCount;

    public Client(InetAddress address, int port, int elementsCount, int delta, int queriesCount) {
        this.address = address;
        this.port = port;
        this.elementsCount = elementsCount;
        this.delta = delta;
        this.queriesCount = queriesCount;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        Random random = new Random();
        try (Socket socket = new Socket(address, port);
            DataInputStream is = new DataInputStream(socket.getInputStream());
            DataOutputStream os = new DataOutputStream(socket.getOutputStream())) {
            for (int query = 0; query < queriesCount; ++query) {
                List<Integer> arr = new LinkedList<>();
                for (int i = 0; i < elementsCount; ++i) {
                    arr.add(random.nextInt());
                }
                Protocol.Array request = Protocol.Array.newBuilder()
                        .setElementsCount(elementsCount).addAllElement(arr).build();
                int size = request.getSerializedSize();
                os.writeInt(size);
                os.write(request.toByteArray());
                os.flush();
                Utils.readArray(is);
                try {
                    Thread.sleep(delta);
                } catch (InterruptedException ignored) { }
            }
            os.writeInt(-1);
            os.flush();
        } catch (IOException e) {
            System.out.println("Could not communicate with a server: " + e.getMessage());
        }
        ClientManager.addClientTime((System.currentTimeMillis() - startTime) / queriesCount);
        System.out.println("finished: " + index++);
    }
}
