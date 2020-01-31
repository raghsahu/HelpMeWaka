package com.helpmewaka.ui.activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 29/11/2019.
 */
public class Custom_Spinner_Model {

    @SerializedName("results")
    @Expose
    private String results;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<Custom_Spin_Data> data = null;

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Custom_Spin_Data> getData() {
        return data;
    }

    public void setData(List<Custom_Spin_Data> data) {
        this.data = data;
    }

}
