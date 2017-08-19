package com.example.rumeysal.productinformation;

import android.content.Intent;
<<<<<<< HEAD

import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class StartingScreen extends AppCompatActivity {



    CircularProgressButton Button;

=======
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartingScreen extends AppCompatActivity {

>>>>>>> parent of 2e1493e... start screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_screen);
    }

<<<<<<< HEAD

=======
>>>>>>> parent of 2e1493e... start screen
    public void Cilis(View view) {
        Intent intent=new Intent(StartingScreen.this,GirisEkrani.class);

<<<<<<< HEAD
        startActivity(intent);
    }
=======
        Intent intent=new Intent(StartingScreen.this,GirisEkrani.class);
        startActivity(intent);}

>>>>>>> parent of 2e1493e... start screen
}
