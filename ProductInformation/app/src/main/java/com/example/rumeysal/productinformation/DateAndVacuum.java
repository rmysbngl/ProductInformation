package com.example.rumeysal.productinformation;

/**
 * Created by rumeysal on 7/24/17.
 */

public class DateAndVacuum {

    private String date;
    private String VacuumValue;

    public DateAndVacuum(String date, String vacuumValue) {
        this.date = date;
        VacuumValue = vacuumValue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVacuumValue() {
        return VacuumValue;
    }

    public void setVacuumValue(String vacuumValue) {
        VacuumValue = vacuumValue;
    }
}
