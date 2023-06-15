package com.example.socket_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.socket_android.Model.SensorDTO;
import com.example.socket_android.Parameter.Sensor;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    /* 필수 */
    private SocketService socketService; // SocketService 객체 선언
    private boolean isBound = false; // 서비스가 바인드되었는지 여부를 나타내는 변수
    
    Sensor sensor;
    SensorDTO sensorVO;
    TextView connect_info;
    EditText host_txt;

    /* 필수 */
    private ServiceConnection connection = new ServiceConnection() { // 서비스 연결을 위한 ServiceConnection 객체 생성
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) { // 서비스가 연결되었을 때 호출되는 메소드
            SocketService.LocalBinder binder = (SocketService.LocalBinder) service; // IBinder 객체를
            // SocketService.LocalBinder 객체로 캐스팅
            socketService = binder.getService(); // SocketService 객체 가져오기
            isBound = true; // 서비스가 바인드되었음을 표시
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) { // 서비스가 연결이 끊겼을 때 호출되는 메소드
            isBound = false; // 서비스가 바인드되지 않았음을 표시
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 필수 */
        // SocketService와 연결하기 위한 Intent 생성
        Intent intent = new Intent(this, SocketService.class);
        // SocketService와 연결
        bindService(intent, connection, Context.BIND_AUTO_CREATE);


        connect_info = findViewById(R.id.connect_info);
        Button connect_btn = findViewById(R.id.connect_btn);
        Button lcd_btn = findViewById(R.id.lcd_button);
        Button next_btn = findViewById(R.id.next_button);

        host_txt = findViewById(R.id.host);

        // Socket 연결
        connect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String host = String.valueOf(host_txt.getText());
                try {
                    socketService.connect(host, 8080);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                    runOnUiThread(() -> connect_info.setText("Connected"));
            }
        });
        
        // LCD 데이터 전송
        lcd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sensor = new Sensor();
                    sensorVO = new SensorDTO();
                    sensorVO.setLcdUsage("True");
                    sensorVO.setLcdText("Socket Communication");
                    JSONObject sensorData = sensor.sendSensorData(sensorVO);
                    sendAndReceive(sensorData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 다음 페이지로 이동
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                /* 필수 */
                // runOnUiThread 필수
                runOnUiThread(()->startActivity(intent));
            }
        });
    }

    /* 필수 */
    ExecutorService executorService = Executors.newFixedThreadPool(2);
    private void sendAndReceive(JSONObject sensorData) {
        if (isBound) {
            // 스레드 풀에서 작업 실행
            executorService.submit(() -> {
                try {
                    // EditText에서 메시지를 가져와 SocketService의 send() 메소드 실행
//                    String message = String.valueOf(messageEditText.getText()); // messageEditText에서 텍스트를 가져와서 String으로 변환 후 message 변수에 할당
                    socketService.send(String.valueOf(sensorData)).get(); // 소켓 서비스를 통해 메시지를 전송하고, 전송 완료까지 대기한다.

                    // SocketService의 receive() 메소드 실행하여 받은 메시지를 TextView에 출력
                    String received = socketService.receive().get(); // 소켓 서비스로부터 데이터를 받아와서 received 변수에 저장한다.
//                    runOnUiThread(() -> receivedTextView.setText(received)); // UI 스레드에서 실행되도록 함. receivedTextView에 received 값을 설정하여 화면에 출력함.
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}