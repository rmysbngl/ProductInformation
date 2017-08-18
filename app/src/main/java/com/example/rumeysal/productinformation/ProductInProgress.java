package com.example.rumeysal.productinformation;

/**
 * Created by rumeysal on 7/28/17.
 */

public class ProductInProgress {
    String urunid;
    String name;

    //Cihaza yerleştirilen ürünlerin listesi için

    public ProductInProgress(String urunid, String name) {
        this.urunid = urunid;
        this.name = name;
    }

    public void setUrunid(String urunid) {
        this.urunid = urunid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrunid() {
        return urunid;
    }

    public String getName() {
        return name;
    }
}
