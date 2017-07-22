package com.lst.lscourier.bean;

import java.io.Serializable;

/**
 * author describe parameter return
 */
public class UserBean implements Serializable {
    private String userid;
    private String pic;
    private String username;
    private String phone;
    private String level;//等级
    private String integral;//积分
    private String balance;//余额

    @Override
    public String toString() {
        return "UserBean{" +
                "userid='" + userid + '\'' +
                ", pic='" + pic + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", level='" + level + '\'' +
                ", integral='" + integral + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


}
