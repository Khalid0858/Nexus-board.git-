package com.himelz.nexusboard.network;

import java.io.*;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameServer {
    private ServerSocket serverSocket;
    private final int port;
    private final CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public GameServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("✅ Server started on port: " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("🎮 New client connected: " + socket.getInetAddress());

                ClientHandler handler = new ClientHandler(socket, this);
                clients.add(handler);
                new Thread(handler).start();

                // Example: notify all clients
                broadcast("A new player joined! Total players: " + clients.size());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            for (ClientHandler client : clients) {
                client.close();
            }
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            System.out.println("❌ Server stopped");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
}
