package com.android.ordermanagement.Models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by audyf on 2/21/2017.
 */
public class SalesOrder implements Serializable{

    private String id;
    private String date;
    private String customerName;
    private String salesmenCode;
    private String saleOrderType;
    private String transporter;
    private double totalQtyPack;
    private double totalQtyKgs;
    private double totalQtyCases;
    private double totalAmount;
    private String taxClass;
    private double taxpercent;
    private double taxAmount;
    private double subTotal;
    private String status;
    private String discountType;
    private String destination;
    private String remarks;

    private ArrayList<Product> products=new ArrayList<>();

    public SalesOrder(){

    }
    public SalesOrder (String id,String date,String customerName,double totalAmount){
        setId(id);
        setDate(date);
        setCustomerName(customerName);
        setTotalAmount(totalAmount);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSalesmenCode() {
        return salesmenCode;
    }

    public void setSalesmenCode(String salesmenCode) {
        this.salesmenCode = salesmenCode;
    }

    public String getSaleOrderType() {
        return saleOrderType;
    }

    public void setSaleOrderType(String saleOrderType) {
        this.saleOrderType = saleOrderType;
    }

    public String getTransporter() {
        return transporter;
    }

    public void setTransporter(String transporter) {
        this.transporter = transporter;
    }

    public double getTotalQtyPack() {
        return totalQtyPack;
    }

    public void setTotalQtyPack(double totalQtyPack) {
        this.totalQtyPack = totalQtyPack;
    }

    public double getTotalQtyKgs() {
        return totalQtyKgs;
    }

    public void setTotalQtyKgs(double totalQtyKgs) {
        this.totalQtyKgs = totalQtyKgs;
    }

    public double getTotalQtyCases() {
        return totalQtyCases;
    }

    public void setTotalQtyCases(double totalQtyCases) {
        this.totalQtyCases = totalQtyCases;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTaxClass() {
        return taxClass;
    }

    public void setTaxClass(String taxClass) {
        this.taxClass = taxClass;
    }

    public double getTaxpercent() {
        return taxpercent;
    }

    public void setTaxpercent(double taxpercent) {
        this.taxpercent = taxpercent;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
