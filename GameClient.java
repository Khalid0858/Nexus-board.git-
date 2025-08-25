package com.himelz.nexusboard.network;

import java.io.*;
import java.net.Socket;

public class GameClient {
    private final String host;
    private final int port;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public GameClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() {
        try {
            socket = new Socket(host, port);
            System.out.println("✅ Connected to server: " + host + ":" + port);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Thread to listen server messages
            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        System.out.println("📥 From server: " + msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public void disconnect() {
        try {
            if (socket != null) socket.close();
            System.out.println("❌ Disconnected from server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
