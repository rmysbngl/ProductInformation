package com.example.rumeysal.productinformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductShowAdapter extends PagerAdapter {

    private ArrayList<Uri> images;
    private Context context;
    private LayoutInflater layoutInflater;

    public ProductShowAdapter(){

    }
    public ProductShowAdapter( Context context,ArrayList<Uri> images) {
        this.images = images;
        this.context = context;

    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.activity_product_show_adapter,container,false);
        ImageView image=(ImageView) view.findViewById(R.id.ShowProduct);

        Picasso.with(context)
                .load(images.get(position))
                .into(image);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
