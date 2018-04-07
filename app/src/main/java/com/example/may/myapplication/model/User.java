package com.example.may.myapplication.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by May on 4/3/2018.
 */

public class User implements Serializable {

    private String id;
    private String name;
    private String instegramLink;
    private String facebookLink;
    private String phone;
//    private ? image;

    public User() {}

    public User(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getInstegramLink() {
        return instegramLink;
    }
    public String getFacebookLink() {
        return facebookLink;
    }
    public String getPhone() {
        return phone;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public User setInstegramLink(String instegramLink) {
        this.instegramLink = instegramLink;
        return this;
    }

    public User setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
        return this;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }
}
