package com.android.ordermanagement.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by challa on 18/02/17.
 */

public class ProductListItem implements Serializable {

    @SerializedName("Item_Code")
    private String code;
    @SerializedName("Item_Name")
    private String name;
    @SerializedName("Item_Group")
    private String group;
    @SerializedName("Price")
    private double price;
    @SerializedName("Uom_Per_EachUnit")
    private String uomPerEchUnit;
    @SerializedName("Fixed_Qty")
    private String fixedQty;
    @SerializedName("No_Of_Unit_Per_Case")
    private int units;
    @SerializedName("No_Of_Packs")
    private double packs;
    @SerializedName("Weight")
    private String weight;
    @SerializedName("Free_Qty_Packs")
    private int freeQty;
    @SerializedName("Per")
    private int per;
    @SerializedName("Case_Units")
    private String caseUnit;
    private String Uom;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUomPerEchUnit() {
        return uomPerEchUnit;
    }

    public void setUomPerEchUnit(String uomPerEchUnit) {
        this.uomPerEchUnit = uomPerEchUnit;
    }

    public String getFixedQty() {
        return fixedQty;
    }

    public void setFixedQty(String fixedQty) {
        this.fixedQty = fixedQty;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public double getPacks() {
        return packs;
    }

    public void setPacks(double packs) {
        this.packs = packs;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(int freeQty) {
        this.freeQty = freeQty;
    }

    public int getPer() {
        return per;
    }

    public void setPer(int per) {
        this.per = per;
    }

    public String getCaseUnit() {
        return caseUnit;
    }

    public void setCaseUnit(String caseUnit) {
        this.caseUnit = caseUnit;
    }

    public String getUom() {
        return Uom;
    }

    public void setUom(String uom) {
        Uom = uom;
    }
}
