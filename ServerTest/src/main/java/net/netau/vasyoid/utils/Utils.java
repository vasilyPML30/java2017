package net.netau.vasyoid.utils;

import com.google.protobuf.GeneratedMessageV3;
import net.netau.vasyoid.servers.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
        if (messageSize < 0) {
            return null;
        }
        byte[] data = new byte[messageSize];
        int totalRead = 0;
        while (totalRead < messageSize) {
            totalRead += input.read(data, totalRead, messageSize - totalRead);
        }
        return data;
    }

    public static Protocol.Array readArray(DataInputStream input) throws IOException {
        byte[] data = readMessage(input);
        if (data == null) {
            return null;
        }
        return Protocol.Array.parseFrom(data);
    }

    public static Protocol.TestTask readTestTask(DataInputStream input) throws IOException {
        byte[] data = readMessage(input);
        if (data == null) {
            return null;
        }
        return Protocol.TestTask.parseFrom(data);
    }

    public static void writeMessage(GeneratedMessageV3 message, DataOutputStream output)
            throws IOException {
        output.writeInt(message.getSerializedSize());
        //noinspection ResultOfMethodCallIgnored
        output.write(message.toByteArray());
        output.flush();
    }
}
