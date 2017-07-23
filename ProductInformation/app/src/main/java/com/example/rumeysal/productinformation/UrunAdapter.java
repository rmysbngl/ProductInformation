package com.example.rumeysal.productinformation;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rumeysal on 7/24/17.
 */

public class UrunAdapter extends ArrayAdapter<DateAndVacuum> {

    List<DateAndVacuum> array;
    Context context;

    public UrunAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<DateAndVacuum> objects) {
        super(context, resource, objects);
        this.array = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.urun_adi, parent);
        }

        TextView tvBilgi = convertView.findViewById(R.id.UrunBilgi);
        TextView tvVeri = convertView.findViewById(R.id.UrunVeri);

        tvBilgi.setText(array.get(position).getDate());
        tvVeri.setText(array.get(position).getVacuumValue());

        return convertView;
    }
}
