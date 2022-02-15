package com.example.fa_kaushilprajapati_c0826985_android;

public class PlacesModel {
    String address;
    Boolean isChecked;

    public PlacesModel(String address, Boolean isChecked) {
        this.address = address;
        this.isChecked = isChecked;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
