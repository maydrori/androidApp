package com.example.may.myapplication.models;

import java.util.List;

/**
 * Created by May on 4/17/2018.
 */

public class WorkshopMembers {

    String workshopId;
    List<String> registered;
    List<String> waitingList;

    public WorkshopMembers() {}

    public WorkshopMembers(List<String> registered,
            List<String> waitingList) {
        this.registered = registered;
        this.waitingList = waitingList;
    }

    public List<String> getRegistered() {
        return registered;
    }

    public List<String> getWaitingList() {
        return waitingList;
    }
}
