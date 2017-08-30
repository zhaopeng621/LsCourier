package com.lst.lscourier.bean;

import java.io.Serializable;

public class Transaction implements Serializable {

    private String left;
    private String right;

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }
}
