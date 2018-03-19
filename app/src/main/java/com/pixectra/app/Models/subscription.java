package com.pixectra.app.Models;

import java.io.Serializable;

/**
 * Created by user on 3/19/2018..
 */

public class subscription implements Serializable{
    String key;
    String id;
    String number;
    String title;
    String type;
    String starting_date;
    Long amount;

    public subscription()
    {}

    public subscription(String id, String number, String title, String type, String starting_date, Long amount) {
        this.id = id;
        this.number = number;
        this.title = title;
        this.type = type;
        this.starting_date = starting_date;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStarting_date() {
        return starting_date;
    }

    public void setStarting_date(String starting_date) {
        this.starting_date = starting_date;
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
