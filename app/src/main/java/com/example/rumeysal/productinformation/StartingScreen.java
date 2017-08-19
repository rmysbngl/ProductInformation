package com.example.rumeysal.productinformation;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class StartingScreen extends AppCompatActivity {


    CircularProgressButton Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_screen);

        Button = (CircularProgressButton) findViewById(R.id.Button);
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask<String, String, String> demoLogin = new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "done";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        if (s.equals("done")) {
                            Intent intent = new Intent(StartingScreen.this, GirisEkrani.class);
                            startActivity(intent);
                        }
                    }

                };
                Button.startAnimation();
                demoLogin.execute();
            }

            ;

        });
    }
    ;



    public void Cilis(View view) {

        Intent intent = new Intent(StartingScreen.this, GirisEkrani.class);
        startActivity(intent);
    }

}
