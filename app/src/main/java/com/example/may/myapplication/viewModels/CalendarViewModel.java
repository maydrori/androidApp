package com.example.may.myapplication.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.may.myapplication.models.User;
import com.example.may.myapplication.models.Workshop;
import com.example.may.myapplication.repositories.UserRepository;
import com.example.may.myapplication.repositories.WorkshopsRepository;

import java.util.List;

/**
 * Created by May on 4/17/2018.
 */

public class CalendarViewModel extends ViewModel{

    private LiveData<List<Workshop>> workshops;

    public void init() {
        if (this.workshops != null) {
            // ViewModel is created per Fragment so
            // we know the userId won't change
            return;
        }
        workshops = WorkshopsRepository.getAllWorkshops();
    }

    public LiveData<List<Workshop>> getWorkshops() {
        return this.workshops;
    }
}
