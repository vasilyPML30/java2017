package net.netau.vasyoid;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class BlockingServer extends Server {

    protected static final InetAddress ADDRESS = InetAddress.getLoopbackAddress();
    protected ServerSocket serverSocket;

    @Override
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

    protected static Request.Array readMessage(DataInputStream input) throws IOException {
        int messageSize = input.readInt();
        byte[] data = new byte[messageSize];
        //noinspection ResultOfMethodCallIgnored
        input.read(data);
        return Request.Array.parseFrom(data);
    }

    protected static void writeMessage(Request.Array array, DataOutputStream output)
            throws IOException {
        output.writeInt(array.getSerializedSize());
        //noinspection ResultOfMethodCallIgnored
        output.write(array.toByteArray());
        output.flush();
    }

    protected abstract void proceed(Socket socket);

}
