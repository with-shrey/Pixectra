package com.pixectra.app.Models;

/**
 * Created by XCODER on 2/10/2018.
 */

public class User {
    String name;
    String email;
    String profilePic;
    String phone_no;

    public User() {
    }

    public User(String name, String email, String profilePic) {
        this.name = name;
        this.email = email;
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }
}
