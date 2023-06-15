package com.example.socket_android.Parameter;

import com.example.socket_android.Model.SensorDTO;

import org.json.JSONObject;

import java.util.HashMap;

public class Sensor {
    public JSONObject sendSensorData(SensorDTO sensorVO){
        HashMap<String, Object> headerMap = new HashMap<>();
        HashMap<String, Object> bodyMap = new HashMap<>();
        HashMap<String, Object> Buzzer = new HashMap<>();
        HashMap<String, Object> Motor = new HashMap<>();
        HashMap<String, Object> Lcd = new HashMap<>();

        Buzzer.put("usage", sensorVO.getBuzzerUsage());
        Buzzer.put("hertz", sensorVO.getBuzzerHertz());

        Motor.put("usage", sensorVO.getMotorUsage());
        Motor.put("angle", sensorVO.getMotorAngle());

        Lcd.put("usage", sensorVO.getLcdUsage());
        Lcd.put("text", sensorVO.getLcdText());

        bodyMap.put("LED", sensorVO.getLed());
        bodyMap.put("BUZZER", Buzzer);
        bodyMap.put("MOTOR", Motor);
        bodyMap.put("LCD", Lcd);
        bodyMap.put("SOUND", sensorVO.getSound());
        bodyMap.put("LIGHT", sensorVO.getLight());

        headerMap.put("data", bodyMap);
        headerMap.put("status", "success");

        JSONObject jsonObject = new JSONObject(headerMap);
        return jsonObject;
    }
}
