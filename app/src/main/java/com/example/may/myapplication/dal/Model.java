package com.example.may.myapplication.dal;

import android.graphics.Bitmap;

import com.example.may.myapplication.dal.firebase.ModelFirebase;
import com.example.may.myapplication.dal.sql.ModelSql;
import com.example.may.myapplication.models.User;
import com.example.may.myapplication.models.Workshop;

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

    public void saveWorkshop(Workshop w) {
        modelFirebase.workshops.addWorkshop(w);
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

    public void enterWaitingList(String workshopId, String userId) {
        modelFirebase.workshopsMembers.enterWaitingList(workshopId, userId);
    }

    public void leaveWaitingList(String workshopId, String userId) {
        modelFirebase.workshopsMembers.leaveWaitingList(workshopId, userId);
    }

    public void getWorkshopMembers(String workshopId, ModelFirebase.GetDataListener listener) {
        modelFirebase.workshopsMembers.getWorkshopMembers(workshopId, listener);
    }

    public void getUserById(String id, ModelFirebase.GetDataListener listener) {
        modelFirebase.users.getUserById(id, listener);
    }

    public void saveUser(User u) {modelFirebase.users.saveUser(u);}

    public void deleteWorkshop(String workshopId) {
        modelFirebase.workshops.deleteWorkshop(workshopId);
        modelFirebase.workshopsMembers.deleteWorkshop(workshopId);
    }

    public interface SaveImageListener {
        void complete(String url);
        void fail();
    }

    public interface GetImageListener {
        void onSuccess(Bitmap bitmap);
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
            public void onSuccess(Bitmap bitmap) {
                listener.onSuccess(bitmap);
            }

            @Override
            public void onFail() {

            }
        });
    }
}
