package net.netau.vasyoid;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class FtpServer implements AutoCloseable, Runnable {

    private ServerSocket serverSocket;
    private FtpException exception = null;

    public FtpServer(InetAddress addres, int port) throws FtpException {
        try {
            serverSocket = new ServerSocket(port, Integer.MAX_VALUE, addres);
        } catch (IOException e) {
            throw new FtpException(e);
        }
    }

    @Override
    public void close() throws FtpException {
        try {
            serverSocket.close();
        } catch (IOException e) {
            if (exception != null) {
                exception.addSuppressed(e);
            } else {
                exception = new FtpException(e);
            }
        }
        if (exception != null) {
            throw exception;
        }
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try (Socket socket = serverSocket.accept();
                 DataInputStream input = new DataInputStream(socket.getInputStream());
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                int queryType = input.readInt();
                if (queryType == 1) {
                    File directory = new File(input.readUTF());
                    String[] files = directory.list();
                    output.writeInt(files == null ? 0 : files.length);
                    if (files != null) {
                        for (String fileName : files) {
                            output.writeUTF(fileName);
                        }
                    }
                    output.flush();
                }
            } catch (IOException e) {
                exception = new FtpException(e);
            }
        }
    }
}
