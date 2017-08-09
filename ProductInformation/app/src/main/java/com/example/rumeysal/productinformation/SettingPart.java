package com.example.rumeysal.productinformation;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.prefs.Preferences;

public class SettingPart extends AppCompatActivity {

 BluetoothDevice device;

    Button plusOne;
    Button minusOne;
    Button plusYuz;
    Button minusYuz;
    Button gazPlusOne;
    Button gazMinusOne;
    Button arduinoGiris;
    EditText vakum;
    EditText plazma;
    EditText gaz;

    static int vacuumValue=-84;
    static int plasmaValue=1500;
    static int gazValue=10;




    public static int getVacuumValue() {

        return vacuumValue;
    }

    public static int getPlasmaValue() {
        return plasmaValue;
    }

    public static int getGazValue() {
        return gazValue;
    }







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_part);

      // device= (BluetoothDevice) ((ArduinoGiris) getApplication()).getDevice();
         plusOne=(Button) findViewById(R.id.PlusOne);
        minusOne = (Button) findViewById(R.id.MinusOne);
        plusYuz = (Button) findViewById(R.id.PlusYuz);
        minusYuz = (Button) findViewById(R.id.MinusYuz);
        gazMinusOne = (Button) findViewById(R.id.GazMinusOne);
        gazPlusOne = (Button) findViewById(R.id.GazPlusOne);
        arduinoGiris = (Button) findViewById(R.id.Home);
        vakum = (EditText) findViewById(R.id.VakumText);
        plazma = (EditText) findViewById(R.id.PlazmaText);
        gaz = (EditText) findViewById(R.id.GazText);


        vakum.setText(String.valueOf(vacuumValue));
        plazma.setText(String.valueOf(plasmaValue));
        gaz.setText(String.valueOf(gazValue));






        arduinoGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vacuumValue=Integer.valueOf(String.valueOf(vakum.getText()));           //en son haliyle data göndermek için
                vakum.setText(String.valueOf(vacuumValue));
                plasmaValue=Integer.valueOf(String.valueOf(plazma.getText()));
                plazma.setText(String.valueOf(plasmaValue));
                gazValue=Integer.valueOf(String.valueOf(gaz.getText()));
                gaz.setText(String.valueOf(gazValue));


                Intent intent = new Intent(SettingPart.this, ArduinoGiris.class);
                startActivity(intent);
            }
        });


      plusOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             //   ((ArduinoGiris) getApplication()).sendData("a");

                vacuumValue=Integer.valueOf(String.valueOf(vakum.getText()));       // editText değişirse veriyi tutmak için
                vacuumValue=vacuumValue+1;                                  //vakumu +1 artırmak
                vakum.setText(String.valueOf(vacuumValue));

            }
        });

        minusOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vacuumValue=Integer.valueOf(String.valueOf(vakum.getText()));
                vacuumValue = vacuumValue - 1;
                vakum.setText(String.valueOf(vacuumValue));

            }
        });


        plusYuz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                plasmaValue = Integer.valueOf(String.valueOf(plazma.getText()));
                plasmaValue = plasmaValue + 100;
                plazma.setText(String.valueOf(plasmaValue));
            }
        });

        minusYuz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                plasmaValue=Integer.valueOf(String.valueOf(plazma.getText()));
                plasmaValue = plasmaValue - 100;
                plazma.setText(String.valueOf(plasmaValue));
            }
        });


        gazPlusOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gazValue=Integer.valueOf(String.valueOf(gaz.getText()));
                gazValue = gazValue + 1;
                gaz.setText(String.valueOf(gazValue));
            }
        });

        gazMinusOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gazValue=Integer.valueOf(String.valueOf(gaz.getText()));
                gazValue = gazValue - 1;
                gaz.setText(String.valueOf(gazValue));
            }
        });


    }



}


