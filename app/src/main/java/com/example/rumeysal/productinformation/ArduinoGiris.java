package com.example.rumeysal.productinformation;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import java.io.OutputStream;
import java.lang.reflect.Method;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class ArduinoGiris extends AppCompatActivity {

    DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date=new Date();
    String currentdate=dateFormat.format(date);
    FirebaseDatabase dat=FirebaseDatabase.getInstance();
    DatabaseReference datRef=dat.getReference().child("Current Products");
    DatabaseReference Processed=dat.getReference().child("Processed Part");
    DatabaseReference UrunTarihleri=dat.getReference().child("Urunler");
   BluetoothSPP bt;


    static ArrayList<String> productIdForDate=new ArrayList<>();//bağlantıda olan Bluetooth un MAC address

    public void setProductIdForDate(ArrayList<String> productIdForDate) {
        this.productIdForDate = productIdForDate;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arduino_giris);
        bt=(BTConnect.getBt());
                                          //Bluetooth aç kapa

    }

    public void ArduinoStart(View view) {
        switch(view.getId()){
            case R.id.Baslat:
              bt.send("V"+(String.valueOf(SettingPart.getVacuumValue()))+"G"+(String.valueOf( SettingPart.getGazValue()))+"P"+(String.valueOf(SettingPart.getPlasmaValue())+"Z"), true);

                datRef.child("vacuum").setValue(SettingPart.getVacuumValue());                   //Firebase işlemi biten verileri girmek
                datRef.child("plasma time").setValue(SettingPart.getPlasmaValue());
                datRef.child("gaz value").setValue(SettingPart.getGazValue());
                datRef.child("Date").setValue(currentdate);


                datRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String key = Processed.push().getKey();
                        Processed.child(key).setValue(dataSnapshot.getValue());
                        for (int i = 0; i < productIdForDate.size(); i++) {
                            UrunTarihleri.child(productIdForDate.get(i)).child("KeyValueDate").push().setValue(key);


                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Intent intent = new Intent(ArduinoGiris.this, ProgressPart.class);
                startActivity(intent);
                break;
            case R.id.ayarlar:
                Intent intent2 = new Intent(ArduinoGiris.this, SettingPart.class);
                startActivity(intent2);
                break;
        }
    }


}
