package com.example.rumeysal.productinformation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UrunBilgisi extends AppCompatActivity {
    EditText ProductName, ID, KurumAdı;
    Button Anasayfa, TarihSorgula;
    public static List<DateAndVacuum> your_array = new ArrayList<DateAndVacuum>();
    Bundle bundle;
    public String id;
    UrunAdapter adapter;
    ListView urunList;
    static ArrayList <String> keys=new ArrayList<>();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference ref=database.getReference("Urunler");
    DatabaseReference tarihRef=database.getReference("Processed Part");
    StorageReference PhotoDownload= FirebaseStorage.getInstance().getReference("Urunler");
    String productcondition;
    static ArrayList<Uri> image=new ArrayList<>();
    ProductShowAdapter productshow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_urun_bilgisi);
        ProductName=(EditText) findViewById(R.id.ProductName) ;
        ID=(EditText) findViewById(R.id.ProductID) ;
        KurumAdı=(EditText)  findViewById(R.id.KurumAdı);
        Anasayfa=(Button) findViewById(R.id.Anasayfa);
        TarihSorgula=(Button) findViewById(R.id.TarihSorgula);
        bundle=getIntent().getExtras();
        id=bundle.getString("IDProduct");

        image.clear();

        Anasayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UrunBilgisi.this,GirisEkrani.class)  ;
                startActivity(intent);
            }
        });

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(id).exists()){
                    DataSnapshot obj = dataSnapshot.child(id);
                    productcondition="OK";
                    ID.setText(id);


                    ProductName.setText(obj.child("name").getValue().toString());
                    KurumAdı.setText(obj.child("Kurum Adı").getValue().toString());
                    for (int i=0; i<obj.child("Images").getChildrenCount();i++){
                        PhotoDownload.child(id).child("images"+i).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                ViewPager photo;
                                photo = (ViewPager) findViewById(R.id.photo);
                                image.add(uri);
                                productshow = new ProductShowAdapter(UrunBilgisi.this, image);
                                photo.setAdapter(productshow);


                            }
                        });
                    }


                    for(DataSnapshot DateRef: obj.child("KeyValueDate").getChildren()){
                        keys.add(DateRef.getValue().toString());
                    }

                    Toast.makeText(UrunBilgisi.this, ""+keys.get(0), Toast.LENGTH_SHORT).show();



                }
                //}



                if(productcondition==null){
                    Toast.makeText(UrunBilgisi.this, "Ürün Bulunamadı", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        TarihSorgula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creatCustomDialog(UrunBilgisi.this);

                tarihRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(int i=0; i<keys.size();i++){
                            your_array.add(new DateAndVacuum(dataSnapshot.child(keys.get(i)).child("Date").getValue().toString(),dataSnapshot.child(keys.get(i)).child("vacuum").getValue().toString(),dataSnapshot.child(keys.get(i)).child("gaz value").getValue().toString(),dataSnapshot.child(keys.get(i)).child("plasma time").getValue().toString()));
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });






            }



        });


    }







    public void creatCustomDialog(Context context){
        View dialogView = View.inflate(context,R.layout.date_and_vacuum, null);
        urunList=dialogView.findViewById(R.id.UrunList);
        adapter = new UrunAdapter(context, R.layout.urun_adi, your_array );
        urunList.setAdapter(adapter);
        AlertDialog.Builder builder=new AlertDialog.Builder(context)
                .setView(dialogView);
        builder.create().show();

    }


}