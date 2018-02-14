package com.pixectra.app.Models;

/**
 * Created by XCODER on 2/10/2018.
 */

public class Product {
    private String id;
    private String title;
    private String url;
    private int pics;
    private int price;

    public Product() {
    }

    public Product(String id, String title, String url, int pics, int price) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.pics = pics;
        this.price = price;
    }

    public String getId() {
        return id;
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
