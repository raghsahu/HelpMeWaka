package com.helpmewaka.ui.activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raghvendra Sahu on 29/11/2019.
 */
public class Custom_Spin_Data {

    @SerializedName("SERV_ID")
    @Expose
    private String sERVID;
    @SerializedName("Service_Title")
    @Expose
    private String serviceTitle;
    @SerializedName("servie_sub_cate")
    @Expose
    private List<ServieSubCateData> servieSubCate = new ArrayList<>();

    public String getSERVID() {
        return sERVID;
    }

    public void setSERVID(String sERVID) {
        this.sERVID = sERVID;
    }

    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public List<ServieSubCateData> getServieSubCate() {
        return servieSubCate;
    }

    public void setServieSubCate(List<ServieSubCateData> servieSubCate) {
        this.servieSubCate = servieSubCate;
    }

}
