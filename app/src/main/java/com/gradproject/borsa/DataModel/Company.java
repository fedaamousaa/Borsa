package com.gradproject.borsa.DataModel;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by تكنولوجى on 08/02/2017.
 */

public class Company extends RealmObject {
    @SerializedName("id")
    @PrimaryKey
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("name_ar")
    String name_ar;
    @SerializedName("symbol")
    String symbol;
    @SerializedName("phone")
    String phone;
    @SerializedName("email")
    String email;
    @SerializedName("website")
    String website;
    @SerializedName("longitude")
    Double longitude;
    @SerializedName("latitude")
    Double latitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_ar() {
        return name_ar;
    }

    public void setName_ar(String name_ar) {
        this.name_ar = name_ar;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
