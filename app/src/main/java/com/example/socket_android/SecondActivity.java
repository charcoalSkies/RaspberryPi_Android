package com.example.socket_android;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socket_android.Model.SensorDTO;
import com.example.socket_android.Parameter.Sensor;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SecondActivity extends AppCompatActivity {

    Sensor sensor;
    SensorDTO sensorVO;
    
    /* 필수 */
    private SocketService socketService;
    private boolean isBound = false;

    EditText messageEditText;
    TextView receivedTextView;

    /* 필수 */
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            SocketService.LocalBinder binder = (SocketService.LocalBinder) service;
            socketService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        /* 필수 */
        Intent intent = new Intent(this, SocketService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        Button sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sensor = new Sensor();
                    sensorVO = new SensorDTO();
                    sensorVO.setLed("True");
                    JSONObject sensorData = sensor.sendSensorData(sensorVO);
                    sendAndReceive(sensorData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }

    /* 필수 */
    ExecutorService executorService = Executors.newFixedThreadPool(2);
    private void sendAndReceive(JSONObject sensorData) {
        if (isBound) {
            // 스레드 풀에서 작업 실행
            executorService.submit(() -> {
                try {
                    socketService.send(String.valueOf(sensorData)).get();
                    String received = socketService.receive().get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
