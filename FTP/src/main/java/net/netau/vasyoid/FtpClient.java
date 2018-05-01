package net.netau.vasyoid;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class FtpClient implements AutoCloseable {

    private Socket socket;

    public FtpClient(InetAddress host, int port) throws FtpException {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            throw new FtpException(e);
        }
    }

    @Override
    public void close() throws FtpException {
        try {
            socket.close();
        } catch (IOException e) {
            throw new FtpException(e);
        }
    }

    public List<String> list(String path) throws FtpException {
        try (DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             DataInputStream input = new DataInputStream(socket.getInputStream())) {
            output.writeInt(1);
            output.writeUTF(path);
            output.flush();
            List<String> result = new ArrayList<>();
            int size = input.readInt();
            for (int i = 0; i < size; ++i) {
                result.add(input.readUTF());
            }
            return result;
        } catch (IOException e) {
            throw new FtpException(e);
        }
    }
}
