package com.example.may.myapplication.models;

import java.io.Serializable;

/**
 * Created by May on 4/3/2018.
 */

public class User implements Serializable {

    private String id;
    private String name;
    private String instegramLink;
    private String facebookLink;
    private String phone;
    private String imageUrl;

    public User() {}

    public User(String id) {
        this.id = id;
    }

    public User(String id, String name, String phone, String instegramLink, String facebookLink) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.instegramLink = instegramLink;
        this.facebookLink = facebookLink;
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
    public String getImageUrl() {
        return imageUrl;
    }

    public User setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
