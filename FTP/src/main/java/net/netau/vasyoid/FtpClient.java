package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * FTP Client communicates with an FTP Server.
 * Two types of queries are supported -- list files in current directory and get a file.
 */
public class FtpClient implements AutoCloseable {

    private static int BUFFER_SIZE = 1024;

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    /**
     * Constructor with specified server address and port.
     * @param host server host address
     * @param port server port
     * @throws FtpException if cannot open a socket
     */
    public FtpClient(@NotNull InetAddress host, int port) throws FtpException {
        try {
            socket = new Socket(host, port);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new FtpException(e);
        }
    }

    /**
     * {@inheritDoc}
     * Sends a terminating query and closes the socket.
     * @throws FtpException if cannot send a terminating query or close the socket
     */
    @Override
    public void close() throws FtpException {
        try {
            output.writeInt(FtpServer.STOP_QUERY_TYPE);
            output.flush();
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            throw new FtpException(e);
        }
    }

    /**
     * Send an FTP query to the server.
     * @param type query type -- LIST_QUERY_TYPE or GET_QUERY_TYPE
     * @param path path on the server where th query will be proceeded
     * @throws FtpException if cannot send the query
     */
    private void sendQuery(int type, @NotNull String path) throws FtpException {
        try {
            output.writeInt(type);
            output.writeUTF(path);
            output.flush();
        } catch (IOException e) {
            throw new FtpException(e);
        }
    }

    /**
     * List files in a specified directory on the server.
     * @param path path to the directory
     * @return List of files
     * @throws FtpException if cannot communicate with server
     */
    @NotNull
    public List<MyFile> list(@NotNull String path) throws FtpException {
        sendQuery(FtpServer.LIST_QUERY_TYPE, path);
        try {
            List<MyFile> result = new ArrayList<>();
            int size = input.readInt();
            for (int i = 0; i < size; ++i) {
                String fileName = input.readUTF();
                boolean isDirectory = input.readBoolean();
                result.add(new MyFile(fileName, isDirectory));
            }
            return result;
        } catch (IOException e) {
            throw new FtpException(e);
        }
    }

    /**
     * Downloads a specified file from the server.
     * @param path path to the file
     * @return path to the downloaded file in the local storage
     * @throws FtpException if cannot communicate with server
     */
    @Nullable
    public File get(@NotNull String path) throws FtpException {
        sendQuery(FtpServer.GET_QUERY_TYPE, path);
        try {
            long size = input.readLong();
            File result = new File(new File(path).getName());
            if (size > 0) {
                byte[] buffer = new byte[BUFFER_SIZE];
                OutputStream fileOutput = new FileOutputStream(result);
                int readLength;
                while ((readLength = input.read(buffer, 0, (int) Math.min(size, BUFFER_SIZE))) > 0) {
                    fileOutput.write(buffer, 0, readLength);
                    size = Math.max(0, size - BUFFER_SIZE);
                }
                fileOutput.close();
                return result;
            }
            return null;
        } catch (IOException e) {
            throw new FtpException(e);
        }
    }

}
