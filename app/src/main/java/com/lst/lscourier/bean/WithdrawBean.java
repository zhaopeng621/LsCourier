package com.lst.lscourier.bean;

import java.io.Serializable;

/**
 * Created by lst718-011 on 2017/8/9.
 */
public class WithdrawBean implements Serializable {

    private String orderId;
    private String trueName;
    private String bankName;
    private String cardNumber;
    private String withdrawNumber;
    private String orderTime;

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }
    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getWithdrawNumber() {
        return withdrawNumber;
    }

    public void setWithdrawNumber(String withdrawNumber) {
        this.withdrawNumber = withdrawNumber;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
