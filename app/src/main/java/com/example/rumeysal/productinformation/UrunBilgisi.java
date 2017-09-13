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
    public static List<DateAndVacuum> your_array = new ArrayList<DateAndVacuum>();          //Tarih sorgulama kısmına basınca oluşturulacak kısım
    Bundle bundle;
    public String id;
    UrunAdapter adapter;                    //tarih sorgularken kullanılan Adapter
    ListView urunList;
    ArrayList <String> keys=new ArrayList<>();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference ref=database.getReference("Urunler");                     //Firebase kısmı ürünün bilgilerine ulaşmak için
    DatabaseReference tarihRef=database.getReference("Processed Part");
    StorageReference PhotoDownload= FirebaseStorage.getInstance().getReference("Urunler");
    String productcondition;
    static ArrayList<Uri> image=new ArrayList<>();              //ürünün resmini Firebaseden çektikten sonra kurmak için
    ProductShowAdapter productshow;                 //Ürün resimlerini göstertecek adapter


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_urun_bilgisi);
        ProductName=(EditText) findViewById(R.id.ProductName) ;
        ID=(EditText) findViewById(R.id.ProductID) ;
        KurumAdı=(EditText)  findViewById(R.id.KurumAdı);
        bundle=getIntent().getExtras();
        id=bundle.getString("IDProduct");           //URUnün ID si Giriş ekranındaki QR code scanner dan alınıyor
        image.clear();
        your_array.clear();

        // Firebase den ürün bilgisini çekiyor
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //keys.clear();
                if(dataSnapshot.child(id).exists()){
                    DataSnapshot obj = dataSnapshot.child(id);
                    productcondition="OK";
                    ID.setText(id);

                    //Firebaseden ürünün isim Kurum adı, resim gibi bilgilerini çekmek için yapıldı
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

                }
                    //Eğer öyle bir ürün yoksa oluşturuluyor
                if(productcondition==null){
                    Toast.makeText(UrunBilgisi.this, "Ürün Bulunamadı", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //Ürünün resmini sayfada kayar şekilde kurmak için
    public void creatCustomDialog(Context context){
        if(urunList != null){
            if(urunList.getAdapter() != null){
                urunList.setAdapter(null);
            }
        }
        urunList = null;
        your_array.clear();
        View dialogView = View.inflate(context,R.layout.date_and_vacuum, null);
        urunList=dialogView.findViewById(R.id.UrunList);
        adapter = new UrunAdapter(context, R.layout.urun_adi, your_array );
        urunList.setAdapter(adapter);
        AlertDialog.Builder builder=new AlertDialog.Builder(context)
                .setView(dialogView);

        builder.create().show();
    }

    public void OnClick(View view) {
        switch(view.getId()){
            case R.id.Anasayfa:
                Intent intent=new Intent(UrunBilgisi.this,GirisEkrani.class)  ;
                startActivity(intent);
                break;
            case R.id.TarihSorgula:
                creatCustomDialog(UrunBilgisi.this);

                //Firebaseden o ürünün işlem geçirdiği tarihleri sorgulamak için
                tarihRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        your_array.clear();
                        for(int i=0; i<keys.size();i++){
                            your_array.add(new DateAndVacuum(dataSnapshot.child(keys.get(i)).child("Date").getValue().toString(), dataSnapshot.child(keys.get(i)).child("Exact Time").getValue().toString(),dataSnapshot.child(keys.get(i)).child("vacuum").getValue().toString(),dataSnapshot.child(keys.get(i)).child("gaz value").getValue().toString(),dataSnapshot.child(keys.get(i)).child("plasma time").getValue().toString()));
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //Eğer herhangi bi işlemden geçmemişse kurması için
                // TODO: Bu yazılarında daha güzel çıkması için ListView designlarına bakılabilir
                if(your_array.size()==0){
                    urunList.setEmptyView(findViewById(android.R.id.empty));
                }
                break;

        }
    }
}