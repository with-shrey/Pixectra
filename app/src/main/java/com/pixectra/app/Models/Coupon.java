package com.pixectra.app.Models;

/**
 * Created by XCODER on 3/10/2018.
 */

public class Coupon {
    private String startDate; // dd/mm/yyyy
    private String endDate; //dd/mm/yyyy
    private String couponCode;
    private int type; // TODO: 0 For percent 1 For Amount
    private int threshold; //  Minimum Amount
    private double discount; // either percent or amount
    private boolean current;

    public Coupon() {
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public int getType() {
        return type;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public double getDiscount() {
        return discount;
    }
}
