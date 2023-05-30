package com.example.socket_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.socket_android.Model.SensorDTO;
import com.example.socket_android.Model.SocketInfo;
import com.example.socket_android.Parameter.SENSOR;

import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    SocketInfo socketInfo;
    SocketConnection socketConnection;
    SENSOR sensor;
    SensorDTO sensorVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        socketInfo = new SocketInfo();

        TextView connect_info = findViewById(R.id.connect_info);

        Button connect_btn = findViewById(R.id.connect_btn);
        Button lcd_btn = findViewById(R.id.lcd_button);

        EditText host_txt = findViewById(R.id.host);

        connect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketInfo.setHost(String.valueOf(host_txt.getText()));
                try {
                    socketConnection = new SocketConnection(socketInfo);
                    socketConnection.connect();
                    connect_info.setText("Connected");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        lcd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sensor = new SENSOR();
                    sensorVO = new SensorDTO();
                    sensorVO.setLcdUsage("True");
                    sensorVO.setLcdText("Socket Communication");
                    JSONObject sensorData = sensor.sendSensorData(sensorVO);
                    socketConnection.setSendData(sensorData);
                    socketConnection.SocketInfo(socketInfo);
                    Thread thread = new Thread(socketConnection.traffic);
                    thread.start();
                    Log.d("SocketHost", "lcd_btn SendThread Start");
                    thread.join(100);
                    Log.d("SocketHost", "lcd_btn SendThread Join " + socketConnection.getSendData());
                    Log.d("SocketHost", "Rcv: " + socketConnection.getRcvData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}