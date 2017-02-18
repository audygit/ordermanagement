package com.android.ordermanagement.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by challa on 2/5/2017.
 */
public class Product implements Serializable{
    @SerializedName("ItemCode")
    private String id;
    @SerializedName("ItemName")
    private String name;
    @SerializedName("QtyInCases")
    private int quantity;
    @SerializedName("QtyInUnits")
    private int quantityUts;
    @SerializedName("QtyInPackages")
    private double quantityPkgs;
    @SerializedName("price")
    private double price;
    @SerializedName("Amount")
    private double amount;
    @SerializedName("WeightInKgs")
    private double weightInKgs;
    @SerializedName("ActualQty")
    private double actualQuantity;
    @SerializedName("BilledQty")
    private double billedQuantity;
    @SerializedName("Rate")
    private String rate;
    @SerializedName("Per")
    private int per;

    public Product(){

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBilledQuantity() {
        return billedQuantity;
    }

    public void setBilledQuantity(double billedQuantity) {
        this.billedQuantity = billedQuantity;
    }

    public int getQuantityUts() {
        return quantityUts;
    }

    public void setQuantityUts(int quantityUts) {
        this.quantityUts = quantityUts;
    }

    public double getQuantityPkgs() {
        return quantityPkgs;
    }

    public void setQuantityPkgs(double quantityPkgs) {
        this.quantityPkgs = quantityPkgs;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeightInKgs() {
        return weightInKgs;
    }

    public void setWeightInKgs(double weightInKgs) {
        this.weightInKgs = weightInKgs;
    }

    public double getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(double actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public int getPer() {
        return per;
    }

    public void setPer(int per) {
        this.per = per;
    }

}
