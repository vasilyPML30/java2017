package net.netau.vasyoid;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class FtpServer implements AutoCloseable, Runnable {

    public static int LIST_QUERY_TYPE = 1;
    public static int GET_QUERY_TYPE = 2;
    public static int STOP_QUERY_TYPE = 3;

    private static int BUFFER_SIZE = 1024;


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
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(new Worker(socket));
                thread.start();
            } catch (IOException ignored) { // Server was closed
                break;
            }
        }
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
                while (!Thread.interrupted()) {
                    int queryType = input.readInt();
                    if (queryType == STOP_QUERY_TYPE) {
                        break;
                    } else if (queryType == LIST_QUERY_TYPE) {
                        File directory = new File(input.readUTF());
                        File[] files = directory.listFiles();
                        output.writeInt(files == null ? 0 : files.length);
                        if (files != null) {
                            for (File file : files) {
                                output.writeUTF(file.getName());
                                output.writeBoolean(file.isDirectory());
                            }
                        }
                        output.flush();
                    } else if (queryType == GET_QUERY_TYPE) {
                        File file = new File(input.readUTF());
                        output.writeLong(file.length());
                        FileInputStream fileInput = new FileInputStream(file);
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int readLength;
                        while ((readLength = fileInput.read(buffer, 0, BUFFER_SIZE)) > 0) {
                            output.write(buffer, 0, readLength);
                        }
                        fileInput.close();
                    }
                }
            } catch (IOException e) {
                FtpServer.this.exception = new FtpException(e);
            }
            try {
                close();
            } catch (FtpException e) {
                FtpServer.this.exception = e;
            }
        }
    }

}
