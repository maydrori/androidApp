package com.example.may.myapplication.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.may.myapplication.MyApp;
import com.example.may.myapplication.dal.Model;
import com.example.may.myapplication.dal.firebase.ModelFirebase;
import com.example.may.myapplication.dal.firebase.UsersFirebase;
import com.example.may.myapplication.dal.firebase.WorkshopsFirebase;
import com.example.may.myapplication.dal.room.AppDatabase;
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

    static public LiveData<User> getUser(String userId) {
        return AppDatabase.db.userDao().findById(userId);
    }

    /**
     * Returns the user's image from the local storage.
     * At the same time, get the image from firebase storage and check if we
     * need to update the local storage
//     * @param userId
     * @return
     */
//    public Bitmap getUserImage(final String imageUrl) {
//
//        final MutableLiveData<Bitmap> result = new MutableLiveData<>();
//
//        // Get the image from firebase storage
//        Model.instance().getImage("image-" + userId, new Model.GetImageListener() {
//            @Override
//            public void onSuccess(Bitmap bitmap, long lastUpdated) {
//
//                // Get the image file from local storage
//                File file = new File(MyApp.context.getFilesDir(), "image-" + userId);
//
//                // Check if the file saved in the internal storage needs an update
//                if (!file.exists() ||  file.lastModified() < lastUpdated) {
//                    ImageHelper.saveImageToLocalStorage("image-" + userId, bitmap);
//                    result.setValue(bitmap);
//                }
//            }
//
//            @Override
//            public void onFail() {
//                Log.d("TAG", "fail to get image for " + userId);
//            }
//        });
//
//        // Read the image from local storage
//        result.setValue(ImageHelper.readImageFromLocalStorage("image-" + userId));
//        return result;
//    }

    static public String getCurrentUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
