package com.example.may.myapplication.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.amitshekhar.DebugDB;
import com.example.may.myapplication.MyApp;
import com.example.may.myapplication.dal.firebase.ModelFirebase;
import com.example.may.myapplication.dal.firebase.WorkshopsFirebase;
import com.example.may.myapplication.dal.room.AppDatabase;
import com.example.may.myapplication.models.Workshop;
import com.example.may.myapplication.models.WorkshopMembers;

import java.util.List;

/**
 * Created by May on 4/17/2018.
 */

public class WorkshopsRepository {

    public static final WorkshopsRepository instance = new WorkshopsRepository();
    public WorkshopsFirebase workshopsFirebase = ModelFirebase.getInstance().workshops;

    private MutableLiveData<List<Workshop>> allWorkshops = new MutableLiveData<>();

    WorkshopsRepository() {
//        allWorkshops = new MutableLiveData<>();
    }

    static public LiveData<WorkshopMembers> getWorkshopMembers(String workshopId) {
        final MutableLiveData<WorkshopMembers> data = new MutableLiveData<>();

//        data.setValue(AppDatabase.db.workshopDao().findById(workshopId));

//        Model.instance().getWorkshopById(workshopId, new ModelFirebase.GetDataListener<Workshop>() {
//            @Override
//            public void onComplete(Workshop workshop) {
//                data.setValue(workshop);
//            }
//        });

        return data;
    }

    static public LiveData<Workshop> getWorkshop(String workshopId) {
        final MutableLiveData<Workshop> data = new MutableLiveData<>();

        data.setValue(AppDatabase.db.workshopDao().findById(workshopId));

//        Model.instance().getWorkshopById(workshopId, new ModelFirebase.GetDataListener<Workshop>() {
//            @Override
//            public void onComplete(Workshop workshop) {
//                data.setValue(workshop);
//            }
//        });

        return data;
    }

    public LiveData<List<Workshop>> getAllWorkshops() {

        // Get last update date
        final long lastUpdateDate = MyApp.context.getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("lastUpdateDate", 0);

        // Get all workshop records that where updated since last update date
        workshopsFirebase.getAllWorkshops(new ModelFirebase.GetDataListener<List<Workshop>>() {
            @Override
            public void onComplete(List<Workshop> data) {
                if (data != null && data.size() > 0) {
                    // Update the local db
                    long recentUpdate = lastUpdateDate;
                    for (final Workshop workshop : data) {
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                AppDatabase.db.workshopDao().insertAll(workshop);
//                                // Insert Data
//                                AppDatabase.getInstance(context).userDao().insert(new User(1,"James","Mathew"));
//
//                                // Get Data
//                                AppDatabase.getInstance(context).userDao().getAllUsers();
                            }
                        });
//                        AppDatabase.db.workshopDao().insertAll(workshop);
                        if (workshop.getLastUpdated() > recentUpdate) {
                            recentUpdate = workshop.getLastUpdated();
                        }
                    }
                    SharedPreferences.Editor editor = MyApp.context.getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
                    editor.putLong("lastUpdateDate", recentUpdate);
                }

                allWorkshops = (MutableLiveData<List<Workshop>>)AppDatabase.db.workshopDao().getAll();
//                allWorkshops.setValue(workshops);
            }
        });

        return allWorkshops;
    }

//    public LiveData<List<Workshop>> getWorkshopById(String workshopId) {
//        return allWorkshops.getValue();
//    }

//    static public LiveData<List<Workshop>> getAllWorkshops() {
//        final MutableLiveData<List<Workshop>> data = new MutableLiveData<>();
//
//        Model.instance().getAllWorkshops(new ModelFirebase.GetDataListener<List<Workshop>>() {
//            @Override
//            public void onComplete(List<Workshop> workshops) {
//                data.setValue(workshops);
//            }
//        });
//
//        return data;
//    }
//
//    static public LiveData<Workshop> getWorkshop(String workshopId) {
//        final MutableLiveData<Workshop> data = new MutableLiveData<>();
//
//        Model.instance().getWorkshopById(workshopId, new ModelFirebase.GetDataListener<Workshop>() {
//            @Override
//            public void onComplete(Workshop workshop) {
//                data.setValue(workshop);
//            }
//        });
//
//        return data;
//    }
//
//    static public LiveData<WorkshopMembers> getWorkshopMembers(String workshopId) {
//        final MutableLiveData<WorkshopMembers> data = new MutableLiveData<>();
//
//        Model.instance().getWorkshopMembers(workshopId, new ModelFirebase.GetDataListener<WorkshopMembers>() {
//            @Override
//            public void onComplete(WorkshopMembers workshopMembers) {
//                data.setValue(workshopMembers);
//            }
//        });
//
//        return data;
//    }
}
