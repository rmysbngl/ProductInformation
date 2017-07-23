package com.example.rumeysal.productinformation;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rumeysal on 7/22/17.
 */

public class UrunAdapter extends BaseAdapter{
    private String veri;
    private LayoutInflater inflater;


   UrunAdapter(Activity activity, String veri) {
        this.veri=veri;
        inflater= (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View satir=inflater.inflate(R.layout.urun_adi,null);
        TextView value=(TextView) satir.findViewById(R.id.UrunBilgi);
        TextView bilgi=(TextView) satir.findViewById(R.id.UrunVeri);


        value.setText("ürün adi");
        bilgi.setText(veri);



        //bilgi.setText();

        return satir;
    }

}
