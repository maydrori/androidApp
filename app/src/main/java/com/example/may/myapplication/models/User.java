package com.example.may.myapplication.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by May on 4/3/2018.
 */

@Entity
public class User {

    @PrimaryKey
    @NonNull
    private String id;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private String instegramLink;
    @ColumnInfo
    private String facebookLink;
    @ColumnInfo
    private String phone;
    @ColumnInfo
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

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstegramLink() {
        return instegramLink;
    }

    public void setInstegramLink(String instegramLink) {
        this.instegramLink = instegramLink;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
