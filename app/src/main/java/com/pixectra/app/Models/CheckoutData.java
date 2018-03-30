package com.pixectra.app.Models;

import java.io.Serializable;

/**
 * Created by XCODER on 3/6/2018.
 */

public class CheckoutData implements Serializable {
    User user;
    Address address;
    Price price;

    public CheckoutData() {
        user = new User();
        address = new Address();
        price = new Price();
    }

    public CheckoutData(User user, Address address, Price price) {
        this.user = user;
        this.address = address;
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "CheckoutData{\n "+
                "user=" + user +"\n"+
                ", address=" + address +"\n"+
                ", price=" + price +"\n"+
                '}';
    }
}
