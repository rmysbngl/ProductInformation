package com.example.rumeysal.productinformation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static List<DateAndVacuum> your_array_list = new ArrayList<DateAndVacuum>();
    TextView cihazadi;
    ListView urunList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference products = database.getReference("message");

        cihazadi  =(TextView)findViewById(R.id.UrunAdı);
        urunList=(ListView) findViewById(R.id.UrunList);

        DatabaseReference refProduct=FirebaseDatabase.getInstance().getReference().child("Products").child("Product1");
        refProduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                cihazadi.setText("Cihaz Adı: "+ dataSnapshot.child("ProductName").getValue().toString());
                String id = dataSnapshot.child("ID").getValue().toString();
                DateAndVacuum davID = new DateAndVacuum("ID: ", id);
                String unit = dataSnapshot.child("Unit").getValue().toString();
                DateAndVacuum davUnit = new DateAndVacuum("UNIT: : ", unit);
                String vacuumValue = dataSnapshot.child("VacuumValue").getValue().toString();
                String date = dataSnapshot.child("Date").getValue().toString();
                    DateAndVacuum davVV = new DateAndVacuum("DATE    ", "VACUUM VALUE");
                DateAndVacuum davDV = new DateAndVacuum(date , vacuumValue);

                your_array_list.add(davID);
                your_array_list.add(davUnit);
                your_array_list.add(davVV);
                your_array_list.add(davDV);

                ArrayAdapter<DateAndVacuum> adapter = new ArrayAdapter<DateAndVacuum>(MainActivity.this, R.layout.urun_adi, your_array_list );

                urunList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
