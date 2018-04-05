package com.pixectra.app.Models;

import java.io.Serializable;

/**
 * Created by user on 3/17/2018.
 */

public class Myorders implements Serializable {
    String fKey;
    String payId;
    String time;
    String date;
    boolean uploaded;
    boolean success;
    Double amount;

    public Myorders() {
    }

    public Myorders(String fKey, String payId, String time, String date, boolean uploaded, boolean success, Double amount) {
        this.fKey = fKey;
        this.payId = payId;
        this.time = time;
        this.date = date;
        this.uploaded = uploaded;
        this.success = success;
        this.amount = amount;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getfKey() {
        return fKey;
    }

    public String getPayId() {
        return payId;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public Double getAmount() {
        return amount;
    }
}
