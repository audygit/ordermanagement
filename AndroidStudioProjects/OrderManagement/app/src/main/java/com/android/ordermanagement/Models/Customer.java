package com.android.ordermanagement.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by challa on 2/7/2017.
 */
public class Customer implements Serializable{
    @SerializedName("Customer_Id")
    private String id;
    @SerializedName("Customer_Name")
    private String name;
    private String date;
    private String lastOrder;
    private String image;
    public Customer(String id,String name, String image){
        setId(id);
        setName(name);
        setDate("23/2/2017");
        setLastOrder("EROTICA(15 units)");
        setImage(image);
    }
    public Customer(String id,String name){
        setId(id);
        setName(name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLastOrder() {
        return lastOrder;
    }

    public void setLastOrder(String lastOrder) {
        this.lastOrder = lastOrder;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
