package net.netau.vasyoid;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Client {

    public static void main(String[] args) throws IOException {
        Random random = new Random();
        Socket socket = new Socket(InetAddress.getLoopbackAddress(), 13333);
        DataInputStream is = new DataInputStream(socket.getInputStream());
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        for (int i = 0; i < 2; ++i) {
            List<Integer> arr = new LinkedList<>();
            System.out.print("\ngenerated: ");
            for (int j = 0; j < 10; ++j) {
                arr.add(random.nextInt(90) + 10);
                System.out.print(arr.get(j) + " ");
            }
            Request.Array request = Request.Array.newBuilder().addAllElement(arr).build();
            int size = request.getSerializedSize();
            byte[] data = new byte[size];
            os.writeInt(size);
            os.write(request.toByteArray());
            os.flush();
            size = is.readInt();
            data = new byte[size];
            is.read(data);
            Request.Array response = Request.Array.parseFrom(data);
            System.out.print("\nsorted:    ");
            for (int x : response.getElementList()) {
                System.out.print(x + " ");
            }
        }
        os.writeInt(-1);
        os.flush();
        socket.close();
    }

}
