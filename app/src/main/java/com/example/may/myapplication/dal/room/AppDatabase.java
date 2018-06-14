package com.example.may.myapplication.dal.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.example.may.myapplication.MyApp;
import com.example.may.myapplication.dal.room.converters.StringArrayConverter;
import com.example.may.myapplication.dal.room.daos.UserDao;
import com.example.may.myapplication.dal.room.daos.WorkshopDao;
import com.example.may.myapplication.models.User;
import com.example.may.myapplication.models.Workshop;

/**
 * Created by May on 4/18/2018.
 */

@Database(entities = {User.class, Workshop.class}, version = 8)
@TypeConverters({StringArrayConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract WorkshopDao workshopDao();

    static public AppDatabase db = Room.databaseBuilder(MyApp.context,
            AppDatabase.class, "app_database")
            .fallbackToDestructiveMigration()
            .build();
}
