package com.pixectra.app.Models;

/**
 * Created by Suhail on 3/14/2018.
 */

public class SubscriptionDetails {

    String title;
    String type;
    String price;
    String no_of_books;
    String desc;
    public SubscriptionDetails()
    {

    }

    public SubscriptionDetails(String title, String type, String price, String no_of_books, String desc) {
        this.title = title;
        this.type = type;
        this.price = price;
        this.no_of_books = no_of_books;
        this.desc = desc;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNo_of_books() {
        return no_of_books;
    }

    public void setNo_of_books(String no_of_books) {
        this.no_of_books = no_of_books;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}