package com.example.carlo.myapplication;

/**
 * Created by carlo on 29/03/2018.
 */

public class Dados {
    double latitude, logitude; //GPS
    float acelX,acelY,acelZ; //acelerometro



    public Dados(double latitude, double logitude, float acelX, float acelY, float acelZ) {

        this.latitude = latitude;
        this.logitude = logitude;
        this.acelX = acelX;
        this.acelY = acelY;
        this.acelZ = acelZ;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLogitude() {
        return logitude;
    }

    public void setLogitude(double logitude) {
        this.logitude = logitude;
    }

    public float getAcelX() {
        return acelX;
    }

    public void setAcelX(float acelX) {
        this.acelX = acelX;
    }

    public float getAcelY() {
        return acelY;
    }

    public void setAcelY(float acelY) {
        this.acelY = acelY;
    }

    public float getAcelZ() {
        return acelZ;
    }

    public void setAcelZ(float acelZ) {
        this.acelZ = acelZ;
    }
}
