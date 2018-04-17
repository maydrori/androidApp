package com.example.may.myapplication.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.may.myapplication.models.Workshop;
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
        workshop = WorkshopsRepository.getWorkshop(workshopId);

    }

    public LiveData<Workshop> getWorkshop() {
        return this.workshop;
    }


//    private MediatorLiveData<CompositeModel> compositeModelLiveData;
//
//    public static class CompositeModel {
//
//        Workshop workshop;
//        WorkshopMembers workshopMembers;
//
//        public Workshop getWorkshop() {
//            return workshop;
//        }
//
//        public WorkshopMembers getWorkshopMembers() {
//            return workshopMembers;
//        }
//    }
//
//
//        this.compositeModelLiveData = new MediatorLiveData<>();
//        this.compositeModelLiveData.addSource(workshop, new Observer<Workshop>() {
//        @Override
//        public void onChanged(@Nullable Workshop workshop) {
//            CompositeModel compositeModel = compositeModelLiveData.getValue();
//            compositeModel.workshop = workshop;
//            if (compositeModel.workshopMembers != null) compositeModelLiveData.postValue(compositeModel);
//        }
//    });
//
//        this.compositeModelLiveData.addSource(workshopMembers, new Observer<WorkshopMembers>() {
//        @Override
//        public void onChanged(@Nullable WorkshopMembers workshopMembers) {
//            CompositeModel compositeModel = compositeModelLiveData.getValue();
//            compositeModel.workshopMembers = workshopMembers;
//            if (compositeModel.workshop != null) compositeModelLiveData.postValue(compositeModel);
//        }
//    });
//
//        this.compositeModelLiveData.setValue(new CompositeModel());
//
//
//    public LiveData<CompositeModel> getCompositeEntrys() {
//        return compositeModelLiveData;
//    }
}
