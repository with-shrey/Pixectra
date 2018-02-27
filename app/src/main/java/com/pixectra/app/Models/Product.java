package com.pixectra.app.Models;

import com.google.firebase.database.Exclude;

/**
 * Created by XCODER on 2/10/2018.
 */

public class Product {
    @Exclude
    private String id;
    private String type;
    private String title;
    private String url;
    private int pics;
    private int price;

    public Product() {
        id = type + "~" + title;
    }

    public Product(String title, String url, int pics, int price) {
        this.title = title;
        this.url = url;
        this.pics = pics;
        this.price = price;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public int getPics() {
        return pics;
    }

    public int getPrice() {
        return price;
    }
}
