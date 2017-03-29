package com.gradproject.borsa.DataModel;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by تكنولوجى on 01/03/2017.
 */

public class Customer extends RealmObject {
    @SuppressWarnings("id")
    @PrimaryKey
    int id;
    @SerializedName("first_name")
    String first_name;
    @SuppressWarnings("last_name")
    String last_name;
    @SerializedName("e_mail")
    String e_mail;
    @SuppressWarnings("phone")
    String phone;
    @SerializedName("stock_nomber")
    int stock_nomber;


    public int getStock_nomber() {
        return stock_nomber;
    }
    public void setStock_nomber(int stock_nomber) {
        this.stock_nomber = stock_nomber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
