package com.example.may.myapplication.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

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

    public LiveData<User> getUser() {
        return this.user;
    }
}
