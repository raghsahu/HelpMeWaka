package com.helpmewaka.ui.model;

public class MsgCoustomerModel {
    String msg_id;
    String clt_id;
    String job_id;
    String task_id;
    String msg_content;
    String createDt;
    String attachment;
    String sender;
    String usrFile;
    String stat;
    String type;



    public MsgCoustomerModel(String msg_id, String clt_id, String job_id, String task_id,
                             String msg_content, String createDt, String attachment,
                             String sender, String usrFile, String stat, String type) {

        this.msg_id=msg_id;
        this.clt_id=clt_id;
        this.job_id=job_id;
        this.task_id=task_id;
        this.msg_content=msg_content;
        this.createDt=createDt;
        this.attachment=attachment;
        this.sender=sender;
        this.usrFile=usrFile;
        this.stat=stat;
        this.type=type;


    }

    public String getMsg_content() {
        return msg_content;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    public String getUsrFile() {
        return usrFile;
    }

    public void setUsrFile(String usrFile) {
        this.usrFile = usrFile;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }
}
