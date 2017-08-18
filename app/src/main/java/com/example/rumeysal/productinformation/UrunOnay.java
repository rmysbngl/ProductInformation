package com.example.rumeysal.productinformation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//Kaydedilen ürünün onayı için .//TODO: bu sayfanın designı kesinlikle yapılmalı
public class UrunOnay extends AppCompatActivity {
    Button butAnasayfa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_onay);
        butAnasayfa=(Button) findViewById(R.id.Girisanasayfa);
        butAnasayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UrunOnay.this,GirisEkrani.class);
                startActivity(intent);
            }
        });
    }

}
