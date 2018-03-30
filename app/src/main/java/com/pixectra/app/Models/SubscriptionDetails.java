package com.pixectra.app.Models;

/**
 * Created by Suhail on 3/14/2018.
 */

public class SubscriptionDetails {

    String Title;
    String Sub_title;
    String details;

    public SubscriptionDetails(String title, String sub_title, String details) {
        Title = title;
        Sub_title = sub_title;
        this.details = details;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSub_title() {
        return Sub_title;
    }

    public void setSub_title(String sub_title) {
        Sub_title = sub_title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
