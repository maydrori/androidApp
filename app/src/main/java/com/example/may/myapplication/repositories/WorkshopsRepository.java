package com.example.may.myapplication.repositories;

import android.arch.lifecycle.LiveData;

import com.example.may.myapplication.dal.Model;
import com.example.may.myapplication.dal.room.AppDatabase;
import com.example.may.myapplication.dal.room.daos.WorkshopDao;
import com.example.may.myapplication.models.Workshop;

import java.util.List;

/**
 * Created by May on 4 /17/2018.
 */

public class WorkshopsRepository {

    public static final WorkshopsRepository instance = new WorkshopsRepository();

    static public LiveData<Workshop> getWorkshop(String workshopId) {
        return AppDatabase.db.workshopDao().get(workshopId);
    }

    public LiveData<List<Workshop>> getAllWorkshopsByIds(List<String> ids) {
        return AppDatabase.db.workshopDao().findByIds(ids);
    }

    public LiveData<List<WorkshopDao.WorkshopMini>> getAllWorkshopsForCalendar() {
        return AppDatabase.db.workshopDao().getAllForCalendar();
    }
}
