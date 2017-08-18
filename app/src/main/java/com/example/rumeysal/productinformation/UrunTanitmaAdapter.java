package com.example.rumeysal.productinformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.zip.Inflater;

//Urun tanıtırken resimlerin kayar biçimde kurulması için oluşturulan sayfa
public class UrunTanitmaAdapter extends PagerAdapter {

   private ArrayList<Bitmap> images;
   private  Context context;
    private LayoutInflater layoutInflater;

    public UrunTanitmaAdapter(){

    }

    public UrunTanitmaAdapter(Context context,ArrayList<Bitmap> images) {
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
        View view=layoutInflater.inflate(R.layout.activity_urun_tanitma_adapter,container,false);
        ImageView image=(ImageView) view.findViewById(R.id.UrunImages);
        image.setImageBitmap(images.get(position));
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}

