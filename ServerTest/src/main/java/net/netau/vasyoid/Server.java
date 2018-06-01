package net.netau.vasyoid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Server {

    protected static final int SIMPLE_BLOCKING_SERVER_PORT = 11111;
    protected static final int BLOCKING_THREAD_POOL_SERVER_PORT = 12222;
    protected static final int NON_BLOCKING_SERVER_PORT = 13333;

    public abstract void run();

    protected static Request.Array sort(Request.Array array) {
        List<Integer> list = new ArrayList<>(array.getElementList());
        for (int i = 0; i < list.size(); ++i) {
            for (int j = i + 1; j < list.size(); ++j) {
                if (list.get(i) > list.get(j)) {
                    Collections.swap(list, i, j);
                }
            }
        }
        return Request.Array.newBuilder().addAllElement(list).build();
    }

    public static void main(String[] args) {
        Server server = new NonBlockingServer();
        server.run();
    }

}
