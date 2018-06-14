package com.example.may.myapplication.dal.room.daos;

import android.os.AsyncTask;

import com.example.may.myapplication.dal.room.AppDatabase;
import com.example.may.myapplication.models.User;

/**
 * Created by May on 6/14/2018.
 */

public class UserAsyncDao {

    static public void update(final User user) {
        class MyAsynchTask extends AsyncTask<String,String,User> {
            @Override
            protected User doInBackground(String... strings) {
                AppDatabase.db.userDao().insertAll(user);

                return user;
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }
}
