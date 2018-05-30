package com.example.may.myapplication.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.example.may.myapplication.models.User;
import com.example.may.myapplication.repositories.UserRepository;

/**
 * Created by May on 4/17/2018.
 */

public class UserViewModel extends ViewModel{

    private LiveData<User> user;

    public void init(String userId) {
        if (this.user != null) {
            // ViewModel is created per Fragment so
            // we know the userId won't change
            return;
        }
        user = UserRepository.getUser(userId);
    }

    public LiveData<Bitmap> getImage(String url, String userId) {
        return UserRepository.instance.getUserImage(url, userId);
    }

    public LiveData<User> getUser() {
        return user;
    }
}
