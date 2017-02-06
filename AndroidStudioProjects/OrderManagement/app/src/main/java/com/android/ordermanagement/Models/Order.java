package com.android.ordermanagement.Models;

/**
 * Created by audyf on 2/5/2017.
 */
public class Order {
    private int id;
    private String customerId;
    private String customerName;
    private double totalAmount;
    public  Order(int id,String customerId,String customerName){
        setId(id);
        setCustomerId(customerId);
        setCustomerName(customerName);
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
}
