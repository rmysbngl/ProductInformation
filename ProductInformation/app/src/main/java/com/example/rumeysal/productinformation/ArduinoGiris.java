package com.example.rumeysal.productinformation;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class ArduinoGiris extends AppCompatActivity {
   // Calendar c= Calendar.getInstance();
    //SimpleDateFormat df=new SimpleDateFormat("dd/mm/yyyy");
   // String currentdate=df.format(c.getTime());
 //   String currentdate=new SimpleDateFormat("dd-mm-yyyy").format(new Date());
    DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date=new Date();
    String currentdate=dateFormat.format(date);
    FirebaseDatabase dat=FirebaseDatabase.getInstance();
    DatabaseReference datRef=dat.getReference().child("Current Products");
    DatabaseReference Processed=dat.getReference().child("Processed Part");

    DatabaseReference UrunTarihleri=dat.getReference().child("Urunler");
    Button baslat;
    Button ayarlar;
    static String mesaj;

    public static void setMesaj(String mesaj) {
        ArduinoGiris.mesaj = mesaj;
    }

    /*
            private static final String TAG = "bluetooth";
            static BluetoothAdapter btAdapter = null;
            static BluetoothSocket btSocket = null;
            static OutputStream outStream = null;
            public static BluetoothDevice device;

            public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Bluetooth Connection Adresi


            public static String address = "20:16:05:23:90:34";//bağlantıda olan Bluetooth un MAC address*/
    static ArrayList<String> productIdForDate=new ArrayList<>();

    public void setProductIdForDate(ArrayList<String> productIdForDate) {
        this.productIdForDate = productIdForDate;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arduino_giris);

        baslat=(Button) findViewById(R.id.Baslat);
        ayarlar=(Button) findViewById(R.id.ayarlar);

      //  btAdapter = BluetoothAdapter.getDefaultAdapter();
        //checkBTState();                                                             //Bluetooth aç kapa

           // Intent bluetooth=new Intent(ArduinoGiris.this,BluetoothService.class);
        //startService(bluetooth);

        baslat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  serviceMesaj.setSendmesaj("t");

                //DATA ların Arduino ya gönderilmesi


               //Toast.makeText(service, ""+mesaj, Toast.LENGTH_SHORT).show();                                          //Başlatmak için gerekli komut
                Toast.makeText(ArduinoGiris.this, ""+mesaj, Toast.LENGTH_SHORT).show();
                datRef.child("vacuum").setValue(SettingPart.getVacuumValue());                   //Firebase işlemi biten verileri girmek
                datRef.child("plasma time").setValue(SettingPart.getPlasmaValue());
                datRef.child("gaz value").setValue(SettingPart.getGazValue());
                datRef.child("Date").setValue(currentdate);




                datRef.addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
                         String key = Processed.push().getKey();
                      Processed.child(key).setValue(dataSnapshot.getValue());
                      for(int i=0; i<productIdForDate.size();i++){
                              UrunTarihleri.child(productIdForDate.get(i)).child("KeyValueDate").push().setValue(key);


                      }

                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {

                  }
              });
              //  datRef.removeValue();

                Intent intent=new Intent(ArduinoGiris.this,ProgressPart.class);
                startActivity(intent);


            }
        });

        ayarlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(ArduinoGiris.this,SettingPart.class);
                startActivity(intent);

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}
