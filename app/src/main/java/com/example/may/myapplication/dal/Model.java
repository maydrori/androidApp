package com.example.may.myapplication.dal;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.may.myapplication.dal.firebase.ModelFirebase;
import com.example.may.myapplication.dal.firebase.WorkshopsFirebase;
import com.example.may.myapplication.models.User;
import com.example.may.myapplication.models.Workshop;
import com.example.may.myapplication.utils.ImageHelper;

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

    Model () {
        syncRemoteData();
    }

    public String getNextWorkshopId() {
        return modelFirebase.workshops.getNextWorkshopId();
    }

    public void saveWorkshop(Workshop w) {
        modelFirebase.workshops.saveWorkshop(w);
    }

    public void registerMemberToWorkshop(String workshopId, String userId) {
        modelFirebase.workshops.registerMemberToWorkshop(workshopId, userId);
    }

    public void unregisterMemberFromWorkshop(String workshopId, String userId) {
        modelFirebase.workshops.unregisterMemberFromWorkshop(workshopId, userId);
    }

    public void leaveWaitingList(String workshopId, String userId) {
        modelFirebase.workshops.leaveWaitingList(workshopId, userId);
    }

    public void enterWaitingList(String workshopId, String userId) {
        modelFirebase.workshops.enterWaitingList(workshopId, userId);
    }

    public void saveUser(User u) {
        u.setLastUpdated(new Date().getTime());
        modelFirebase.users.saveUser(u);
    }

    public void deleteWorkshop(final String workshopId) {
        modelFirebase.workshops.deleteWorkshop(workshopId);
    }

    public interface SaveImageListener {
        void complete(String url);
        void fail();
    }

    public interface GetImageListener {
        void onDone(Bitmap bitmap);
    }

    /**
     * Save image in firebase
     * @param imageBitmap
     * @param listener
     */
    public void saveImage(final Bitmap imageBitmap, final SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, new SaveImageListener(){
            @Override
            public void complete(String url) {
                listener.complete(url);
            }

            @Override
            public void fail() {
                listener.fail();
            }
        });
    }

    /**
     * Get user image by url. First try to get it from the local device storage.
     * If the image doesn't exist, get it from the firebase storage and save it locally.
     * @param url
     * @param listener
     */
    public void getImage(final String url, final GetImageListener listener){

        // If no url- return.
        if (url == null) listener.onDone(null);
        else {
            // Try to get the image from the local device storage
            String localFileName = URLUtil.guessFileName(url, null, null);
            final Bitmap image = ImageHelper.readImageFromLocalStorage(localFileName);

            if (image != null) listener.onDone(image);
            // Image not found, get it from firebase
            else {
                modelFirebase.getImage(url, new GetImageListener() {
                    @Override
                    public void onDone(Bitmap imageBitmap) {
                        if (imageBitmap == null) {
                            listener.onDone(null);
                        } else {
                            // Save the image locally
                            String localFileName = URLUtil.guessFileName(url, null, null);
                            ImageHelper.saveImageToLocalStorage(imageBitmap, localFileName);

                            // Return the image using the listener
                            listener.onDone(imageBitmap);
                        }
                    }
                });
            }
        }
    }

    public void syncRemoteData() {
        modelFirebase.syncRemoteData();
    }
}
