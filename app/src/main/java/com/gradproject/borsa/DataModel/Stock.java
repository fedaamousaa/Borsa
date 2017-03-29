package com.gradproject.borsa.DataModel;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by تكنولوجى on 08/02/2017.
 */

public class Stock  extends RealmObject {
    @SerializedName("id")
    @PrimaryKey
    int id;
    @SerializedName("type")
    int type;
    @SerializedName("company")
    Company company;
    @SerializedName("current_value")
    double current_value;
    @SerializedName("last_value")
    double last_value;
    @SerializedName("init_no")
    int init_no;
    @SerializedName("curr_no")
    int curr_no;
    @SerializedName("date_add")
    String date_add;
    @SerializedName("date_upd")
    String date_upd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public double getCurrent_value() {
        return current_value;
    }

    public void setCurrent_value(double current_value) {
        this.current_value = current_value;
    }

    public double getLast_value() {
        return last_value;
    }

    public void setLast_value(double last_value) {
        this.last_value = last_value;
    }

    public int getInit_no() {
        return init_no;
    }

    public void setInit_no(int init_no) {
        this.init_no = init_no;
    }

    public int getCurr_no() {
        return curr_no;
    }

    public void setCurrent_no(int curr_no) {
        this.curr_no = curr_no;
    }

    public String getDate_add() {
        return date_add;
    }

    public void setDate_add(String date_add) {
        this.date_add = date_add;
    }

    public String getDate_upd() {
        return date_upd;
    }

    public void setDate_upd(String date_upd) {
        this.date_upd = date_upd;
    }
}
