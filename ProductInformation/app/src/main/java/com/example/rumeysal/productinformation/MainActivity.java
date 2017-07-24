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

	//mike

        List<String> your_array_list = new ArrayList<String>();
        your_array_list.add("foo");
        your_array_list.add("bar");


       // List<Product> productList= new ArrayList<>();
       // productList.add(new Product("kjkl","nkl","nk","oıı","nk"));
        //productList.add(new Product("kjkl","nkl","nk","oıı","nk"));

        ListView urunList=(ListView) findViewById(R.id.UrunList);
        //UrunAdapter adapter=new UrunAdapter(this, product[0]);
        //UrunAdapter adapter=new UrunAdapter(this, product[1]);
        //UrunAdapter adapter=new UrunAdapter(this, product[2]);
        //urunList.setAdapter(adapter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,android.R.id.text1,your_array_list );

        urunList.setAdapter(adapter);
    }
}
