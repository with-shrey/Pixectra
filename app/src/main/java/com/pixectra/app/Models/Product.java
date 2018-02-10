package com.pixectra.app.Models;

/**
 * Created by XCODER on 2/10/2018.
 */

public class Product {
    int id;
    String title;
    String url;
    int pics;
    int price;
    String priceText;

    public Product() {
    }

    public Product(int id, String title, String url, int pics, int price, String priceText) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.pics = pics;
        this.price = price;
        this.priceText = priceText;
    }

    public int getId() {
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

    public String getPriceText() {
        return priceText;
    }
}
