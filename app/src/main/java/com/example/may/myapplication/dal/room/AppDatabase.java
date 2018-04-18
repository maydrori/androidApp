package com.example.may.myapplication.dal.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.example.may.myapplication.MyApp;
import com.example.may.myapplication.dal.room.daos.UserDao;
import com.example.may.myapplication.dal.room.daos.WorkshopDao;
import com.example.may.myapplication.models.User;
import com.example.may.myapplication.models.Workshop;

/**
 * Created by May on 4/18/2018.
 */

@Database(entities = {User.class, Workshop.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract WorkshopDao workshopDao();

    static public AppDatabase db = Room.databaseBuilder(MyApp.context,
            AppDatabase.class, "app_database")
//            .allowMainThreadQueries()
            .build();
}