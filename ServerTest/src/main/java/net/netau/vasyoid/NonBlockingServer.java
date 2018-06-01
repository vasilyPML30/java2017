package net.netau.vasyoid;

import com.google.protobuf.InvalidProtocolBufferException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

public class NonBlockingServer extends Server {

    private static final int THREAD_POOL_SIZE = 4;

    private ServerSocketChannel acceptor;
    private final Object readMutex = new Object();
    private final Object writeMutex = new Object();
    private Selector readSelector;
    private Selector writeSelector;
    private final ExecutorService threadPool;

    public NonBlockingServer() {
        threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        try {
            acceptor = ServerSocketChannel.open();
            acceptor.bind(new InetSocketAddress(NON_BLOCKING_SERVER_PORT));
            readSelector = Selector.open();
            writeSelector = Selector.open();
        } catch (IOException e) {
            System.out.println("Could not create a socket: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        ReadWorker readWorker = new ReadWorker();
        WriteWorker writeWorker = new WriteWorker();
        readWorker.start();
        writeWorker.start();
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                SocketChannel channel = acceptor.accept();
                channel.configureBlocking(false);
                //noinspection SynchronizeOnNonFinalField
                //synchronized (readMutex) {
                    channel.register(readSelector, SelectionKey.OP_READ, new Client());
                //}
            }
        } catch (IOException e) {
            System.out.println("Could not create a socket channel: " + e.getMessage());
        }
    }

    private class ReadWorker extends Thread {

        @Override
        public void run() {
            //noinspection InfiniteLoopStatement
                try {
                    while (true) {
                        //noinspection SynchronizeOnNonFinalField
                        synchronized (readMutex) {
                            if (readSelector.selectNow() == 0) {
                                continue;
                            }
                            Set<SelectionKey> readyChannelsKeys = readSelector.selectedKeys();
                            Iterator<SelectionKey> keyIterator = readyChannelsKeys.iterator();
                            while (keyIterator.hasNext()) {
                                SelectionKey key = keyIterator.next();
                                keyIterator.remove();
                                Client client = (Client) key.attachment();
                                SocketChannel channel = (SocketChannel) key.channel();
                                if (!channel.isOpen()) {
                                    key.cancel();
                                    continue;
                                }
                                if (!client.read(channel)) {
                                    continue;
                                }
                                key.cancel();
                                threadPool.submit(() -> {
                                    client.prepareResponse(sort(client.array));
                                    try {
                                        //noinspection SynchronizeOnNonFinalField
                                        synchronized (writeMutex) {
                                            channel.register(writeSelector, SelectionKey.OP_WRITE, client);
                                        }
                                    } catch (ClosedChannelException e) {
                                        System.out.println("Could not register a channel: " + e.getMessage());
                                    }
                                });
                            }
                            readyChannelsKeys.clear();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error working with channels: " + e.getMessage());
                }
        }
    }

    private class WriteWorker extends Thread {

        @Override
        public void run() {
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    //noinspection SynchronizeOnNonFinalField
                    synchronized (writeMutex) {
                        if (writeSelector.selectNow() == 0) {
                            continue;
                        }
                        Set<SelectionKey> readyChannelsKeys = writeSelector.selectedKeys();
                        Iterator<SelectionKey> keyIterator = readyChannelsKeys.iterator();
                        while (keyIterator.hasNext()) {
                            SelectionKey key = keyIterator.next();
                            keyIterator.remove();
                            Client client = (Client) key.attachment();
                            SocketChannel channel = (SocketChannel) key.channel();
                            if (!client.write(channel)) {
                                continue;
                            }
                            key.cancel();
                            //noinspection SynchronizeOnNonFinalField
                            synchronized (readMutex) {
                                channel.register(readSelector, SelectionKey.OP_READ, client);
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error working with channels: " + e.getMessage());
                }
            }
        }
    }

    private static class Client {

        private ByteBuffer serializedData = null;
        private Request.Array array = null;

        public void readSize(SocketChannel channel) {
            ByteBuffer size = ByteBuffer.allocate(4);
            try {
                while (size.remaining() > 0) {
                    channel.read(size);
                }
            } catch (IOException e) {
                System.out.println("Could not read from a channel: " + e.getMessage());
            }
            size.flip();
            int messageSize = size.getInt();
            if (messageSize < 0) {
                try {
                    channel.close();
                } catch (IOException e) {
                    System.out.println("Could not close a socket: " + e.getMessage());
                }
            } else {
                serializedData = ByteBuffer.allocate(messageSize);
            }
        }

        public void writeSize(SocketChannel channel) {
            ByteBuffer size = ByteBuffer.allocate(4);
            size.putInt(serializedData.capacity());
            size.flip();
            try {
                while (size.remaining() > 0) {
                    channel.write(size);
                }
            } catch (IOException e) {
                System.out.println("Could not write to a channel: " + e.getMessage());
            }
        }

        public void prepareResponse(Request.Array array) {
            this.array = array;
            serializedData = ByteBuffer.allocate(array.getSerializedSize());
            serializedData.put(array.toByteArray());
            serializedData.flip();
        }

        public boolean read(SocketChannel channel) {
            if (serializedData == null) {
                readSize(channel);
                return false;
            }
            try {
                channel.read(serializedData);
            } catch (IOException e) {
                System.out.println("Could not read data from a channel: " + e.getMessage());
            }
            if (serializedData.remaining() == 0) {
                serializedData.flip();
                try {
                    array = Request.Array.parseFrom(serializedData.array());
                    serializedData.clear();
                } catch (InvalidProtocolBufferException e) {
                    System.out.println("Invalid protocol buffer: " + e.getMessage());
                }
                return true;
            }
            return false;
        }

        public boolean write(SocketChannel channel) {
            if (array != null) {
                writeSize(channel);
                array = null;
                return false;
            }
            try {
                channel.write(serializedData);
            } catch (IOException e) {
                System.out.println("Could not write data to a channel: " + e.getMessage());
            }
            if (serializedData.remaining() == 0) {
                serializedData = null;
                array = null;
                return true;
            }
            return false;
        }
    }
}
