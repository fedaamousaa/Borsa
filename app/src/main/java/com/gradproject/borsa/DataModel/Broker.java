package com.gradproject.borsa.DataModel;

/**
 * Created by Shery on 17/12/2016.
 */

public class Broker extends Object{
    String name;
    String phone;
    int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
