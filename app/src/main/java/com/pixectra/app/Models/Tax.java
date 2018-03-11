package com.pixectra.app.Models;

/**
 * Created by XCODER on 3/10/2018.
 */

public class Tax {
    double cgst;
    double sgst;
    double cess;
    double discount;
    double threshold;
    double type;

    public Tax() {
    }

    public double getThreshold() {
        return threshold;
    }

    public double getType() {
        return type;
    }

    public double getCgst() {
        return cgst;
    }

    public double getSgst() {
        return sgst;
    }

    public double getCess() {
        return cess;
    }

    public double getDiscount() {
        return discount;
    }
}
