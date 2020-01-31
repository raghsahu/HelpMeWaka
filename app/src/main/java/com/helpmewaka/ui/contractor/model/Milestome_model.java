package com.helpmewaka.ui.contractor.model;

public class Milestome_model {
    String job_id;
    String task_id;
    String cnt_id;
    String rate_caption;
    String amount;


    public Milestome_model(String job_id, String task_id, String cnt_id,
                           String rate_caption, String amount) {
        this.job_id=job_id;
        this.task_id=task_id;
        this.cnt_id=cnt_id;
        this.rate_caption=rate_caption;
        this.amount=amount;

    }

    public String getJob_id() {
        return job_id;
    }

    public String getTask_id() {
        return task_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRate_caption() {
        return rate_caption;
    }

    public void setRate_caption(String rate_caption) {
        this.rate_caption = rate_caption;
    }

    public String getCnt_id() {
        return cnt_id;
    }

    public void setCnt_id(String cnt_id) {
        this.cnt_id = cnt_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }
}
