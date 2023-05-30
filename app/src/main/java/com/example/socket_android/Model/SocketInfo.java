package com.example.socket_android.Model;

import java.net.Socket;

public class SocketInfo {
    private Socket socket;
    private String host;
    private int port = 8080;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
