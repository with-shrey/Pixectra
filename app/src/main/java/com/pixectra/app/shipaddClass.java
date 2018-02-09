package com.pixectra.app;

import java.io.Serializable;

/**
 * Created by Prashu on 2/7/2018.
 * Used to store shipping address of the user
 */

public class shipaddClass implements Serializable {

    String name;
    String addNo;
    String street;
    String pincode;
    String city;
    String state;
    String mobile;
    String optional;

    public shipaddClass(String name, String addNo, String street, String pincode, String city, String state, String mobile,String optional) {
        this.name = name;
        this.addNo = addNo;
        this.street = street;
        this.pincode = pincode;
        this.city = city;
        this.state = state;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public String getAddNo() {
        return addNo;
    }

    public String getStreet() {
        return street;
    }

    public String getPincode() {
        return pincode;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getMobile() {
        return mobile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddNo(String addNo) {
        this.addNo = addNo;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOptional() {
        return optional;
    }

    public void setOptional(String optional) {
        this.optional = optional;
    }

}
