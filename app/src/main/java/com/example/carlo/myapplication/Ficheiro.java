package com.example.carlo.myapplication;

import android.content.Context;
import android.widget.Toast;

import java.io.FileOutputStream;

/**
 * Created by carlo on 12/04/2018.
 */

public class Ficheiro {
    Context context;
    String NomeFich = "cubDados.csv";

    public Ficheiro(Context cont){
        context= cont;
    }

    public void gravarNoFicheiro(String dados){
        try{
            FileOutputStream fileOut = context.openFileOutput(NomeFich,Context.MODE_APPEND);
            fileOut.write(dados.getBytes());
            Toast.makeText(context,"Save!", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context,"Error on save!",Toast.LENGTH_LONG).show();
        }
    }
}
