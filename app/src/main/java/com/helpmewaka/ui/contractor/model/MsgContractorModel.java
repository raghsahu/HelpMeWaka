package com.helpmewaka.ui.contractor.model;

public class MsgContractorModel {
    String cnt_id;
    String job_id;
    String task_id;
    String msg_content;
    String attachment;
    String createDt;
    String sender;
    String usrFile;
    String type;

    public String getCnt_id() {
        return cnt_id;
    }

    public void setCnt_id(String cnt_id) {
        this.cnt_id = cnt_id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getCreateDt() {
        return createDt;
    }

    public String getSender() {
        return sender;
    }

    public String getUsrFile() {
        return usrFile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUsrFile(String usrFile) {
        this.usrFile = usrFile;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    public MsgContractorModel(String cnt_id, String job_id, String task_id, String msg_content,
                              String attachment, String createDt, String sender, String usrFile,
                              String type) {


    this.cnt_id=cnt_id;
    this.job_id=job_id;
    this.task_id=task_id;
    this.msg_content=msg_content;
    this.sender=sender;
    this.attachment=attachment;
    this.createDt=createDt;
    this.usrFile=usrFile;
    this.type=type;














    }
}
