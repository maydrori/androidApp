package com.example.may.myapplication.dal.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by May on 4/4/2018.
 */

public class ModelFirebase {

    private static ModelFirebase instance = new ModelFirebase();

    public static ModelFirebase getInstance() {return instance;}

    public WorkshopsFirebase workshops;
    public UsersFirebase users;

    final String USERS_LAST_UPDATE_DATE = "usersLastUpdateDate";
    final String WORKSHOPS_LAST_UPDATE_DATE = "workshopsLastUpdateDate";

    public ModelFirebase() {
        FirebaseDatabase fbInstance = FirebaseDatabase.getInstance();
        workshops = new WorkshopsFirebase(fbInstance.getReference("workshops"));
        users = new UsersFirebase(fbInstance.getReference("users"));
    }

    /**
     * Upload image to firebase stoarge
     * @param imageBitmap
     * @param listener
     */
    public void saveImage(Bitmap imageBitmap, final Model.SaveImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        String name = ""+ (new Date()).getTime();
        StorageReference imagesRef = storage.getReference().child("images").child(name);

        UploadTask uploadTask = imagesRef.putBytes(ImageHelper.bitmapToBytes(imageBitmap));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                listener.complete(downloadUrl.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.complete(null);
            }
        });
    }

    /**
     * Get image from firebase storage via the image url
     * @param url
     * @param listener
     */
    public void getImage(String url, final Model.GetImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(url);

        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(5 * ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = ImageHelper.bytesToBitmap(bytes);
                listener.onDone(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                exception.printStackTrace();
                listener.onDone(null);
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
        editor.putLong(field, date);
        editor.commit();
    }
}
