package com.example.rumeysal.productinformation;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import java.util.ArrayList;

import java.util.List;



public class UrunYerlestir extends AppCompatActivity {

    ListView urunyerlestir;
    Button arduinoGiris;
    String id="0";
   static ArrayList<String> productIdForDate=new ArrayList<>();         //to getting prdocut id to check later
    ArduinoGiris ag = new ArduinoGiris();                       //to send products id to arduino page


    static List<ProductInProgress> islemUrun=new ArrayList<>();          //Şu an işlem gören ürünler

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference ref=database.getReference("Urunler");                           //sistemde kayıtlı ürünlerin listesi
    DatabaseReference urunList=database.getReference("Current Products");          //şu anki işlem için tutulacak database


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, ""+productIdForDate, Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_yerlestir);
        urunyerlestir=(ListView) findViewById(R.id.EklenenUrunler);
        islemUrun.clear();                  //Önceden Yerleşmiş ürünleri kaldır
        urunList.removeValue();                //Firebasede önceki işlemi kaldır
        arduinoGiris=(Button) findViewById(R.id.ArduinoPart);           //Arduino ya Geçiş
        arduinoGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ag.setProductIdForDate(productIdForDate);
                id="0";
                Intent intent=new Intent(UrunYerlestir.this,ArduinoGiris.class);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onResume() {             //sayfa devam ettiği sürece aşağıdakileri yap
        super.onResume();

        final UrunYerlestirAdapter adapter=new UrunYerlestirAdapter(UrunYerlestir.this,R.layout.urunyerlestirsatir,islemUrun); //şu an işlemde olan ürünlerin listesi
        urunyerlestir.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {        //Firebaseden kullandığımız dataları bulmak için
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(final DataSnapshot urunsnap:dataSnapshot.getChildren() ){           //ürünlerin içindeki tüm dosyaların başlıklarını inceliyor

                    if(id.equals(urunsnap.getKey())){
                        urunList.child("product").push().setValue(urunsnap.getValue());        //eğer ürünü bulduysa işlem görenler listesine taşıyor
                        islemUrun.add(new ProductInProgress(urunsnap.child("ID").getValue().toString(),urunsnap.child("name").getValue().toString())); //işlem görecek olanları listelemek için açılan class
                        productIdForDate.add(urunsnap.child("ID").getValue().toString());
                        adapter.notifyDataSetChanged();


                    }else{
                        urunyerlestir.setEmptyView(findViewById(android.R.id.empty));
                        adapter.notifyDataSetChanged();
                    }


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        urunyerlestir.setOnItemClickListener(new AdapterView.OnItemClickListener() {        //urunlerin üstüne tıklandığnda yapılacak işlemler
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int position=i;
                AlertDialog.Builder diyalogOlusturucu = new AlertDialog.Builder(UrunYerlestir.this);
                diyalogOlusturucu.setMessage("Veriyi Silmek İstediğinize Emin Misiniz")
                        .setCancelable(false)
                        .setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                urunList.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot data:dataSnapshot.getChildren()){
                                            //String key=(islemUrun.get(position).getName());
                                            //Toast.makeText(UrunYerlestir.this, ""+ key , Toast.LENGTH_SHORT).show();
                                            if((islemUrun.get(position).getUrunid()).equals(data.child("ID").getValue().toString())){
                                                FirebaseDatabase.getInstance().getReference().child("Current Products")  //şu an işlemde olan ürünlerden veriyi silma
                                                        .child(data.getKey()).removeValue();
                                                islemUrun.remove(position);
                                                //mobil ekranda görünen veriyi silme
                                                productIdForDate.remove(position);
                                                adapter.notifyDataSetChanged();
                                                break;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            }
                        })
                        .setNegativeButton("HAYIR", null);

                diyalogOlusturucu.create().show();


            }
        });

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_urunyerlestir, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItem = item.getItemId();

        switch (selectedItem) {
            case R.id.add_product:
                IntentIntegrator integrator= new IntentIntegrator(this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
                break;
        }

        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_SHORT).show();
            }else{
                id=result.getContents();


            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
