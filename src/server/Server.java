package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException{
        try (ServerSocket serverSocket = new ServerSocket(ConsoleHelper.readInt())) {
            System.out.println("Server is working");
            while (true) {
                new Handler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            while (true) {
                connection.send(new Message(MessageType.NAME_REQUEST));
                Message message = connection.receive();
                if (message.getType() != MessageType.USER_NAME || message.getData().isEmpty() || connectionMap.containsKey(message.getData())) {
                    continue;
                } else {
                    connectionMap.put(message.getData(), connection);
                    connection.send(new Message(MessageType.NAME_ACCEPTED));
                }
                return message.getData();
            }
        }

        private void notifyUsers(Connection connection, String userName) throws IOException {
            for(String name : connectionMap.keySet()) {
                if(name != userName) {
                    connection.send(new Message(MessageType.USER_ADDED, name));
                }
            }
        }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
            while (true) {
                Message message = connection.receive();
                if(message.getType() == MessageType.TEXT) {
                    sendBroadcastMessage(new Message(message.getType(), userName + ": " + message.getData()));
                } else {
                    ConsoleHelper.writeMessage("Ошибка! Неверный тип сообщений");
                }
            }
        }

        @Override
        public void run() {
            ConsoleHelper.writeMessage("Установлено соединение с удаленным адресом: " + socket.getRemoteSocketAddress());
            try (Connection connection = new Connection(socket)){
                String name = serverHandshake(connection);
                sendBroadcastMessage(new Message(MessageType.USER_ADDED, name));
                notifyUsers(connection, name);
                serverMainLoop(connection, name);
                for (Map.Entry<String, Connection> pair : connectionMap.entrySet()){
                    if(pair.getKey().equals(name)) {
                        connectionMap.remove(pair.getKey());
                        sendBroadcastMessage(new Message(MessageType.USER_REMOVED, name));
                    }
                }
                ConsoleHelper.writeMessage("Соеденение с удаленным адресом закрыто");
            } catch (ClassNotFoundException e) {
                ConsoleHelper.writeMessage("Ошибка при обмене с данными");
            } catch (IOException e) {
                ConsoleHelper.writeMessage("Ошибка при обмене с данными");
            }
        }
    }

    public static void sendBroadcastMessage(Message message) {
        try {
            for (Connection connection : connectionMap.values()) {
                connection.send(message);
            }
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Ошибка");
        }
    }
}
