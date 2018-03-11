package com.pixectra.app.Models;

/**
 * Created by XCODER on 3/6/2018.
 */

public class CheckoutData {
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

    public void setUser(User user) {
        this.user = user;
    }

    public void setAddress(Address address) {
        this.address = address;
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
