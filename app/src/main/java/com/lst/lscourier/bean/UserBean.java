package com.lst.lscourier.bean;

import java.io.Serializable;

/**
 * author describe parameter return
 */
public class UserBean implements Serializable {
    private String id;
    private String username;
    private String id_card;
    private String status;
    private String is_pay;
    private String money;
    private String pic;
    private String today_money;
    private String y_day_money;
    private String all_money;
    private String exit_order;
    private String order;
    private String month_money;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_pay() {
        return is_pay;
    }

    public void setIs_pay(String is_pay) {
        this.is_pay = is_pay;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getToday_money() {
        return today_money;
    }

    public void setToday_money(String today_money) {
        this.today_money = today_money;
    }

    public String getY_day_money() {
        return y_day_money;
    }

    public void setY_day_money(String y_day_money) {
        this.y_day_money = y_day_money;
    }

    public String getAll_money() {
        return all_money;
    }

    public void setAll_money(String all_money) {
        this.all_money = all_money;
    }

    public String getExit_order() {
        return exit_order;
    }

    public void setExit_order(String exit_order) {
        this.exit_order = exit_order;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getMonth_money() {
        return month_money;
    }

    public void setMonth_money(String month_money) {
        this.month_money = month_money;
    }
}
