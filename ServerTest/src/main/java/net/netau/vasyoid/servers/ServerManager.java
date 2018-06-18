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
import java.util.Scanner;

public class ServerManager {

    private static Server.TestResult completeTask(Protocol.TestTask testTask, Server server,
                                                  InetAddress clientAddress) {
        Server.TestResult result = new Server.TestResult();
        try (Socket socket = new Socket(clientAddress,
                ClientManager.CLIENT_MANAGER_PORT);
             DataInputStream is = new DataInputStream(socket.getInputStream());
             DataOutputStream os = new DataOutputStream(socket.getOutputStream())) {
            Thread serverThread = new Thread(() ->
                    result.addResult(server.run(testTask.getClientsCount(),
                    testTask.getRequestsCount()))
            );
            serverThread.start();
            Utils.writeMessage(testTask, os);
            serverThread.join();
            int clientTime = is.readInt();
            result.setClientTime(clientTime);
        } catch (IOException e) {
            System.out.println("Could not communicate with a client: " + e.getMessage());
        } catch (InterruptedException ignored) { }
        return result;
    }

    public static List<Server.TestResult> run(Config config, InetAddress clientAddress) {
        List<Server.TestResult> results = new ArrayList<>();
        Server server = Server.newServer(config.serverType);
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
                    results.add(completeTask(testTask, server, clientAddress));
                }
            }
        }
        server.close();
        return results;
    }

    public static class Config {

        private int clientsCountMin = 100, clientsCountMax = 100, clientsCountStride = 1;
        private int elementsCountMin = 100, elementsCountMax = 100, elementsCountStride = 1;
        private int deltaMin = 100, deltaMax = 100, deltaStride = 100;
        private int parameterMin = 100, parameterMax = 100, parameterStride = 1;
        private int requestsCount = 100;
        private Server.ServerType serverType;

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            result.append("Server ").append(serverType);
            result.append("\nX ").append(requestsCount);
            result.append("\nN ").append(elementsCountMin);
            if (elementsCountMin != elementsCountMax) {
                result.append(" .. ")
                        .append(elementsCountMax)
                        .append(", ")
                        .append(elementsCountStride);
            }
            result.append("\nM ").append(clientsCountMin);
            if (clientsCountMin != clientsCountMax) {
                result.append(" .. ")
                        .append(clientsCountMax)
                        .append(", ")
                        .append(clientsCountStride);
            }
            result.append("\nD ").append(deltaMin);
            if (deltaMin != deltaMax) {
                result.append(" .. ")
                        .append(deltaMax)
                        .append(", ")
                        .append(deltaStride);
            }
            result.append("\n");
            return result.toString();
        }

        public Config(Server.ServerType serverType) {
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

        public Server.ServerType getServerType() {
            return serverType;
        }

        private int[] parseValues(String string) {
            int[] result = new int[3];
            if (string.contains("..")) {
                String[] parts = string.split(",|\\.|\\s");
                result[0] = Integer.parseInt(parts[1]);
                result[1] = Integer.parseInt(parts[5]);
                result[2] = Integer.parseInt(parts[7]);
                parameterMin = result[0];
                parameterMax = result[1];
                parameterStride = result[2];
            } else {
                result[0] = result[1] = Integer.parseInt(string.substring(2));
                result[2] = 1;
            }
            return result;
        }

        public void read(Scanner in) {
            in.nextLine();
            int[] values = parseValues(in.nextLine());
            setRequestsCount(values[0]);
            values = parseValues(in.nextLine());
            setElementsCount(values[0], values[1], values[2]);
            values = parseValues(in.nextLine());
            setClientsCount(values[0], values[1], values[2]);
            values = parseValues(in.nextLine());
            setDelta(values[0], values[1], values[2]);
        }

        public int getParameterMin() {
            return parameterMin;
        }

        public int getParameterStride() {
            return parameterStride;
        }
    }
}
