package com.example.may.myapplication.dal.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.may.myapplication.MyApp;
import com.example.may.myapplication.dal.Model;
import com.example.may.myapplication.dal.room.daos.UserAsyncDao;
import com.example.may.myapplication.dal.room.daos.WorkshopAsyncDao;
import com.example.may.myapplication.models.User;
import com.example.may.myapplication.models.Workshop;
import com.example.may.myapplication.repositories.UserRepository;
import com.example.may.myapplication.utils.ImageHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

/**
 * Created by May on 4/4/2018.
 */

public class ModelFirebase {

    private static ModelFirebase instance = new ModelFirebase();

    public static ModelFirebase getInstance() {return instance;}

    public WorkshopsFirebase workshops;
    public UsersFirebase users;
    StorageReference imagesStorage;
    final String USERS_LAST_UPDATE_DATE = "usersLastUpdateDate";
    final String WORKSHOPS_LAST_UPDATE_DATE = "workshopsLastUpdateDate";

    public ModelFirebase() {
        FirebaseDatabase fbInstance = FirebaseDatabase.getInstance();
        workshops = new WorkshopsFirebase(fbInstance.getReference("workshops"));
        users = new UsersFirebase(fbInstance.getReference("users"));

        imagesStorage = FirebaseStorage.getInstance().getReference().child("images"); ;
    }

    /**
     * Upload the image to the firebase storage
     * @param imageBitmap
     * @param name
     * @param listener
     */
    public void saveImage(final Bitmap imageBitmap, final String name, final Model.SaveImageListener listener) {

        byte[] data = ImageHelper.bitmapToBytes(imageBitmap);

        UploadTask uploadTask = imagesStorage.child(name).putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                listener.complete(downloadUrl.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.fail();
            }
        });
    }

    public void getImage(final String name, final Model.GetImageListener listener) {

        final long ONE_MEGABYTE = 1024 * 1024 * 5;
        imagesStorage.child(name).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(final byte[] bytes) {

                imagesStorage.child(name).getMetadata().addOnCompleteListener(new OnCompleteListener<StorageMetadata>() {

                    @Override
                    public void onComplete(@NonNull Task<StorageMetadata> task) {
                        long lastUpdated = task.getResult().getUpdatedTimeMillis();

                        listener.onSuccess(ImageHelper.bytesToBitmap(bytes), lastUpdated);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                listener.onFail();
            }
        });
    }

    public interface GetDataListener<T> {
        void onComplete(T data);
    }

    /**
     * Sync all the data we need from firebase to local database by last update timestamp
     */
    public void syncRemoteData() {
        syncUsers();
        syncWorkshops();
    }

    private void syncUsers() {
        final long lastUpdateDate = getLastUpdateDate(USERS_LAST_UPDATE_DATE);

        // Get all users from firebase by the last update date
        users.getAllUsers(lastUpdateDate, new ModelFirebase.GetDataListener<List<User>>() {
            @Override
            public void onComplete(List<User> users) {
                if (users != null && users.size() > 0) {

                    // Update all the outdated users in the local db
                    long recentUpdate = lastUpdateDate;
                    for (final User user : users) {

                        UserAsyncDao.update(user);

                        if (user.getLastUpdated() > recentUpdate) {
                            recentUpdate = user.getLastUpdated();
                        }
                        Log.d("TAG", "own user: " + UserRepository.getCurrentUserId() + ".. updating user: " + user.getId().toString());
                    }
                    updateLastUpdateDate(USERS_LAST_UPDATE_DATE, recentUpdate);
                }
            }
        });
    }

    private void syncWorkshops() {
        final long lastUpdateDate = getLastUpdateDate(WORKSHOPS_LAST_UPDATE_DATE);

        // Get all workshops from firebase by the last update date
        workshops.getAllWorkshops(lastUpdateDate, new ModelFirebase.GetDataListener<List<Workshop>>() {
            @Override
            public void onComplete(List<Workshop> workshops) {
                if (workshops != null && workshops.size() > 0) {

                    // Update all the outdated workshops in the local db
                    long recentUpdate = lastUpdateDate;
                    for (final Workshop w : workshops) {

                        WorkshopAsyncDao.update(w);

                        if (w.getLastUpdated() > recentUpdate) {
                            recentUpdate = w.getLastUpdated();
                        }
                        Log.d("TAG", "own user: " + UserRepository.getCurrentUserId() + ".. updating workshop: " + w.getId().toString());
                    }
                    updateLastUpdateDate(WORKSHOPS_LAST_UPDATE_DATE, recentUpdate);
                }
            }
        });
    }

    private long getLastUpdateDate(String field) {
        return MyApp.context.getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong(WORKSHOPS_LAST_UPDATE_DATE, 0);
    }

    private void updateLastUpdateDate(String field, long date) {
        SharedPreferences.Editor editor = MyApp.context.getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
//        editor.putLong(field, new Date().getTime());
        editor.putLong(field, date);
        editor.commit();
    }
}
