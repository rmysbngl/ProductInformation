package com.example.rumeysal.productinformation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SearchDate extends AppCompatActivity {
    EditText enterDate;
    TextWatcher tw;
    DatabaseReference tarih= FirebaseDatabase.getInstance().getReference("Processed Part");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_date);
        enterDate=(EditText) findViewById(R.id.Date);
        enterDate.addTextChangedListener(tw);

    }
    public void TarihSorgula(){


        tarih.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot obj:dataSnapshot.getChildren()){
                //    Toast.makeText(SearchDate.this, ""+currentdate, Toast.LENGTH_SHORT).show();
                   // Toast.makeText(SearchDate.this, ""+obj.child("Date").getValue(), Toast.LENGTH_SHORT).show();
                /*    if((dateFormat.format(date).equals(dateFormat.format(obj.child("Date").getValue().toString())))){
                        Toast.makeText(SearchDate.this, "you got it"+(obj.child("Current Products").child("Date").getValue()), Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(SearchDate.this, "Bu tarihte işlem bulunamadı", Toast.LENGTH_SHORT).show();
                    }*/
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    public void Sorgula(View view) {
        switch(view.getId()){
            case(R.id.Sorgula):
                TarihSorgula();
                break;
        }

    }
}
