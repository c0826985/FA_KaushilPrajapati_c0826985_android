package com.example.fa_kaushilprajapati_c0826985_android;

import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class PlacesModel {
    int id;
    String address;
    Boolean isChecked;
    LatLng latLng;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
