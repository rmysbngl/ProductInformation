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

import app.akexorcist.bluetotohspp.library.BluetoothSPP;


//TODO: BU kısımda +/- 1 gibi buttonlar için güzel bir tasarım oluşuturulmalı
public class SettingPart extends AppCompatActivity {

    BluetoothSPP bt;


    EditText vakum;
    EditText plazma;
    EditText gaz;


    //Cihazın Default value ları
    static int vacuumValue=-84;
    static int plasmaValue=1500;
    static int gazValue=10;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_part);
        bt=(BTConnect.getBt());
        vakum = (EditText) findViewById(R.id.VakumText);
        plazma = (EditText) findViewById(R.id.PlazmaText);
        gaz = (EditText) findViewById(R.id.GazText);


        vakum.setText(String.valueOf(vacuumValue));
        plazma.setText(String.valueOf(plasmaValue));
        gaz.setText(String.valueOf(gazValue));





    }

    @Override
    public void onBackPressed() {               //Geri tuşuna basınca bozulmaları önlemek için
        super.onBackPressed();
        Intent intent=new Intent(SettingPart.this,ArduinoGiris.class);
        startActivity(intent);
    }

    public void SetClick(View view) {
        switch(view.getId()){
            case(R.id.PlusOne):
                vacuumValue=Integer.valueOf(String.valueOf(vakum.getText()));       // editText değişirse veriyi tutmak için
                vacuumValue=vacuumValue+1;                                  //vakumu +1 artırmak
                vakum.setText(String.valueOf(vacuumValue));
                break;
            case (R.id.MinusOne):
                vacuumValue=Integer.valueOf(String.valueOf(vakum.getText()));
                vacuumValue = vacuumValue - 1;
                vakum.setText(String.valueOf(vacuumValue));

                break;
            case(R.id.MinusYuz):
                plasmaValue=Integer.valueOf(String.valueOf(plazma.getText()));
                plasmaValue = plasmaValue - 100;
                plazma.setText(String.valueOf(plasmaValue));
                break;
            case(R.id.PlusYuz):
                plasmaValue = Integer.valueOf(String.valueOf(plazma.getText()));
                plasmaValue = plasmaValue + 100;
                plazma.setText(String.valueOf(plasmaValue));
                break;
            case(R.id.GazMinusOne):
                gazValue=Integer.valueOf(String.valueOf(gaz.getText()));
                gazValue = gazValue - 1;
                gaz.setText(String.valueOf(gazValue));
                break;
            case(R.id.GazPlusOne):
                gazValue=Integer.valueOf(String.valueOf(gaz.getText()));
                gazValue = gazValue + 1;
                gaz.setText(String.valueOf(gazValue));
                break;
            case(R.id.Home):
                vacuumValue=Integer.valueOf(String.valueOf(vakum.getText()));           //en son haliyle data göndermek için
                vakum.setText(String.valueOf(vacuumValue));
                plasmaValue=Integer.valueOf(String.valueOf(plazma.getText()));
                plazma.setText(String.valueOf(plasmaValue));
                gazValue=Integer.valueOf(String.valueOf(gaz.getText()));
                gaz.setText(String.valueOf(gazValue));

                //TODO: ARduino ya gönderilen verilerin kısımları bunları da Ömer'e göster
                bt.send("V"+(String.valueOf(SettingPart.getVacuumValue()))+"G"+(String.valueOf( SettingPart.getGazValue()))+"P"+(String.valueOf(SettingPart.getPlasmaValue())), true);
                Intent intent = new Intent(SettingPart.this, ArduinoGiris.class);
                startActivity(intent);
                break;

        }
    }
    public static int getVacuumValue() {

        return vacuumValue;
    }

    public static int getPlasmaValue() {
        return plasmaValue;
    }

    public static int getGazValue() {
        return gazValue;
    }


}


