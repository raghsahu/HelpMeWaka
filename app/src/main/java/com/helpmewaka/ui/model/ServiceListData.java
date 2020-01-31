package com.helpmewaka.ui.model;

/**
 * Created by Ravindra Birla on 20/09/2019.
 */
public class ServiceListData {
    public String SERV_ID;
    public String Service_Title;

    public ServiceListData(String s){
        Service_Title = s;
        SERV_ID = "-1";

    }

    @Override
    public String toString() {
        return Service_Title;
    }

}
