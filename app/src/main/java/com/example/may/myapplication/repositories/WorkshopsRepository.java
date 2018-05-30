package com.example.may.myapplication.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;

import com.example.may.myapplication.MyApp;
import com.example.may.myapplication.dal.Model;
import com.example.may.myapplication.dal.firebase.ModelFirebase;
import com.example.may.myapplication.dal.firebase.WorkshopsFirebase;
import com.example.may.myapplication.dal.room.AppDatabase;
import com.example.may.myapplication.dal.room.daos.WorkshopDao;
import com.example.may.myapplication.models.User;
import com.example.may.myapplication.models.Workshop;
import com.example.may.myapplication.utils.ImageHelper;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by May on 4 /17/2018.
 */

public class WorkshopsRepository {

    public static final WorkshopsRepository instance = new WorkshopsRepository();
    public WorkshopsFirebase workshopsFirebase = ModelFirebase.getInstance().workshops;

    static public LiveData<Workshop> getWorkshop(String workshopId) {
        return AppDatabase.db.workshopDao().getFullWorkshop(workshopId);
    }

    public LiveData<List<Workshop>> getAllWorkshopsByIds(List<String> ids) {

        return AppDatabase.db.workshopDao().findByIds(ids);
    }

    public LiveData<List<WorkshopDao.WorkshopMini>> getAllWorkshopsForCalendar() {
        syncWorkshops();
        syncUsers();

        return AppDatabase.db.workshopDao().getAllForCalendar();
    }

    private void syncUsers() {
        final long lastUpdateDate = MyApp.context.getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("usersLastUpdateDate", 0);

        // Get all workshop records that where updated since last update date
        ModelFirebase.getInstance().users.getAllUsers(new ModelFirebase.GetDataListener<List<User>>() {
            @Override
            public void onComplete(List<User> data) {
                if (data != null && data.size() > 0) {
                    // Update the local db
                    long recentUpdate = 0;
//                    lastUpdateDate;
                    for (final User user : data) {
                        if (user.getLastUpdated() >= recentUpdate) {
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    AppDatabase.db.userDao().insertAll(user);
                                }
                            });
                        }
                    }
                    SharedPreferences.Editor editor = MyApp.context.getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
                    editor.putLong("usersLastUpdateDate", new Date().getTime());
                    editor.commit();
                }
            }
        });
    }

    private void syncWorkshops() {
        final long lastUpdateDate = MyApp.context.getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("workshopsLastUpdateDate", 0);

        // Get all workshop records that where updated since last update date
        workshopsFirebase.getAllWorkshops(new ModelFirebase.GetDataListener<List<Workshop>>() {
            @Override
            public void onComplete(List<Workshop> data) {
                if (data != null && data.size() > 0) {
                    // Update the local db
                    long recentUpdate = 0;
//                    lastUpdateDate;
                    for (final Workshop workshop : data) {
                        if (workshop.getLastUpdated() >= recentUpdate) {
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    AppDatabase.db.workshopDao().insertAll(workshop);
                                }
                            });
                        }
                    }
                    SharedPreferences.Editor editor = MyApp.context.getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
                    editor.putLong("workshopsLastUpdateDate", new Date().getTime());
                    editor.commit();
                }
            }
        });
    }
}
