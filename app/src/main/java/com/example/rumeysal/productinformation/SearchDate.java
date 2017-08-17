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
       tw= new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               if (!charSequence.toString().equals(current)) {
                   String clean = charSequence.toString().replaceAll("[^\\d.]", "");
                   String cleanC = current.replaceAll("[^\\d.]", "");

                   int cl = clean.length();
                   int sel = cl;
                   for (int i = 2; i <= cl && i < 6; i += 2) {
                       sel++;
                   }
                   //Fix for pressing delete next to a forward slash
                   if (clean.equals(cleanC)) sel--;

                   if (clean.length() < 8){
                       clean = clean + ddmmyyyy.substring(clean.length());
                   }else{
                       //This part makes sure that when we finish entering numbers
                       //the date is correct, fixing it otherwise
                       int day  = Integer.parseInt(clean.substring(0,2));
                       int mon  = Integer.parseInt(clean.substring(2,4));
                       int year = Integer.parseInt(clean.substring(4,8));

                       if(mon > 12) mon = 12;
                       cal.set(Calendar.MONTH, mon-1);
                       year = (year<1900)?1900:(year>2100)?2100:year;
                       cal.set(Calendar.YEAR, year);
                       // ^ first set year for the line below to work correctly
                       //with leap years - otherwise, date e.g. 29/02/2012
                       //would be automatically corrected to 28/02/2012

                       day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                       clean = String.format("%02d%02d%02d",day, mon, year);
                   }

                   clean = String.format("%s/%s/%s", clean.substring(0, 2),
                           clean.substring(2, 4),
                           clean.substring(4, 8));

                   sel = sel < 0 ? 0 : sel;
                   current = clean;
                   enterDate.setText(current);
                   enterDate.setSelection(sel < current.length() ? sel : current.length());
               }
           }

           @Override
           public void afterTextChanged(Editable editable) {

           }

           private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();}



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
