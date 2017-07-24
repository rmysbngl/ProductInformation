package com.example.rumeysal.productinformation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference products = database.getReference("message");


        List<String> your_array_list = new ArrayList<String>();

        DatabaseReference refProduct=FirebaseDatabase.getInstance().getReference().child("Products").child("Product1");
        refProduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                your_array_list.add(dataSnapshot.child("Date").getValue().toString());
                your_array_list.add(dataSnapshot.child("ID").getValue().toString());
	 	your_array_list.add(dataSnapshot.child("ProductName").getValue().toString());
                your_array_list.add(dataSnapshot.child("Unit").getValue().toString());
                your_array_list.add(dataSnapshot.child("VacuumValue").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ListView urunList=(ListView) findViewById(R.id.UrunList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,android.R.id.text1,your_array_list );

        urunList.setAdapter(adapter);
    }
}
