package com.example.may.myapplication.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;

import com.example.may.myapplication.dal.room.daos.WorkshopDao;
import com.example.may.myapplication.models.User;
import com.example.may.myapplication.models.Workshop;
import com.example.may.myapplication.repositories.UserRepository;
import com.example.may.myapplication.repositories.WorkshopsRepository;

/**
 * Created by May on 4/17/2018.
 */

public class WorkshopViewModel extends ViewModel{

    private LiveData<Workshop> workshop;

    public void init(String workshopId) {
        if (this.workshop != null) {
            // ViewModel is created per Fragment so
            // we know the userId won't change
            return;
        }
        workshop = WorkshopsRepository.instance.getWorkshop(workshopId);
    }

    public LiveData<Workshop> getWorkshop() {
        return this.workshop;
    }

    public LiveData<Bitmap> getTeacherImage(String userId) {
        return UserRepository.instance.getUserImage(userId);
    }
}
