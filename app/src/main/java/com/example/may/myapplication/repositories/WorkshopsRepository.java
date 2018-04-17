package com.example.may.myapplication.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.may.myapplication.dal.Model;
import com.example.may.myapplication.dal.firebase.ModelFirebase;
import com.example.may.myapplication.models.Workshop;
import com.example.may.myapplication.models.WorkshopMembers;

import java.util.List;
import java.util.Map;

/**
 * Created by May on 4/17/2018.
 */

public class WorkshopsRepository {

    static public LiveData<List<Workshop>> getAllWorkshops() {
        final MutableLiveData<List<Workshop>> data = new MutableLiveData<>();

        Model.instance().getAllWorkshops(new ModelFirebase.GetDataListener<List<Workshop>>() {
            @Override
            public void onComplete(List<Workshop> workshops) {
                data.setValue(workshops);
            }
        });

        return data;
    }

    static public LiveData<Workshop> getWorkshop(String workshopId) {
        final MutableLiveData<Workshop> data = new MutableLiveData<>();

        Model.instance().getWorkshopById(workshopId, new ModelFirebase.GetDataListener<Workshop>() {
            @Override
            public void onComplete(Workshop workshop) {
                data.setValue(workshop);
            }
        });

        return data;
    }

    static public LiveData<WorkshopMembers> getWorkshopMembers(String workshopId) {
        final MutableLiveData<WorkshopMembers> data = new MutableLiveData<>();

        Model.instance().getWorkshopMembers(workshopId, new ModelFirebase.GetDataListener<WorkshopMembers>() {
            @Override
            public void onComplete(WorkshopMembers workshopMembers) {
                data.setValue(workshopMembers);
            }
        });

        return data;
    }
}
