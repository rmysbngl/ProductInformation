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

    static String id;               //Product ID for Urun Bilgisi class

    public static String getId() {
        return id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_ekrani);

    }


    //QR code part to get product ID

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
            case(R.id.IslemTarihiSorgula):          //TODO: Bu kısım hala net değil, çıkartılabilir
               // Intent intent3=new Intent(GirisEkrani.this, SearchDate.class);
                //startActivity(intent3);
                AlertDialog.Builder dialog=new AlertDialog.Builder(GirisEkrani.this);
                dialog.setMessage("Bu özellik aktif değildir")
                        .setCancelable(true);
                dialog.show();
                break;

            case(R.id.UrunTanitma):         //Sisteme yeni giriş yapmak istendiğinde gidilecek sayfa

                Intent intent=new Intent(GirisEkrani.this, UrunTanitma.class);
                startActivity(intent);
                break;

            case(R.id.UrunBilgisiGiris):                //Hazır olan bir ürünün bilgilerini öğrenmek için önce QR code kısmı çalışıyor sonro diğer class a geçiş yapılıyor
                    IntentIntegrator integrator = new IntentIntegrator(GirisEkrani.this);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                    integrator.setPrompt("");
                    integrator.setCameraId(0);
                    integrator.setBeepEnabled(false);
                    integrator.setBarcodeImageEnabled(false);
                    integrator.initiateScan();
                break;

        case(R.id.Yeniurun):        //Cihaz çalışırken, cihaza yerleşecek ürünlere yönlendirir.
            Intent intent2=new Intent(GirisEkrani.this, UrunYerlestir.class);
            startActivity(intent2);
            break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GirisEkrani.this,StartingScreen.class );
        startActivity(intent);
        super.onBackPressed();
    }
}
