package com.himelz.nexusboard.network;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final GameServer server;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket, GameServer server) {
        this.socket = socket;
        this.server = server;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String msg;
            while ((msg = in.readLine()) != null) {
                System.out.println("📩 Received: " + msg);
                server.broadcast(msg); // Forward to all players
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
