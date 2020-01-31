package com.helpmewaka.ui.contractor.model;

public class ContractorLiveLocation {
    String cnt_id;
    String task_id;
    String task_code;
    String job_id;
    String lognitude;
    String latitude;
    String postDt;


    public String getCnt_id() {
        return cnt_id;
    }

    public void setCnt_id(String cnt_id) {
        this.cnt_id = cnt_id;
    }

    public String getTask_id() {
        return task_id;
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

    public String getTask_code() {
        return task_code;
    }

    public void setTask_code(String task_code) {
        this.task_code = task_code;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public ContractorLiveLocation(String cnt_id, String task_id, String task_code,
                                  String job_id, String lognitude, String latitude,
                                  String postDt) {

        this.cnt_id=cnt_id;
        this.job_id=job_id;
        this.latitude=latitude;
        this.lognitude=lognitude;
        this.task_code=task_code;
        this.task_id=task_id;
        this.postDt=postDt;


    }


}
