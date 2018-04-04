package com.example.may.myapplication.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by May on 3/31/2018.
 */

public class Workshop {
    private String id;
    private String teacherId;
    private String place;
    private Date date;
    private String level;
    private int maxParticipants;
    private List<String> members;

    public Workshop(String id, Date date, String teacherId, String place, String level, int maxParticipants) {
        this.id = id;
        this.date = date;
        this.teacherId = teacherId;
        this.place = place;
        this.level = level;
        this.maxParticipants = maxParticipants;
        this.members = new ArrayList<String>();
    }

    public String getId () {
        return this.id;
    }
    public Date getDate () {
        return this.date;
    }
    public String getTeacherId () {
        return this.teacherId;
    }
    public String getPlace () {
        return this.place;
    }
    public String getLevel () {
        return this.level;
    }
    public int getMaxParticipants () {
        return this.maxParticipants;
    }

    public void addMember(String memberId) {
        this.members.add(memberId);
    }
}
