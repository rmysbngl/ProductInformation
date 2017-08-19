package com.example.rumeysal.productinformation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_screen);
    }

    public void Cilis(View view) {
        Intent intent=new Intent(StartingScreen.this,GirisEkrani.class);

        startActivity(intent);
    }
}
