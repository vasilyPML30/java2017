package net.netau.vasyoid.servers;

import net.netau.vasyoid.clients.ClientManager;
import net.netau.vasyoid.utils.Protocol;
import net.netau.vasyoid.utils.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerManager {

    private Server.TestResult completeTask(Protocol.TestTask testTask,
                                           Server.ServerType serverType) {
        Server.TestResult result = new Server.TestResult();
        try (Socket socket = new Socket(InetAddress.getLoopbackAddress(),
                ClientManager.CLIENT_MANAGER_PORT);
             DataInputStream is = new DataInputStream(socket.getInputStream());
             DataOutputStream os = new DataOutputStream(socket.getOutputStream())) {
            Server server = Server.newServer(serverType);
            Thread serverThread = new Thread(() -> {
                result.addResult(server.run(testTask.getClientsCount(),
                        testTask.getRequestsCount()));
            });
            serverThread.start();
            Utils.writeMessage(testTask, os);
            serverThread.join();
            int clientTime = is.readInt();
            result.setClientTime(clientTime);
        } catch (IOException e) {
            System.out.println("Could not communicate with a client: " + e.getMessage());
        } catch (InterruptedException ignored) { }
        System.err.println("sort: " + result.getAverageSortTime());
        System.err.println("handle: " + result.getAverageHandleTime());
        System.err.println("client: " + result.getAverageClientTime());
        return result;
    }

    public List<Server.TestResult> run(Config config) {
        List<Server.TestResult> results = new ArrayList<>();
        for (int clientsCount = config.clientsCountMin;
             clientsCount <= config.clientsCountMax;
             clientsCount += config.clientsCountStride) {
            for (int elementsCount = config.elementsCountMin;
                 elementsCount <= config.elementsCountMax;
                 elementsCount += config.elementsCountStride) {
                for (int delta = config.deltaMin;
                     delta <= config.deltaMax;
                     delta += config.deltaStride) {
                    Protocol.TestTask testTask = Protocol.TestTask.newBuilder()
                            .setClientsCount(clientsCount)
                            .setArraySize(elementsCount)
                            .setDelta(delta)
                            .setPort(config.serverType.getPort())
                            .setRequestsCount(config.requestsCount)
                            .build();
                    results.add(completeTask(testTask, config.serverType));
                }
            }
        }
        return results;
    }

    public static void main(String[] args) {
        Config config = new Config(Server.ServerType.THREAD_POOL);
        config.setClientsCount(100);
        config.setRequestsCount(100);
        config.setElementsCount(1000);
        new ServerManager().run(config);
    }

    public static class Config {
        private int clientsCountMin = 100, clientsCountMax = 100, clientsCountStride = 1;
        private int elementsCountMin = 100, elementsCountMax = 100, elementsCountStride = 1;
        private int deltaMin = 100, deltaMax = 100, deltaStride = 100;
        private int requestsCount = 100;
        private Server.ServerType serverType;

        Config(Server.ServerType serverType) {
            this.serverType = serverType;
        }

        public void setClientsCount(int value) {
            clientsCountMin = clientsCountMax = value;
            clientsCountStride = 1;
        }

        public void setClientsCount(int min, int max, int stride) {
            clientsCountMin = min;
            clientsCountMax = max;
            clientsCountStride = stride;
        }

        public void setElementsCount(int value) {
            elementsCountMin = elementsCountMax = value;
            elementsCountStride = 1;
        }

        public void setElementsCount(int min, int max, int stride) {
            elementsCountMin = min;
            elementsCountMax = max;
            elementsCountStride = stride;
        }

        public void setDelta(int value) {
            deltaMin = deltaMax = value;
            deltaStride = 1;
        }

        public void setDelta(int min, int max, int stride) {
            deltaMin = min;
            deltaMax = max;
            deltaStride = stride;
        }

        public void setRequestsCount(int value) {
            requestsCount = value;
        }

        public void setServerType(Server.ServerType serverType) {
            this.serverType = serverType;
        }

    }
}
