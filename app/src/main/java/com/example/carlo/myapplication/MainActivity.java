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
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
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
    double altitude;

    //Gyroscope
    Sensor gyroscope;
    SensorManager sensorManagerGyro;
    float sensorGyroX,sensorGyroY,sensorGyroZ;
    TextView GX,GY,GZ;

    //LightSensor
    Sensor lightSensor;
    SensorManager sensorManagerLight;
    float nLight;
    TextView tL;


    private Ficheiro fich;
    TextView tGps;
    List<Dados> dados = new ArrayList();
    Dados a;
    //*******************BOTÕES*************************************************
    public void tranferOnClick (View v){
        Log.i("tranf","gravar");
        a=new Dados(latitude,longitude,sensorX,sensorY,sensorZ);
        Log.i("TEST",""+a.getLatitude());
        fich = new Ficheiro(this);
        fich.gravarNoFicheiro(teste);
        //Button button = (Button) findViewById(R.id.button2);
        //button.isEnabled(true);
    }
    public void stopOnClick(View v){
        sensorManager.unregisterListener(this);
        sensorManagerLight.unregisterListener(this);
        sensorManagerGyro.unregisterListener(this);

    }

    public void onClickStartActivity(View v){

        //****************************************ACELEROMETRO********************************************************
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        //****************************************GIROSCOPIO***********************************************************
        sensorManagerGyro = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscope = sensorManagerGyro.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if(gyroscope == null){
            Toast.makeText(this, "The device has no gyroscope!\n", Toast.LENGTH_SHORT).show();
            finish();
        }

        sensorManagerGyro.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);

        //****************************************LIGHTSENSOR***********************************************************
        sensorManagerLight =(SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManagerLight.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(lightSensor == null){
            Toast.makeText(this, "The device has no lightsensor!\n", Toast.LENGTH_SHORT).show();
            finish();
        }
        sensorManagerLight.registerListener(this,lightSensor,SensorManager.SENSOR_DELAY_NORMAL);


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
                                SimpleDateFormat adf = new SimpleDateFormat("dd-MM-yyyy , HH:mm:ss");
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
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        switch(event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                sensorX = event.values[0];
                sensorY = event.values[1];
                sensorZ = event.values[2];
                tx = (TextView) findViewById(R.id.tX);
                ty = (TextView) findViewById(R.id.tY);
                tz = (TextView) findViewById(R.id.tZ);
                tx.setText("Accelerometer\nX: " + ((int)sensorX) + "\n");
                ty.setText("Y: " + ((int)sensorY) + "\n");
                tz.setText("Z: " + ((int)sensorZ));
                break;
            case Sensor.TYPE_GYROSCOPE:
                sensorGyroX = event.values[0];
                sensorGyroY = event.values[1];
                sensorGyroZ = event.values[2];
                GX = (TextView) findViewById(R.id.tGX);
                GY = (TextView) findViewById(R.id.tGY);
                GZ = (TextView) findViewById(R.id.tGZ);
                GX.setText("GX: " + ((int)sensorGyroX));
                GY.setText("GY: " + ((int)sensorGyroY));
                GZ.setText("GZ: " + ((int)sensorGyroZ));
                break;
            case Sensor.TYPE_LIGHT:
                nLight = event.values[0];
                tL = (TextView) findViewById(R.id.tL);
                tL.setText("Luminosidade: "+ (nLight));
            }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }




   //****************************************ON RESUME ***********************************************************
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
    //****************************************GPS LOCALIZAçÃO********************************************************
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1234 &&
                grantResults[0]==PackageManager.PERMISSION_GRANTED &&
                grantResults[1]==PackageManager.PERMISSION_GRANTED){
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,10,this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        altitude = location.getAltitude();
        tGps.setText("GPS\nLatitude: "+ latitude + "\n" + "Longitude: " + longitude + "\n" + "Altitude: " + altitude);
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
