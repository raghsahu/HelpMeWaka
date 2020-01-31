package com.helpmewaka.ui.model;

public class CustomerLiveLocation {
    String clt_id;
    String job_code;
    String job_id;
    String lognitude;
    String latitude;
    String postDt;


    public CustomerLiveLocation(String clt_id, String job_code, String job_id,
                                String lognitude, String latitude, String postDt) {

        this.clt_id=clt_id;
        this.job_code=job_code;
        this.job_id=job_id;
        this.lognitude=lognitude;
        this.latitude=latitude;
        this.postDt=postDt;


    }

    public String getJob_code() {
        return job_code;
    }

    public String getPostDt() {
        return postDt;
    }

    public void setPostDt(String postDt) {
        this.postDt = postDt;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLognitude() {
        return lognitude;
    }

    public void setLognitude(String lognitude) {
        this.lognitude = lognitude;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public void setJob_code(String job_code) {
        this.job_code = job_code;
    }

    public String getClt_id() {
        return clt_id;
    }

    public void setClt_id(String clt_id) {
        this.clt_id = clt_id;
    }
}
