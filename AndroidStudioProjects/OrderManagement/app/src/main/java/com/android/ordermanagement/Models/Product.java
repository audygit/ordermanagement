package com.android.ordermanagement.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by audyf on 2/5/2017.
 */
public class Product implements Serializable{
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("unit")
    private String unit;
    @SerializedName("amount")
    private double amount;
    @SerializedName("billed_quantity")
    private int billedQuantity;

    public Product(){

    }
    public Product(int id,String name,int quantity,String unit,double amount){
        setId(id);
        setAmount(amount);
        setName(name);
        setQuantity(quantity);
        setUnit(unit);
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getBilledQuantity() {
        return billedQuantity;
    }

    public void setBilledQuantity(int billedQuantity) {
        this.billedQuantity = billedQuantity;
    }
}
