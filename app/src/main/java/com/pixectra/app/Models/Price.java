package com.pixectra.app.Models;

/**
 * Created by XCODER on 3/6/2018.
 */

public class Price {
    double base, cgst, sgst,cess,discount, total;

    public Price(double base, double cgst, double sgst, double cess, double discount, double total) {
        this.base = base;
        this.cgst = cgst;
        this.sgst = sgst;
        this.cess = cess;
        this.discount = discount;
        this.total = total;
    }

    public Price() {

    }

    public double getCess() {
        return cess;
    }

    public double getBase() {
        return base;
    }

    public double getCgst() {
        return cgst;
    }

    public double getSgst() {
        return sgst;
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
                ", cgst=" + cgst +"\n"+
                ", sgst=" + sgst +"\n"+
                ", cess=" + cess +"\n"+
                ", discount=" + discount +"\n"+
                ", total=" + total +"\n"+
                '}';
    }
}
