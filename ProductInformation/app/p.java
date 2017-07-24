package com.example.rumeysal.productinformation;

/**
 * Created by rumeysal on 7/22/17.
 */

public class Product {
    private String urunadı;
    private String id;
    private String tarih;
    private String unity;
    private String VacuumValue;

    public Product(String urunadı, String id, String tarih, String unity, String vacuumValue) {
        this.urunadı = urunadı;
        this.id = id;
        this.tarih = tarih;
        this.unity = unity;
        VacuumValue = vacuumValue;
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

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getUnity() {
        return unity;
    }

    public void setUnity(String unity) {
        this.unity = unity;
    }

    public String getVacuumValue() {
        return VacuumValue;
    }

    public void setVacuumValue(String vacuumValue) {
        VacuumValue = vacuumValue;
    }
}
