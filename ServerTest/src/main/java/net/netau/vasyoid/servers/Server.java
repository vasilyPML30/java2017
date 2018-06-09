package net.netau.vasyoid.servers;

import java.util.concurrent.CountDownLatch;

public abstract class Server {

    protected static final int SIMPLE_BLOCKING_SERVER_PORT = 11111;
    protected static final int BLOCKING_THREAD_POOL_SERVER_PORT = 12222;
    protected static final int NON_BLOCKING_SERVER_PORT = 13333;

    protected CountDownLatch completedRequests;
    protected TestResult testResult;

    public abstract TestResult run(int clientsCount, int requestsCount);

    public static void main(String[] args) {
        Server server = new NonBlockingServer();
        server.run(100, 100);
    }

    public static class TestResult {
        private long sortTime = 0;
        private long handleTime = 0;
        private long clientTime = 0;
        private int clientsCount = 0;

        public synchronized void addClient() {
            clientsCount++;
        }

        public synchronized void addSortTime(int time) {
            sortTime += time;
        }

        public synchronized void addHandleTime(int time) {
            sortTime += time;
        }

        public synchronized void addClientTime(int time) {
            sortTime += time;
        }

        public synchronized void addResult(TestResult result) {
            sortTime += result.sortTime;
            handleTime += result.handleTime;
            clientTime += result.clientTime;
            clientsCount += result.clientsCount;
        }

        public synchronized int getAverageSortTime() {
            return (int) (sortTime / clientsCount);
        }

        public synchronized int getAverageHandleTime() {
            return (int) (handleTime / clientsCount);
        }

        public synchronized int getAverageClientTime() {
            return (int) (clientTime / clientsCount);
        }
    }
}
