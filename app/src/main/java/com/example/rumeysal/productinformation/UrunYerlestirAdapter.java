package com.example.rumeysal.productinformation;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rumeysal on 7/25/17.
 */


//Cihaza yerleştirilen ürünlerin ListView eyerleşmesi için gerekli Adapter
public class UrunYerlestirAdapter extends ArrayAdapter<ProductInProgress> {

    Context context;
    List<ProductInProgress> product;

    public UrunYerlestirAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ProductInProgress> objects) {
        super(context, resource, objects);
        this.context=context;
        this.product=objects;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.urunyerlestirsatir, parent,false);
        }
        TextView id=(TextView) convertView.findViewById(R.id.newProductID);
        TextView name=(TextView) convertView.findViewById(R.id.newProductName);
        id.setText(product.get(position).urunid);
        name.setText(product.get(position).name);


        return convertView;

    }
}
