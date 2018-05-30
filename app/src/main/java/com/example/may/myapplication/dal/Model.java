package com.example.may.myapplication.dal;

import android.graphics.Bitmap;

import com.example.may.myapplication.dal.firebase.ModelFirebase;
import com.example.may.myapplication.dal.firebase.WorkshopsMembersFirebase;
import com.example.may.myapplication.models.User;
import com.example.may.myapplication.models.Workshop;

import java.util.Date;

/**
 * Created by May on 4/4/2018.
 */

public class Model {

    private static Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();

    public static Model instance() {
        return instance;
    }

    public String getNextWorkshopId() {
        return modelFirebase.workshops.getNextWorkshopId();
    }

    public void saveWorkshop(Workshop w) {
        modelFirebase.workshops.saveWorkshop(w);
    }

    public void getWorkshopById(String workshopId, ModelFirebase.GetDataListener listener) {
        modelFirebase.workshops.getWorkshopById(workshopId, listener);
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

    public void leaveWaitingList(String workshopId, String userId) {
        modelFirebase.workshopsMembers.leaveWaitingList(workshopId, userId);
    }

    public void enterWaitingList(String workshopId, String userId, WorkshopsMembersFirebase.LeaveWaitingListListener listener) {
        modelFirebase.workshopsMembers.enterWaitingList(workshopId, userId, listener);
    }

    public void getUserById(String id, ModelFirebase.GetDataListener listener) {
        modelFirebase.users.getUserById(id, listener);
    }

    public void saveUser(User u) {
        u.setLastUpdated(new Date().getTime());
        modelFirebase.users.saveUser(u);
    }

    public void deleteWorkshop(String workshopId) {
        modelFirebase.workshops.deleteWorkshop(workshopId);
    }

    public interface SaveImageListener {
        void complete(String url);
        void fail();
    }

    public interface GetImageListener {
        void onSuccess(Bitmap bitmap, long lastUpdated);
        void onFail();
    }

    // Name need to be unique (with timestamp)
    public void saveImage(final Bitmap imageBitmap, final String name, final SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, name,  new SaveImageListener(){

            @Override
            public void complete(String url) {
//                String fileName = URLUtil.guessFileName(url, null, null);
//                saveImageToFile(imageBitmap, fileName);
                listener.complete(url);
            }

            @Override
            public void fail() {

            }
        });

    }

    public void getImage(String imageName, final GetImageListener listener) {

        modelFirebase.getImage(imageName, new GetImageListener() {
            @Override
            public void onSuccess(Bitmap bitmap, long lastUpdated) {
                listener.onSuccess(bitmap, lastUpdated);
            }

            @Override
            public void onFail() {

            }
        });
    }
}
