package com.example.socket_android;

import android.system.ErrnoException;
import android.util.Log;
import com.google.gson.Gson;
import com.example.socket_android.Model.SocketInfo;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class SocketConnection extends Thread{
    private Socket socket;
    private SocketInfo socketInfo;
    private String RcvData;
//    private String SendData;
    private JSONObject jsonData;
    private PrintWriter pw;
    private BufferedReader br;

    public SocketConnection(SocketInfo socketInfo) throws IOException {
        this.socketInfo = socketInfo;
    }

    public Socket getConnection() {
        return this.socket;
    }

    public void SocketInfo(SocketInfo socketInfo) {
        this.socketInfo = socketInfo;
    }

    public String getRcvData(){ return this.RcvData; }
    public void setRcvData(String RcvData){ this.RcvData = RcvData; }

    public JSONObject getSendData(){ return this.jsonData; }
    public void setSendData(JSONObject jsonData){ this.jsonData = jsonData; }



    public SocketInfo getSocketInfo(){ return this.socketInfo; }

    public void connect() {
        this.socket = new Socket();
            new Thread(() -> {
                try { socket.connect(new InetSocketAddress(socketInfo.getHost(), socketInfo.getPort())); }
                catch (Exception e) { e.printStackTrace(); }
            }).start();
        Log.d("SocketHost", "SocketHost: " + socketInfo.getHost() + ":" + socketInfo.getPort());
    }

    Runnable traffic = new Runnable() {
        @Override
        public void run() {
            try {
                pw = new PrintWriter(socket.getOutputStream());
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                pw.println(jsonData.toString()+'\0');
                pw.flush();

                InputStream inputStream = socket.getInputStream();
                byte[] byteRcv = new byte[512];
                int readByteCount = inputStream.read(byteRcv);
                setRcvData(new String(byteRcv, 0, readByteCount, "UTF-8"));
                Log.d("SocketHost", "rcvThread: " + getRcvData());
                Arrays.fill(byteRcv, (byte) 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Runnable Rcv = new Runnable() {
        @Override
        public void run() {
            try{
                InputStream inputStream = socket.getInputStream();
                byte[] byteRcv = new byte[512];
                int readByteCount = inputStream.read(byteRcv);
                setRcvData(new String(byteRcv, 0, readByteCount, "UTF-8"));
//                Log.d("SocketHost", "rcvThread: " + getRcvData());
                Arrays.fill(byteRcv, (byte) 0);
            }catch (Exception e){e.printStackTrace();}
        }
    };
}