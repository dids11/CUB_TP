package com.example.carlo.myapplication;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.widget.TextView;

import java.text.SimpleDateFormat;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    Sensor accelerometer;
    SensorManager sensorManager;
    float sensorX;
    float sensorY;
    float sensorZ;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager =(SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        //DATA E HORAS
        Thread t = new Thread(){
            @Override
            public void run(){
                try{
                    while (!isInterrupted()){
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
                }catch (InterruptedException e){

                }
            }
        };
        t.start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
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
        TextView tx = (TextView) findViewById(R.id.tX);
        TextView ty = (TextView) findViewById(R.id.tY);
        TextView tz = (TextView) findViewById(R.id.tZ);
        TextView ta = (TextView) findViewById(R.id.tA);
        tx.setText("X: " + (sensorX));
        ty.setText("Y: " + (sensorY));
        tz.setText("Z: " + (sensorZ));
        //ta.setText("A: " + (sensorA));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
