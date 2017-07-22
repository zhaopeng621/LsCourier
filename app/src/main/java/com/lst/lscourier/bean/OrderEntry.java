package com.lst.lscourier.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/15.
 */

public class OrderEntry implements Serializable {
    private String id;
    private String order_id;
    private String user_id;
    private String start_address;
    private String start_xxaddress;
    private String exit_address;
    private String exit_xxaddress;
    private String start_name;
    private String exit_name;
    private String start_phone;
    private String exit_phone;
    private String order_type;
    private String order_weight;
    private String start_time;
    private String order_time;
    private String money;
    private String order_status;
    private String distance;
    private String message;
    private String pay_type;
    private String deliveryman_id;
    private String r_time;
    private String start_long;
    private String start_lat;
    private String exit_long;
    private String exit_lat;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStart_address() {
        return start_address;
    }

    public void setStart_address(String start_address) {
        this.start_address = start_address;
    }

    public String getStart_xxaddress() {
        return start_xxaddress;
    }

    public void setStart_xxaddress(String start_xxaddress) {
        this.start_xxaddress = start_xxaddress;
    }

    public String getExit_address() {
        return exit_address;
    }

    public void setExit_address(String exit_address) {
        this.exit_address = exit_address;
    }

    public String getExit_xxaddress() {
        return exit_xxaddress;
    }

    public void setExit_xxaddress(String exit_xxaddress) {
        this.exit_xxaddress = exit_xxaddress;
    }

    public String getStart_name() {
        return start_name;
    }

    public void setStart_name(String start_name) {
        this.start_name = start_name;
    }

    public String getExit_name() {
        return exit_name;
    }

    public void setExit_name(String exit_name) {
        this.exit_name = exit_name;
    }

    public String getStart_phone() {
        return start_phone;
    }

    public void setStart_phone(String start_phone) {
        this.start_phone = start_phone;
    }

    public String getExit_phone() {
        return exit_phone;
    }

    public void setExit_phone(String exit_phone) {
        this.exit_phone = exit_phone;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getOrder_weight() {
        return order_weight;
    }

    public void setOrder_weight(String order_weight) {
        this.order_weight = order_weight;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDeliveryman_id() {
        return deliveryman_id;
    }

    public void setDeliveryman_id(String deliveryman_id) {
        this.deliveryman_id = deliveryman_id;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getR_time() {
        return r_time;
    }

    public void setR_time(String r_time) {
        this.r_time = r_time;
    }

    public String getStart_long() {
        return start_long;
    }

    public void setStart_long(String start_long) {
        this.start_long = start_long;
    }

    public String getStart_lat() {
        return start_lat;
    }

    public void setStart_lat(String start_lat) {
        this.start_lat = start_lat;
    }

    public String getExit_long() {
        return exit_long;
    }

    public void setExit_long(String exit_long) {
        this.exit_long = exit_long;
    }

    public String getExit_lat() {
        return exit_lat;
    }

    public void setExit_lat(String exit_lat) {
        this.exit_lat = exit_lat;
    }
}
