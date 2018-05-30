package com.example.may.myapplication.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;

import com.example.may.myapplication.MyApp;
import com.example.may.myapplication.dal.Model;
import com.example.may.myapplication.dal.firebase.ModelFirebase;
import com.example.may.myapplication.dal.firebase.UsersFirebase;
import com.example.may.myapplication.dal.firebase.WorkshopsFirebase;
import com.example.may.myapplication.dal.room.daos.UserDao;
import com.example.may.myapplication.models.User;
import com.example.may.myapplication.utils.ImageHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

/**
 * Created by May on 4/17/2018.
 */

public class UserRepository {

    public static final UserRepository instance = new UserRepository();
    public UsersFirebase usersFirebase = ModelFirebase.getInstance().users;

    static public LiveData<User> getUser(String userId) {
        final MutableLiveData<User> data = new MutableLiveData<>();

        Model.instance().getUserById(userId, new ModelFirebase.GetDataListener<User>() {
            @Override
            public void onComplete(User user) {
                data.setValue(user);
            }
        });

        return data;
    }

    public LiveData<Bitmap> getUserImage(String imageUrl, final String userId) {

        final MutableLiveData<Bitmap> result = new MutableLiveData<>();

        Model.instance().getImage("image-" + userId, new Model.GetImageListener() {
            @Override
            public void onSuccess(Bitmap bitmap, long lastUpdated) {

                File file = new File(MyApp.context.getFilesDir(), "image-" + userId);

                // Check if the file saved in the internal storage needs an update
                if (!file.exists() ||  file.lastModified() < lastUpdated) {
                    ImageHelper.saveImageToLocalStorage("image-" + userId, bitmap);
                    result.setValue(bitmap);
                }
            }

            @Override
            public void onFail() {

                //  TODO : alert
            }
        });

        result.setValue(ImageHelper.readImageFromLocalStorage("image-" + userId));
        return result;
    }

    static public String getCurrentUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
