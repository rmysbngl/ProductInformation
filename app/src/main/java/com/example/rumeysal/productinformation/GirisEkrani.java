package com.example.rumeysal.productinformation;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class GirisEkrani extends AppCompatActivity {

    static String id;

    public static String getId() {
        return id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_ekrani);




    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_SHORT).show();
            } else {
                id = result.getContents();
                Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();
                //Toast.makeText(this, ""+ id, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(GirisEkrani.this,UrunBilgisi.class);
                intent.putExtra("IDProduct",id);
                startActivity(intent);


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void Pages(View view) {
        switch(view.getId()){
            case(R.id.IslemTarihiSorgula):
                Intent intent3=new Intent(GirisEkrani.this, SearchDate.class);
                startActivity(intent3);
                break;
            case(R.id.UrunTanitma):

                Intent intent=new Intent(GirisEkrani.this, UrunTanitma.class);
                startActivity(intent);
                break;

            case(R.id.UrunBilgisiGiris):
                AlertDialog.Builder dialogOlusturucu = new AlertDialog.Builder(GirisEkrani.this);
                dialogOlusturucu.setMessage("Lütfen görmek istediğiniz ürünün QR kodunu tutunuz")
                        .setCancelable(false)
                        .setPositiveButton("Urun Tara", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                IntentIntegrator integrator = new IntentIntegrator(GirisEkrani.this);
                                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                                integrator.setPrompt("Scan");
                                integrator.setCameraId(0);
                                integrator.setBeepEnabled(false);
                                integrator.setBarcodeImageEnabled(false);
                                integrator.initiateScan();


                            }
                        })
                        .setNegativeButton("Anasayfa", null);
                dialogOlusturucu.create().show();

                break;

        case(R.id.Yeniurun):
            Intent intent2=new Intent(GirisEkrani.this, UrunYerlestir.class);
            startActivity(intent2);
            break;
        }
    }
}
