package com.example.carlo.myapplication;

/**
 * Created by carlo on 29/03/2018.
 */

public class Dados {
    String lat ;
    String lng ;
    String alti;
    String timestamp ;
    String x_acc ;
    String y_acc ;
    String z_acc ;
    String x_gyro ;
    String y_gyro ;
    String z_gyro ;
    String x_m ;
    String y_m ;
    String z_m ;
    String lumi;
    String activity;


    public Dados(String lat, String lng, String alti, String timestamp, String x_acc, String y_acc, String z_acc, String x_gyro, String y_gyro, String z_gyro, String x_m, String y_m, String z_m, String lumi, String activity) {
        this.lat = lat;
        this.lng = lng;
        this.alti = alti;
        this.timestamp = timestamp;
        this.x_acc = x_acc;
        this.y_acc = y_acc;
        this.z_acc = z_acc;
        this.x_gyro = x_gyro;
        this.y_gyro = y_gyro;
        this.z_gyro = z_gyro;
        this.x_m = x_m;
        this.y_m = y_m;
        this.z_m = z_m;
        this.lumi = lumi;
        this.activity = activity;
    }

    public String getComplet(){
        String sComplet=lat+lng+alti+timestamp+x_acc+y_acc+z_acc+x_gyro+y_gyro+z_gyro+x_m+y_m+z_m+lumi+activity+"\n";
        return sComplet;
    }
    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getAlti() {
        return alti;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getX_acc() {
        return x_acc;
    }

    public String getY_acc() {
        return y_acc;
    }

    public String getZ_acc() {
        return z_acc;
    }

    public String getX_gyro() {
        return x_gyro;
    }

    public String getY_gyro() {
        return y_gyro;
    }

    public String getZ_gyro() {
        return z_gyro;
    }

    public String getX_m() {
        return x_m;
    }

    public String getY_m() {
        return y_m;
    }

    public String getZ_m() {
        return z_m;
    }

    public String getLumi() {
        return lumi;
    }

    public String getActivity() {
        return activity;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setAlti(String alt) {
        this.alti = alti;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setX_acc(String x_acc) {
        this.x_acc = x_acc;
    }

    public void setY_acc(String y_acc) {
        this.y_acc = y_acc;
    }

    public void setZ_acc(String z_acc) {
        this.z_acc = z_acc;
    }

    public void setX_gyro(String x_gyro) {
        this.x_gyro = x_gyro;
    }

    public void setY_gyro(String y_gyro) {
        this.y_gyro = y_gyro;
    }

    public void setZ_gyro(String z_gyro) {
        this.z_gyro = z_gyro;
    }

    public void setX_m(String x_m) {
        this.x_m = x_m;
    }

    public void setY_m(String y_m) {
        this.y_m = y_m;
    }

    public void setZ_m(String z_m) {
        this.z_m = z_m;
    }

    public void setLumi(String lumi) {
        this.lumi = lumi;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
