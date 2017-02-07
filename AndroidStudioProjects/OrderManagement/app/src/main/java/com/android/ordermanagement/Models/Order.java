package com.android.ordermanagement.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by audyf on 2/5/2017.
 */
public class Order implements Serializable{
    @SerializedName("id")
    private int id;
    @SerializedName("customer_id")
    private String customerId;
    @SerializedName("customer_name")
    private String customerName;
    @SerializedName("total_amount")
    private double totalAmount;
    @SerializedName("products")
    private ArrayList<Product> products;
    @SerializedName("service_tax")
    private double serviceTax;
    @SerializedName("vat")
    private double vat;
    public  Order(int id,String customerId,String customerName){
        setId(id);
        setCustomerId(customerId);
        setCustomerName(customerName);
    }
    public  Order(int id,String customerId,String customerName,ArrayList<Product> products){
        setId(id);
        setCustomerId(customerId);
        setCustomerName(customerName);
        setProducts(products);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public double getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(double serviceTax) {
        this.serviceTax = serviceTax;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }
}
