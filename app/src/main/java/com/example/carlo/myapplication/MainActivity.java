package com.example.carlo.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener{ //LocationListener
    //acelerometro
    Sensor accelerometer;
    SensorManager sensorManager;
    float sensorX,sensorY,sensorZ;
    TextView tx,ty,tz;
    double longitude;
    double latitude;

    //Gyroscope
    Sensor gyroscope;
    SensorManager sensorManagerGyro;
    float sensorGyroX,sensorGyroY,sensorGyroZ;
    TextView GX,GY,GZ;

    //magnetometer
    Sensor magnetometer;


    TextView tGps;

    List<Dados> dados = new ArrayList();
    Dados a;

    //botao tranfer data
    public void tranferOnClick (View v){
        Log.i("tranf","gravar");
        a=new Dados(latitude,longitude,sensorX,sensorY,sensorZ);
        Log.i("TEST",""+a.getLatitude());

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //****************************************DATA E HORAS********************************************************
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tdate = (TextView) findViewById(R.id.tA);
                                long date = System.currentTimeMillis();
                                SimpleDateFormat adf = new SimpleDateFormat("MMM dd yyyy , hh-mm-ss a");
                                String dateString = adf.format(date);
                                tdate.setText(dateString);
                            }
                        });
                    }
                } catch (InterruptedException e) {

                }
            }
        };
        t.start();

        //****************************************ACELEROMETRO********************************************************
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        //****************************************GIROSCOPIO***********************************************************
        sensorManagerGyro = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscope = sensorManagerGyro.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManagerGyro.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);

        //****************************************GPS (LOCALIZAçÃO)***************************************************
        tGps=findViewById(R.id.tGPS);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, 1234);
                return;
            }
        }

    }




    //****************************************ACELEROMETRO**********************************************************
    @Override
    public void onSensorChanged(SensorEvent event) {

        switch(event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                sensorX = event.values[0];
                sensorY = event.values[1];
                sensorZ = event.values[2];

                /*final float alpha = 0.8;

                // Isolate the force of gravity with the low-pass filter.
                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

                // Remove the gravity contribution with the high-pass filter.
                linear_acceleration[0] = event.values[0] - gravity[0];
                linear_acceleration[1] = event.values[1] - gravity[1];
                linear_acceleration[2] = event.values[2] - gravity[2];*/

                //float sensorA = (event.values[0] + event.values[1] + event.values[2]);
                tx = (TextView) findViewById(R.id.tX);
                ty = (TextView) findViewById(R.id.tY);
                tz = (TextView) findViewById(R.id.tZ);
                tx.setText("X: " + (sensorX));
                ty.setText("Y: " + (sensorY));
                tz.setText("Z: " + (sensorZ));
                break;

            case Sensor.TYPE_GYROSCOPE:
                sensorGyroX = event.values[0];
                sensorGyroY = event.values[1];
                sensorGyroZ = event.values[2];
                GX = (TextView) findViewById(R.id.tGX);
                GY = (TextView) findViewById(R.id.tGY);
                GZ = (TextView) findViewById(R.id.tGZ);
                GX.setText("GX: " + (sensorGyroX));
                GY.setText("GY: " + (sensorGyroY));
                GZ.setText("GZ: " + (sensorGyroZ));
                break;
            }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }




    //****************************************GPS LOCALIZAçÃO********************************************************
    @Override
    protected void onResume() {
        super.onResume();
        SensorManager sm =(SensorManager) getSystemService(SENSOR_SERVICE);
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                return;
            }
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,10,this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1234 &&
                grantResults[0]==PackageManager.PERMISSION_GRANTED &&
                grantResults[1]==PackageManager.PERMISSION_GRANTED){
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,10,this); //toDo: actualizar valores
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        tGps.setText("GPS: "+ longitude + " , " + latitude);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
