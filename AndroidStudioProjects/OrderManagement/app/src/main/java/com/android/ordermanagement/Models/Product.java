package com.android.ordermanagement.Models;

/**
 * Created by audyf on 2/5/2017.
 */
public class Product {
    private int id;
    private String name;
    private int quantity;
    private String unit;
    private double amount;

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
}
