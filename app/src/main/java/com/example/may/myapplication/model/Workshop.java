package com.example.may.myapplication.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by May on 3/31/2018.
 */

public class Workshop implements Serializable {
    private String id;
    private String teacherId;
    private String place;
    private long date;
    private String genre;
    private String level;
    private int maxParticipants;

    public Workshop(long date, String teacherId, String place, String genre, String level, int maxParticipants) {
        this.id = Model.instance().getNextWorkshopId();
        this.date = date;
        this.teacherId = teacherId;
        this.place = place;
        this.genre = genre;
        this.level = level;
        this.maxParticipants = maxParticipants;
    }

    public Workshop() {}

    public String getId () {
        return this.id;
    }
    public long getDate () {
        return this.date;
    }
    public String getTeacherId () {
        return this.teacherId;
    }
    public String getPlace () {
        return this.place;
    }
    public String getGenre () {return this.genre;}
    public String getLevel () {
        return this.level;
    }
    public int getMaxParticipants () {
        return this.maxParticipants;
    }
}
