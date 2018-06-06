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

    static public LiveData<Workshop> getWorkshop(String workshopId) {
        return AppDatabase.db.workshopDao().getFullWorkshop(workshopId);
    }

    public LiveData<List<Workshop>> getAllWorkshopsByIds(List<String> ids) {
        return AppDatabase.db.workshopDao().findByIds(ids);
    }

    public LiveData<List<WorkshopDao.WorkshopMini>> getAllWorkshopsForCalendar() {
        return AppDatabase.db.workshopDao().getAllForCalendar();
    }
}
