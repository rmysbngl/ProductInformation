package com.example.rumeysal.productinformation;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by rumeysal on 7/22/17.
 */

public class Product {
    private String urunadı;
    private String id;
    private String kurumadı;

    //Yeni ürün gelidğinde ürün verilerini tutmak için

    public Product(String id, String urunadı) {
        this.urunadı = urunadı;
        this.id = id;
    }

    public Product(String id, String urunadı, String kurumadı) {
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


}
