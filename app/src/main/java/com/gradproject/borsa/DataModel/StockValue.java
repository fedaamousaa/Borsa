package com.gradproject.borsa.DataModel;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by تكنولوجى on 11/03/2017.
 */

public class StockValue extends RealmObject{
    @SerializedName("id")
    @PrimaryKey
    int id;
    @SerializedName("stock_id")
    int stock_id;
    @SerializedName("date_add")
    Date date_add;
    @SerializedName("value")
    Double value;

    public StockValue() {
    }

    public StockValue(int id, int stock_id, Date date_add, Double value) {
        this.id = id;
        this.stock_id = stock_id;
        this.date_add = date_add;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStock_id() {
        return stock_id;
    }

    public void setStock_id(int stock_id) {
        this.stock_id = stock_id;
    }

    public Date getDate_add() {
        return date_add;
    }

    public void setDate_add(Date date_add) {
        this.date_add = date_add;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
