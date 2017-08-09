package com.example.rumeysal.productinformation;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by rumeysal on 7/22/17.
 */

public class Product {
    private ArrayList<Uri> productImages;
    private String urunadı;
    private String id;
    private String kurumadı;


    public Product(String id, String urunadı) {
        this.urunadı = urunadı;
        this.id = id;
    }

    public Product(String id, String urunadı, String kurumadı) {
        this.productImages = productImages;
        this.urunadı = urunadı;
        this.id = id;
        this.kurumadı = kurumadı;
    }

    public String getUrunadı() {
        return urunadı;
    }

    public void setUrunadı(String urunadı) {
        this.urunadı = urunadı;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getKurumadı() {
        return kurumadı;
    }

    public void setProductImages(ArrayList<Uri> productImages) {
        this.productImages = productImages;
    }

    public ArrayList<Uri> getProductImages() {
        return productImages;
    }
}
