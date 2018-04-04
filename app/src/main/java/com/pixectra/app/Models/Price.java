package com.pixectra.app.Models;

/**
 * Created by XCODER on 3/6/2018.
 */

public class Price {
    double base, discount, total, credits;

    public Price(double base, double discount, int credits, double total) {
        this.base = base;
        this.discount = discount;
        this.total = total;
        this.credits = credits;
    }

    public Price() {

    }


    public double getBase() {
        return base;
    }


    public double getDiscount() {
        return discount;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "\n"+"\n"+"Price{" +"\n"+
                "base=" + base +"\n"+
                ", credits=" + credits + "\n" +
                ", discount=" + discount +"\n"+
                ", total=" + total +"\n"+
                '}';
    }
}
