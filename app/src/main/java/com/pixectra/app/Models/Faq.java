package com.pixectra.app.Models;

public class Faq {
    String q;
    String a;

    public Faq() {
    }

    public Faq(String q, String a) {
        this.q = q;
        this.a = a;
    }

    public String getQ() {
        return q;
    }

    public String getA() {
        return a;
    }
}
