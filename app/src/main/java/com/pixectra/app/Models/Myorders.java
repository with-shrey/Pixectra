package com.pixectra.app.Models;

import java.io.Serializable;

/**
 * Created by user on 3/17/2018.
 */

public class Myorders implements Serializable {
    String key;
    String id;
    String name;
    String time;
    String date;
    String uploaded;
    Long amount;

    public Myorders(String id, String name, String time, String date, String uploaded, Long amount) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.date = date;
        this.uploaded = uploaded;
        this.amount = amount;
    }

    public Myorders() {
    }

    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUploaded() {
        return uploaded;
    }

    public void setUploaded(String uploaded) {
        this.uploaded = uploaded;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
