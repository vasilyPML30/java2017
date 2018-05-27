package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * FTP Server proceeds FTP queries.
 * Two types of queries are supported -- list files in current directory and get a file.
 */
public class FtpServer implements AutoCloseable, Runnable {

    public static final int LIST_QUERY_TYPE = 1;
    public static final int GET_QUERY_TYPE = 2;
    public static final int STOP_QUERY_TYPE = 3;

    private static final int BUFFER_SIZE = 1024;


    private final ServerSocket serverSocket;
    private FtpException exception = null;

    /**
     * Constructor with specified server address and port.
     * @param address network connection address to listen
     * @param port port to listen
     * @throws FtpException if cannot open a socket
     */
    public FtpServer(@NotNull InetAddress address, int port) throws FtpException {
        try {
            serverSocket = new ServerSocket(port, Integer.MAX_VALUE, address);
        } catch (IOException e) {
            throw new FtpException(e);
        }
    }

    /**
     * {@inheritDoc}
     * Closes the socket.
     * @throws FtpException if cannot close the socket
     */
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

    /**
     * {@inheritDoc}
     * Main loop. Accepts incoming connection queries and creates new threads to proceed them.
     */
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

    /**
     * Worker receives FTP queries and respond to them.
     */
    private class Worker implements Runnable {

        private Socket socket;

        public Worker(@NotNull Socket socket) {
            this.socket = socket;
        }

        /**
         * {@inheritDoc}
         * Main loop.
         */
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
                        if (file.exists()) {
                            FileInputStream fileInput = new FileInputStream(file);
                            byte[] buffer = new byte[BUFFER_SIZE];
                            int readLength;
                            while ((readLength = fileInput.read(buffer, 0, BUFFER_SIZE)) > 0) {
                                output.write(buffer, 0, readLength);
                            }
                            fileInput.close();
                        }
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
