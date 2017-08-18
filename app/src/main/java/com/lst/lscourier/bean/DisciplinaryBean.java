package com.lst.lscourier.bean;

import java.io.Serializable;

/**
 * Created by lst718-011 on 2017/8/9.
 */
public class DisciplinaryBean implements Serializable {

    private String disciplinaryId;
    private String disciplinaryType;
    private String disciplinaryMsg;
    private String disciplinaryTime;
    private String disciplinaryMoney;

    public String getDisciplinaryId() {
        return disciplinaryId;
    }

    public void setDisciplinaryId(String disciplinaryId) {
        this.disciplinaryId = disciplinaryId;
    }

    public String getDisciplinaryType() {
        return disciplinaryType;
    }

    public void setDisciplinaryType(String disciplinaryType) {
        this.disciplinaryType = disciplinaryType;
    }

    public String getDisciplinaryMsg() {
        return disciplinaryMsg;
    }

    public void setDisciplinaryMsg(String disciplinaryMsg) {
        this.disciplinaryMsg = disciplinaryMsg;
    }

    public String getDisciplinaryTime() {
        return disciplinaryTime;
    }

    public void setDisciplinaryTime(String disciplinaryTime) {
        this.disciplinaryTime = disciplinaryTime;
    }

    public String getDisciplinaryMoney() {
        return disciplinaryMoney;
    }

    public void setDisciplinaryMoney(String disciplinaryMoney) {
        this.disciplinaryMoney = disciplinaryMoney;
    }
}
