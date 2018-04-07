package com.example.may.myapplication.utils;

import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.TextView;

import com.example.may.myapplication.R;
import com.example.may.myapplication.model.Model;
import com.example.may.myapplication.model.User;
import com.example.may.myapplication.model.firebase.ModelFirebase;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by May on 4/7/2018.
 */

public class UsersManager {

    private static UsersManager instance = new UsersManager();

    public void setCurrentUser(final ModelFirebase.GetDataListener<User> listener) {
        Model.instance().getUserById(FirebaseAuth.getInstance().getCurrentUser().getUid(), new ModelFirebase.GetDataListener<User>() {
            @Override
            public void onComplete(User user) {
            instance().currentUser = user;
            listener.onComplete(user);
            }
        });
    }

    public void setCurrentUser(User user) {
        instance().currentUser = user;
    }

    private User currentUser;

    public static UsersManager instance() {
        return instance;
    }

    public User getCurrentUser () {
        return currentUser;
    }
}
