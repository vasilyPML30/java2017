package net.netau.vasyoid;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Client {

    public static void main(String[] args) throws IOException {
        Random random = new Random();
        List<Integer> arr = new LinkedList<>();
        for (int i = 0; i < 10; ++i) {
            arr.add(random.nextInt(100));
        }
        Request.Array request = Request.Array.newBuilder().addAllElement(arr).build();
        Socket socket = new Socket(InetAddress.getLoopbackAddress(), 1111);
        OutputStream os = socket.getOutputStream();
        request.writeTo(os);
        os.flush();
        os.close();
        socket.close();
    }

}
