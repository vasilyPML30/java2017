package net.netau.vasyoid;

import com.google.protobuf.GeneratedMessageV3;
import net.netau.vasyoid.servers.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utils {

    public static Protocol.Array sort(Protocol.Array array, Server.TestResult result) {
        List<Integer> list = new ArrayList<>(array.getElementList());
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < list.size(); ++i) {
            for (int j = i + 1; j < list.size(); ++j) {
                if (list.get(i) > list.get(j)) {
                    Collections.swap(list, i, j);
                }
            }
        }
        result.addSortTime((int) (System.currentTimeMillis() - startTime));
        return Protocol.Array.newBuilder()
                .setElementsCount(list.size()).addAllElement(list).build();
    }

    private static byte[] readMessage(DataInputStream input) throws IOException {
        int messageSize = input.readInt();
        byte[] data = new byte[messageSize];
        //noinspection ResultOfMethodCallIgnored
        input.read(data);
        return data;
    }

    public static Protocol.Array readArray(DataInputStream input) throws IOException {
        return Protocol.Array.parseFrom(readMessage(input));
    }

    public static Protocol.TestTask readTestTask(DataInputStream input) throws IOException {
        return Protocol.TestTask.parseFrom(readMessage(input));
    }

    public static void writeMessage(GeneratedMessageV3 array, DataOutputStream output)
            throws IOException {
        output.writeInt(array.getSerializedSize());
        //noinspection ResultOfMethodCallIgnored
        output.write(array.toByteArray());
        output.flush();
    }
}
