package com.example.may.myapplication.dal;

import android.graphics.Bitmap;

import com.example.may.myapplication.dal.firebase.ModelFirebase;
import com.example.may.myapplication.dal.firebase.WorkshopsFirebase;
import com.example.may.myapplication.models.User;
import com.example.may.myapplication.models.Workshop;

import java.util.Date;

/**
 * Created by May on 4/4/2018.
 */

public class Model {

    private static Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();

    public static Model instance() {
        return instance;
    }

    Model () {
        syncRemoteData();
    }

//    class WorkshopListData extends MutableLiveData<List<Workshop>> {
//
//        @Override
//        protected void onActive() {
//            super.onActive();
//            // new thread tsks
//            // 1. get the students list from the local DB
//            WorkshopAsyncDao.getAll(new WorkshopAsyncDao.WorkshopAsynchDaoListener<List<Workshop>>() {
//                @Override
//                public void onComplete(List<Workshop> data) {
//                    // 2. update the live data with the new student list
//                    setValue(data);
//                    Log.d("TAG","got students from local DB " + data.size());
//
//                    // 3. get the student list from firebase
//                    modelFirebase.workshops.getAllWorkshops(new ModelFirebase.GetDataListener<List<Workshop>>() {
//                        @Override
//                        public void onComplete(List<Workshop> workshoplist) {
//                            // 4. update the live data with the new student list
//                            setValue(workshoplist);
////                            Log.d("TAG","got students from firebase " + workshoplist.size());
//
//                            // 5. update the local DB
//                            WorkshopAsyncDao.insertAll(workshoplist, new WorkshopAsyncDao.WorkshopAsynchDaoListener<Boolean>() {
//                                @Override
//                                public void onComplete(Boolean data) {
//
//                                }
//                            });
//                        }
//                    });
//                }
//            });
//        }
//
//        @Override
//        protected void onInactive() {
//            super.onInactive();
//            modelFirebase.workshops.cancellGetAllStudents();
//            Log.d("TAG","cancellGetAllStudents");
//        }
//
//        public WorkshopListData() {
//            super();
//            //setValue(AppLocalDb.db.studentDao().getAll());
//            setValue(new LinkedList<Workshop>());
//        }
//    }
//
//    WorkshopListData workshopListData = new WorkshopListData();

//    public LiveData<List<Workshop>> getAllWorkshops(){
//        return workshopListData;
//    }

    public String getNextWorkshopId() {
        return modelFirebase.workshops.getNextWorkshopId();
    }

    public void saveWorkshop(Workshop w) {
        modelFirebase.workshops.saveWorkshop(w);
    }

    public void getWorkshopById(String workshopId, ModelFirebase.GetDataListener listener) {
        modelFirebase.workshops.getWorkshopById(workshopId, listener);
    }

//    public void getAllWorkshops(ModelFirebase.GetDataListener listener) {
//        modelFirebase.workshops.getAllWorkshops(listener);
//    }

    public void registerMemberToWorkshop(String workshopId, String userId) {
        modelFirebase.workshops.registerMemberToWorkshop(workshopId, userId);
    }

    public void unregisterMemberFromWorkshop(String workshopId, String userId) {
        modelFirebase.workshops.unregisterMemberFromWorkshop(workshopId, userId);
    }

    public void leaveWaitingList(String workshopId, String userId) {
        modelFirebase.workshops.leaveWaitingList(workshopId, userId);
    }

    public void enterWaitingList(String workshopId, String userId) {
        modelFirebase.workshops.enterWaitingList(workshopId, userId);
    }

    public void getUserById(String id, ModelFirebase.GetDataListener listener) {
        modelFirebase.users.getUserById(id, listener);
    }

    public void saveUser(User u) {
        u.setLastUpdated(new Date().getTime());
        modelFirebase.users.saveUser(u);
    }

    public void deleteWorkshop(final String workshopId) {
        modelFirebase.workshops.deleteWorkshop(workshopId);
    }

    public interface SaveImageListener {
        void complete(String url);
        void fail();
    }

    public interface GetImageListener {
        void onSuccess(Bitmap bitmap, long lastUpdated);
        void onFail();
    }

    public void saveImage(final Bitmap imageBitmap, final String name, final SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, name, new SaveImageListener(){
            @Override
            public void complete(String url) {
                listener.complete(url);
            }

            @Override
            public void fail() {
                listener.fail();
            }
        });
    }

    public void getImage(String imageName, final GetImageListener listener) {

        modelFirebase.getImage(imageName, new GetImageListener() {
            @Override
            public void onSuccess(Bitmap bitmap, long lastUpdated) {
                listener.onSuccess(bitmap, lastUpdated);
            }

            @Override
            public void onFail() {
                listener.onFail();
            }
        });
    }

    public void syncRemoteData() {
        modelFirebase.syncRemoteData();
    }
}
