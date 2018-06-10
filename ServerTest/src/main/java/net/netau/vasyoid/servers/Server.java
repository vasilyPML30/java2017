package net.netau.vasyoid.servers;

import java.util.concurrent.CountDownLatch;

public abstract class Server {

    protected CountDownLatch completedRequests;
    protected TestResult testResult;

    public abstract TestResult run(int clientsCount, int requestsCount);

    public static Server newServer(ServerType type) {
        switch (type) {
            case SIMPLE:
                return new SimpleBlockingServer();
            case THREAD_POOL:
                return new ThreadPoolBlockingServer();
            default:
                return new NonBlockingServer();
        }
    }

    public static class TestResult {
        private long sortTime = 0;
        private long handleTime = 0;
        private int clientTime = 0;
        private int testsCount = 0;

        public synchronized void addTest() {
            testsCount++;
        }

        public synchronized void addSortTime(int time) {
            sortTime += time;
        }

        public synchronized void addHandleTime(int time) {
            handleTime += time;
        }

        public synchronized void setClientTime(int time) {
            clientTime = time;
        }

        public synchronized void addResult(TestResult result) {
            sortTime += result.sortTime;
            handleTime += result.handleTime;
            clientTime += result.clientTime;
            testsCount += result.testsCount;
        }

        public synchronized int getAverageSortTime() {
            return (int) (sortTime / testsCount);
        }

        public synchronized int getAverageHandleTime() {
            return (int) (handleTime / testsCount);
        }

        public synchronized int getAverageClientTime() {
            return clientTime;
        }
    }

    public enum ServerType {
        SIMPLE(11111), THREAD_POOL(12222), NON_BLOCKING(13333);

        private int port;

        ServerType(int port) {
            this.port = port;
        }

        public int getPort() {
            return port;
        }
    }
}
