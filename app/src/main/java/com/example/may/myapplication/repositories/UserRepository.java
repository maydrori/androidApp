package com.example.may.myapplication.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.may.myapplication.dal.Model;
import com.example.may.myapplication.dal.firebase.ModelFirebase;
import com.example.may.myapplication.dal.room.daos.UserDao;
import com.example.may.myapplication.models.User;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by May on 4/17/2018.
 */

public class UserRepository {

//    private UserDao userDao;

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

    static public String getCurrentUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
