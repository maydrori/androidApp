package com.example.may.myapplication.model;

import com.example.may.myapplication.model.firebase.ModelFirebase;

/**
 * Created by May on 4/4/2018.
 */

public class Model {

    private static Model instance = new Model();
    ModelSql modelSql = new ModelSql();
    ModelFirebase modelFirebase = new ModelFirebase();

    public static Model instance() {
        return instance;
    }

    public String getNextWorkshopId() {
        return modelFirebase.workshops.getNextWorkshopId();
    }

    public void addWorkshop(Workshop w) {
        modelFirebase.workshops.addWorkshop(w);
    }

    public void getAllWorkshops(ModelFirebase.GetDataListener listener) {
        modelFirebase.workshops.getAllWorkshops(listener);
    }

    public void registerMemberToWorkshop(String workshopId, String userId) {
        modelFirebase.workshopsMembers.registerMemberToWorkshop(workshopId, userId);
    }

    public void unregisterMemberFromWorkshop(String workshopId, String userId) {
        modelFirebase.workshopsMembers.unregisterMemberFromWorkshop(workshopId, userId);
    }

    public void enterWaitingList(String workshopId, String userId) {
        modelFirebase.workshopsMembers.enterWaitingList(workshopId, userId);
    }

    public void leaveWaitingList(String workshopId, String userId) {
        modelFirebase.workshopsMembers.leaveWaitingList(workshopId, userId);
    }

    public void getWorkshopMembers(String workshopId, ModelFirebase.GetDataListener listener) {
        modelFirebase.workshopsMembers.getWorkshopMembers(workshopId, listener);
    }

    public void addUser(User u) {
        modelFirebase.users.addUser(u);
    }

    public void getUserById(String id, ModelFirebase.GetDataListener listener) {
        modelFirebase.users.getUserById(id, listener);
    }

    public void updateUser(User u) {modelFirebase.users.updateUser(u);}
}
