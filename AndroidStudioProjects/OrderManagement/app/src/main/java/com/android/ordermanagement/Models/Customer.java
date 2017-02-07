package com.android.ordermanagement.Models;

/**
 * Created by challa on 2/7/2017.
 */
public class Customer {
    private int id;
    private String name;
    private String date;
    private String lastOrder;
    public Customer(int id,String name){
        setId(id);
        setName(name);
        setDate("23/2/2017");
        setLastOrder("EROTICA(15 units)");
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
}
