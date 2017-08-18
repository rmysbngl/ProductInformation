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

    DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  //Cihazın çaıştığı günü öğrenmek için
    Date date=new Date();
    String currentdate=dateFormat.format(date);
    FirebaseDatabase dat=FirebaseDatabase.getInstance();
    DatabaseReference datRef=dat.getReference().child("Current Products");          //Firebase kısımları sisteme işlem eklemek yada ürün bilgisi çekmek için
    DatabaseReference Processed=dat.getReference().child("Processed Part");
    DatabaseReference UrunTarihleri=dat.getReference().child("Urunler");
     BluetoothSPP bt;



    static ArrayList<String> productIdForDate=new ArrayList<>();            //Ürünün işlem tarihini daha kolay bulmak için, Firebase 'e kaydolduğu ID adresini tutuyor





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


              bt.send("Z", true);           //Cihazı başlatmak için gönderilen komut //TODO: Hangi ifadenin gönderileceğini ömerle tekrar ayarla

                datRef.child("vacuum").setValue(SettingPart.getVacuumValue());                   //Firebase işlemi biten verileri girmek
                datRef.child("plasma time").setValue(SettingPart.getPlasmaValue());
                datRef.child("gaz value").setValue(SettingPart.getGazValue());
                String[] tarih=currentdate.split(" ");
                datRef.child("Date").setValue(tarih[0]);
                datRef.child("Exact Time").setValue(tarih[1]);


                ProgressPart.setVacuumNew(SettingPart.getVacuumValue());                    //işlem başladıktan sonra son vacuum valuesunu gönderiyoruz//TODO: bunu yapmak yerine cihazda bitme konumutu alması gerekebilir.
                ProgressPart.setMinuteProgress(SettingPart.getPlasmaValue());               //işlem süresini (progress bar içinde) tutmak için progress kısmına iletiliyor.

               //tarih sorgulamayı kolaylaştırmak için verilerin Firebase e kaydolduğu key valuelarını tutuyor
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

                //İşlem kısmına geçmek için
                Intent intent = new Intent(ArduinoGiris.this, ProgressPart.class);
                startActivity(intent);
                break;


            //Ayarlar sayfasna geçmek için
            case R.id.ayarlar:
                Intent intent2 = new Intent(ArduinoGiris.this, SettingPart.class);
                startActivity(intent2);
                break;
        }
    }


    //Geri tuşuna basınca bluetooth servisini kapatıpı yeniden connection istiyor. Bağlantı sorunarını ayırmak için
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        bt.stopService();
        Intent intent=new Intent(ArduinoGiris.this,BTConnect.class);
        startActivity(intent);
    }


    public void setProductIdForDate(ArrayList<String> productIdForDate) {
        ArduinoGiris.productIdForDate = productIdForDate;
    }
}
