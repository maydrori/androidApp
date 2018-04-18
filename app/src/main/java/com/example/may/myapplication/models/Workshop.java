package com.example.may.myapplication.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by May on 3/31/2018.
 */

@Entity
public class Workshop implements Serializable {

    @PrimaryKey
    @NonNull
    private String id;
    private String teacherId;
    private String place;
    private long date;
    private String genre;
    private String level;
    private int maxParticipants;

    private long lastUpdated;

    public Workshop(String id, long date, String teacherId, String place, String genre, String level, int maxParticipants) {
        this.id = id;
        this.date = date;
        this.teacherId = teacherId;
        this.place = place;
        this.genre = genre;
        this.level = level;
        this.maxParticipants = maxParticipants;
    }

    public Workshop() {}

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
