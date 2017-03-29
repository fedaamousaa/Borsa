package com.gradproject.borsa.DataModel;

import com.google.gson.annotations.SerializedName;

import io.realm.annotations.PrimaryKey;

/**
 * Created by تكنولوجى on 15/03/2017.
 */

public class UserStocks {
    @SerializedName("id")
    @PrimaryKey
    int id;
    @SerializedName("stock")
    Stock stock;
    @SerializedName("customer")
    Customer customer;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @SerializedName("quantity")
    int quantity;

}
