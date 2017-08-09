package com.example.rumeysal.productinformation;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.urun_adi, parent,false);
        }

        TextView tvBilgi = convertView.findViewById(R.id.UrunBilgi);
        TextView tvVacuum = convertView.findViewById(R.id.UrunVacuum);
        TextView  tvGas=convertView.findViewById(R.id.UrunGas);
        TextView  tvPlasma=convertView.findViewById(R.id.UrunPlazma);

        tvBilgi.setText(array.get(position).getDate());
        tvVacuum.setText("Vacuum: "+array.get(position).getVacuumValue());
        tvGas.setText("Gas:    "+array.get(position).getGasValue());
        tvPlasma.setText("Plasma: "+array.get(position).getPlasmaValue());

        return convertView;
    }
}
