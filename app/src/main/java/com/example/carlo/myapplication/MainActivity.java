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

import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.jcraft.jsch.*;
import android.os.AsyncTask;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener, AdapterView.OnItemSelectedListener {
    //Accelerometer
    Sensor accelerometer;
    SensorManager sensorManager;
    float sensorX,sensorY,sensorZ;
    TextView tx,ty,tz;

    //GPS
    double longitude;
    double latitude;
    double altitude;
    TextView tGps;

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

    //Magnetometer
    Sensor magnetometer;
    SensorManager sensorManagerMagnetometer;
    float sensorMagnoX, sensorMagnoY, sensorMagnoZ;
    TextView magnoX, magnoY, magnoZ;

    //Data/tempo
    String dateString;
    //fICHEIRO
    private Ficheiro fich;
    String fname ="cubDados.csv";

    //Spinner
    Spinner spinner;
    String sCompleta;
    List<Dados> Ldados = new ArrayList();

    Button bStart,bStop,bTransf;
    private boolean runThread = false;

    //***********************BOTÕES*****************************************

    public void onClickStartActivity(View v){
        bStart = (Button) findViewById(R.id.bTStart);
        bStop = (Button) findViewById(R.id.bTStop);
        Button bTr = (Button) findViewById(R.id.bTTransf);
        bStart.setEnabled(false);
        bStop.setEnabled(true);
        bTr.setEnabled(false);
        runThread= true;

        fich = new Ficheiro(this);
        if(!FileExists(fname)){
            Button bT = (Button) findViewById(R.id.bTTransf);
            bT.setEnabled(false);
            fich.gravarNoFicheiro("lat;lng;alt;timestamp;x_acc;y_acc;z_acc;x_gyro;y_gyro;z_gyro;x_m;y_m;z_m;lumi;activity \n");
        }

        //Accelerometer
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accelerometer == null){
            Toast.makeText(this, "The device has no Accelerometer!\n", Toast.LENGTH_SHORT).show();
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

        //Magnetometer
        sensorManagerMagnetometer = (SensorManager) getSystemService(SENSOR_SERVICE);
        magnetometer = sensorManagerMagnetometer.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(magnetometer == null){
            Toast.makeText(this, "The device has no Magnetometer!\n", Toast.LENGTH_SHORT).show();
            finish();
        }
        sensorManagerMagnetometer.registerListener(this,magnetometer,SensorManager.SENSOR_DELAY_NORMAL);




        //PASSAR PARA EM ARRAY
        Thread b = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()&& runThread) {
                        Thread.sleep(250);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Dados a;
                                String lat = String.valueOf(latitude)+";";
                                String lng = String.valueOf(longitude)+";";
                                String alt= String.valueOf(altitude)+";";
                                String timestamp = dateString+";";
                                String x_acc = String.valueOf(sensorX)+";";
                                String y_acc = String.valueOf(sensorY)+";";
                                String z_acc = String.valueOf(sensorZ)+";";
                                String x_gyro = String.valueOf(sensorGyroX)+";";
                                String y_gyro = String.valueOf(sensorGyroY)+";";
                                String z_gyro = String.valueOf(sensorGyroZ)+";";
                                String x_m = String.valueOf(sensorMagnoX)+";";
                                String y_m = String.valueOf(sensorMagnoY)+";";
                                String z_m = String.valueOf(sensorMagnoZ)+";";
                                String lumi = String.valueOf(nLight)+";";
                                String activity = String.valueOf(spinner != null ? spinner.getSelectedItem() : null);
                                a = new Dados(lat, lng, alt, timestamp,x_acc,  y_acc, z_acc, x_gyro, y_gyro, z_gyro, x_m, y_m, z_m, lumi,activity);
                                Ldados.add(a);
                                a=null;

                            }
                        });
                    }
                } catch (InterruptedException e) {

                }
            }
        };
        b.start();

        //PARA GRAVAR EM FICHEIRO
            Thread c = new Thread() {
                @Override
                public void run() {
                    try {
                        while (!isInterrupted()&& runThread) {
                            Thread.sleep(8000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    for(int i=0;i<Ldados.size();i++){
                                        fich.gravarNoFicheiro(Ldados.get(i).getComplet());
                                    }
                                    Ldados.clear();
                                }
                            });
                        }
                    } catch (InterruptedException e) {

                    }
                }
            };
            c.start();
    }

    public void stopOnClick(View v){
        Button bSta = (Button) findViewById(R.id.bTStart);
        runThread= false;
        Button bSt = (Button) findViewById(R.id.bTStop);
        Button bTra = (Button) findViewById(R.id.bTTransf);
        bSt.setEnabled(false);
        bSta.setEnabled(true);
        bTra.setEnabled(true);
        sensorManager.unregisterListener(this);
        sensorManagerLight.unregisterListener(this);
        sensorManagerGyro.unregisterListener(this);
        sensorManagerMagnetometer.unregisterListener(this);
    }

    public void tranferOnClick (View v){
        if(FileExists(fname)){
            new LongOperation().execute();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*fich = new Ficheiro(this);
        if(!FileExists(fname)){
            Button bT = (Button) findViewById(R.id.bTTransf);
            bT.setEnabled(false);
            fich.gravarNoFicheiro("lat;lng;alt;timestamp;x_acc;y_acc;z_acc;x_gyro;y_gyro;z_gyro;x_m;y_m;z_m;lumi;activity \n");
        }*/

        //Spinner Activities
        spinner = (Spinner)findViewById(R.id.spinner);
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
                                dateString = adf.format(date);
                                tdate.setText(dateString);
                            }
                        });
                    }
                } catch (InterruptedException e) {

                }
            }
        };
        t.start();

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
                tx.setText("X: " + ((int)sensorX));
                ty.setText("Y: " + ((int)sensorY));
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
                tL.setText("Luminosity: " + nLight);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                sensorMagnoX = event.values[0];
                sensorMagnoY = event.values[1];
                sensorMagnoZ = event.values[2];
                magnoX = (TextView) findViewById(R.id.tMX);
                magnoY = (TextView) findViewById(R.id.tMY);
                magnoZ = (TextView) findViewById(R.id.tMZ);
                magnoX.setText("MX: " + ((int)sensorMagnoX));
                magnoY.setText("MY: " + ((int)sensorMagnoY));
                magnoZ.setText("MZ: " + ((int)sensorMagnoZ));
                break;
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
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10,10,this);
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
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10,10,this);
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onLocationChanged(Location location) {
        if(location!=null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            altitude = location.getAltitude();
            tGps = (TextView) findViewById(R.id.tGPS);
            longitude = Double.valueOf(String.format("%.2f", longitude));
            latitude = Double.valueOf(String.format("%.2f", latitude));
            altitude = Double.valueOf(String.format("%.2f", altitude));
            tGps.setText("Lat: " + latitude + "\n" + "Long: " + longitude + "\n" + "Alt: " + altitude);
        }
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
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    //****************************************OUTRAS FUNCOES************************
    public boolean FileExists(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }
    public boolean FileDelete(String fname){
        File filee = getBaseContext().getFileStreamPath(fname);
        boolean del= filee.delete();
        return del;
    }
    private class LongOperation extends AsyncTask<Void, Integer, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                JSch ssh = new JSch();
                Session session = ssh.getSession("cubistudent", "urbysense.dei.uc.pt", 22);
                // Remember that this is just for testing and we need a quick access, you can add an identity and known_hosts file to prevent
                // Man In the Middle attacks
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.setPassword("mis_cubi_2018");

                session.connect();
                Channel channel = session.openChannel("sftp");
                channel.connect();

                ChannelSftp sftp = (ChannelSftp) channel;

                sftp.cd("/home/cubistudent/a21220121_a21220107");
                // If you need to display the progress of the upload, read how to do it in the end of the article
                String uuid = UUID.randomUUID().toString();
                uuid="dadosCb_"+uuid+".csv";
                // use the put method , if you are using android remember to remove "file://" and use only the relative path
                sftp.put(getBaseContext().getFileStreamPath(fname).getPath(), uuid);
                channel.disconnect();
                session.disconnect();
            } catch (JSchException e) {
                System.out.println(e.getMessage().toString());
                e.printStackTrace();
            } catch (SftpException e) {
                System.out.println(e.getMessage().toString());
                e.printStackTrace();
            }
            return "Terminado";
        }



        @Override
        protected void onPostExecute(String result) {
            Log.d("PostExecuted",result);
            Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            FileDelete(fname);
        }



    }
    public void onDestroy() {
        super.onDestroy();
    }
}
