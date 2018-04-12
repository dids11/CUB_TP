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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener, AdapterView.OnItemSelectedListener { //LocationListener
    //Accelerometer
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

    //GPS
    TextView tGps;

    //Data & Time
    List<Dados> dados = new ArrayList();
    Dados a;

    //Button Transfer Data
    public void tranferOnClick (View v){
        Log.i("tranf","gravar");
        a=new Dados(latitude,longitude,sensorX,sensorY,sensorZ);
        Log.i("TEST",""+a.getLatitude());
    }

    public void stopOnClick(View v){
        //************USAR DEPOIS PARA PARAR DE REGISTAR OS VALORES**********
        sensorManager.unregisterListener(this);
        sensorManagerLight.unregisterListener(this);
        sensorManagerGyro.unregisterListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Spinner Activities
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.activities_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

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

    public void onClickStartActivity(View v){
        //Accelerometer
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accelerometer == null){
            Toast.makeText(this, "The device has no Gyroscope!\n", Toast.LENGTH_SHORT).show();
            finish();
        }
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        //Gyroscope
        sensorManagerGyro = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscope = sensorManagerGyro.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if(gyroscope == null){
            Toast.makeText(this, "The device has no Gyroscope!\n", Toast.LENGTH_SHORT).show();
            finish();
        }
        sensorManagerGyro.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);

        //LightSensor
        sensorManagerLight =(SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManagerLight.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(lightSensor == null){
            Toast.makeText(this, "The device has no LightSensor!\n", Toast.LENGTH_SHORT).show();
            finish();
        }
        sensorManagerLight.registerListener(this,lightSensor,SensorManager.SENSOR_DELAY_NORMAL);

        //GPS
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
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,10,this); //toDo: actualizar valores
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String sSelected = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(this, sSelected, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
