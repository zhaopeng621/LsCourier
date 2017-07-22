package com.lst.lscourier.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/28.
 */

public class OrderProgressEntry implements Serializable {
    private String title;
    private String message;
    private String time;
    private String satate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String isSatate() {
        return satate;
    }

    public void setSatate(String satate) {
        this.satate = satate;
    }
}
