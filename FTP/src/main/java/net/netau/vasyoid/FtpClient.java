package net.netau.vasyoid;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class FtpClient implements AutoCloseable {

    private static int BUFFER_SIZE = 1024;

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public FtpClient(InetAddress host, int port) throws FtpException {
        try {
            socket = new Socket(host, port);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new FtpException(e);
        }
    }

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

    private void sendQuery(int type, String path) throws FtpException {
        try {
            output.writeInt(type);
            output.writeUTF(path);
            output.flush();
        } catch (IOException e) {
            throw new FtpException(e);
        }
    }

    public List<String> list(String path) throws FtpException {
        sendQuery(FtpServer.LIST_QUERY_TYPE, path);
        try {
            List<String> result = new ArrayList<>();
            int size = input.readInt();
            for (int i = 0; i < size; ++i) {
                String fileName = input.readUTF();
                if (input.readBoolean()) {
                    fileName += "/";
                }
                result.add(fileName);
            }
            return result;
        } catch (IOException e) {
            throw new FtpException(e);
        }
    }

    public File get(String path) throws FtpException {
        sendQuery(FtpServer.GET_QUERY_TYPE, path);
        try {
            long size = input.readLong();
            File result = new File(new File(path).getName());
            if (size > 0) {
                byte[] buffer = new byte[BUFFER_SIZE];
                OutputStream fileOutput = new FileOutputStream(result);
                int readLength;
                while ((readLength = input.read(buffer, 0, (int)(size % BUFFER_SIZE))) > 0) {
                    fileOutput.write(buffer, 0, readLength);
                    size /= BUFFER_SIZE;
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
