package com.example.may.myapplication.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.view.View;

import com.example.may.myapplication.dal.room.daos.WorkshopDao;
import com.example.may.myapplication.models.User;
import com.example.may.myapplication.models.Workshop;
import com.example.may.myapplication.repositories.UserRepository;
import com.example.may.myapplication.repositories.WorkshopsRepository;

import java.util.List;

/**
 * Created by May on 4/17/2018.
 */

public class CalendarViewModel extends ViewModel {

    private LiveData<List<WorkshopDao.WorkshopMini>> calendarWorkshops;

    public CalendarViewModel () {
        calendarWorkshops = WorkshopsRepository.instance.getAllWorkshopsForCalendar();
    }

    public LiveData<Bitmap> getImage(String url, String userId) {
        return UserRepository.instance.getUserImage(url, userId);
    }

    public LiveData<List<WorkshopDao.WorkshopMini>> getWorkshopsForCalendar() {
        return this.calendarWorkshops;
    }

    public LiveData<List<Workshop>> getWorkshopsByIds(List<String> ids) {
        return WorkshopsRepository.instance.getAllWorkshopsByIds(ids);
    }
}
