package com.helpmewaka.ui.activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 29/11/2019.
 */
public class ServieSubCateData {
    @SerializedName("SubCat_SERV_ID")
    @Expose
    private String subCatSERVID;
    @SerializedName("Service_Subcategory")
    @Expose
    private String serviceSubcategory;

    public String getSubCatSERVID() {
        return subCatSERVID;
    }

    public void setSubCatSERVID(String subCatSERVID) {
        this.subCatSERVID = subCatSERVID;
    }

    public String getServiceSubcategory() {
        return serviceSubcategory;
    }

    public void setServiceSubcategory(String serviceSubcategory) {
        this.serviceSubcategory = serviceSubcategory;
    }
}
