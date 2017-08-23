package com.example.rumeysal.productinformation;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


// Sisteme yeni ürün girişi yapıldığı zaman kullanılacak
public class UrunTanitma extends AppCompatActivity {
    private static int Camera_Request = 1;
    TextView QRID;
    EditText name;
    EditText KurumAdı;
    public static String id;
    String urunadi;
    String kurumAdi;
    ArrayList<Bitmap> images = new ArrayList<>();         //taken from camera for describing product


    //yeni eklenen ürünler için Firebase part
    FirebaseDatabase urun = FirebaseDatabase.getInstance();
    DatabaseReference UrunRef = urun.getReference().child("Urunler");                 //saving products to firebase
    static List<Product> product = new ArrayList<Product>();                                 //adding product features


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_tanitma);

        QRID = (TextView) findViewById(R.id.QRID);
        name = (EditText) findViewById(R.id.Name);


    }


    @Override
    protected void onStart() {
        super.onStart();
        //To scan QR code at the beginning
        if (id == null) {

            IntentIntegrator integrator = new IntentIntegrator(UrunTanitma.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt(" ");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();

        }


    }

    //ürünün resmini çekmek için menu oluşturuldu. //TODO: eğer istenilirse resim eklemek için button da oluşturulabilir. Bu kısım Sorulacak
    //Buralar silinecek tmm denerim :D biyerde daha vardı onuda button yap ürün bilgisindeydi sanırım o yazıyı kaldırmak istedim daha sonra başka bir şey yazdı orda ne gibi uzundu biraz orayaya da
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_urunyerlestir, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItem = item.getItemId();
//TODO: XML dosyasına buton ekle içinede yoruma alacıpım kısımları koy sonrada tüm menu yazanları silersin
        switch (selectedItem) {
            case R.id.add_product:              //while choosing plus add image for product
                ///   Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                // startActivityForResult(cameraIntent,Camera_Request);

                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //Setting pager to show images taken from product
            images.add(photo);
            ViewPager viewPager;
            UrunTanitmaAdapter urunTanitmaAdapter;
            viewPager = (ViewPager) findViewById(R.id.Pager);
            urunTanitmaAdapter = new UrunTanitmaAdapter(UrunTanitma.this, images);
            viewPager.setAdapter(urunTanitmaAdapter);

        }
        //QR code reader part
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                // super.onBackPressed();
                Intent intent = new Intent(UrunTanitma.this, GirisEkrani.class);
                startActivity(intent);
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_SHORT).show();

            } else {
                id = result.getContents();
                QRID.setText(id);

                //scan for other id to understand this id is used before
                UrunRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(id).exists()) {
                            Toast.makeText(UrunTanitma.this, "This ID was used before ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UrunTanitma.this, GirisEkrani.class);    //If it is used go back
                            startActivity(intent);
                            id = null;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void Clicked(View view) {
        switch (view.getId()) {
            case R.id.Onay:             //Saving product if everything is ok
                StorageReference storePhoto;

                KurumAdı = (EditText) findViewById(R.id.Kurum);
                urunadi = name.getText().toString().trim();
                kurumAdi = KurumAdı.getText().toString().trim();
                product.add(new Product(id, urunadi, kurumAdi));


                for (int i = 0; i < product.size(); i++) {

                    final DatabaseReference ProductInformation = UrunRef.child(product.get(i).getId());
                    ProductInformation.child("name").setValue(product.get(i).getUrunadı());
                    ProductInformation.child("ID").setValue(product.get(i).getId());
                    ProductInformation.child("Kurum Adı").setValue(product.get(i).getKurumadı());

                    for (int j = 0; j < images.size(); j++) {         //Store image on Firebase Storoge
                        storePhoto = FirebaseStorage.getInstance().getReference("Urunler").child(product.get(i).getId()).child("images" + j);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                        images.get(j).compress(Bitmap.CompressFormat.JPEG, 20, baos);
                        byte[] data = baos.toByteArray();
                        UploadTask uploadTask = storePhoto.putBytes(data);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                //  String downloadUrl = taskSnapshot.getDownloadUrl().toString();


                                Toast.makeText(UrunTanitma.this, "It is succefull", Toast.LENGTH_SHORT).show();

                                ProductInformation.child("Images").push().setValue(downloadUrl);
                                id = null;


                            }
                        });


                    }


                }

                Intent intent = new Intent(UrunTanitma.this, GirisEkrani.class);          //Urun Onaylandı Page
                startActivity(intent);

                break;

        }


    }
    // geri dönme tuşu denendi ama olmadı.


}
