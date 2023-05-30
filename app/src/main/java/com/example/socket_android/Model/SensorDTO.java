package com.example.socket_android.Model;

public class SensorDTO {
    private String led = "OFF";
    private String buzzerUsage = "False";
    private String buzzerHertz = "0";
    private String motorUsage = "False";
    private String motorAngle = "0";
    private String lcdUsage = "False";
    private String lcdText = "";
    private String sound = "False";
    private String light = "False";

    public SensorDTO(){}

    public SensorDTO(String led, String buzzerUsage, String buzzerHertz, String motorUsage,
                     String motorAngle, String lcdUsage, String lcdText, String sound, String light) {
        this.led = led;
        this.buzzerUsage = buzzerUsage;
        this.buzzerHertz = buzzerHertz;
        this.motorUsage = motorUsage;
        this.motorAngle = motorAngle;
        this.lcdUsage = lcdUsage;
        this.lcdText = lcdText;
        this.sound = sound;
        this.light = light;
    }

    public String getLed() {
        return led;
    }

    public void setLed(String led) {
        this.led = led;
    }

    public String getBuzzerUsage() {
        return buzzerUsage;
    }

    public void setBuzzerUsage(String buzzerUsage) {
        this.buzzerUsage = buzzerUsage;
    }

    public String getBuzzerHertz() {
        return buzzerHertz;
    }

    public void setBuzzerHertz(String buzzerHertz) {
        this.buzzerHertz = buzzerHertz;
    }

    public String getMotorUsage() {
        return motorUsage;
    }

    public void setMotorUsage(String motorUsage) {
        this.motorUsage = motorUsage;
    }

    public String getMotorAngle() {
        return motorAngle;
    }

    public void setMotorAngle(String motorAngle) {
        this.motorAngle = motorAngle;
    }

    public String getLcdUsage() {
        return lcdUsage;
    }

    public void setLcdUsage(String lcdUsage) {
        this.lcdUsage = lcdUsage;
    }

    public String getLcdText() {
        return lcdText;
    }

    public void setLcdText(String lcdText) {
        this.lcdText = lcdText;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }
}
