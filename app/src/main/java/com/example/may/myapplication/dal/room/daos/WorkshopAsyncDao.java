package com.example.may.myapplication.dal.room.daos;

import android.os.AsyncTask;

import com.example.may.myapplication.dal.room.AppDatabase;
import com.example.may.myapplication.models.Workshop;

import java.util.List;

/**
 * Created by May on 6/13/2018.
 */

public class WorkshopAsyncDao {

    public interface WorkshopAsynchDaoListener<T>{
        void onComplete(T data);
    }
    static public void getAll(final WorkshopAsynchDaoListener<List<Workshop>> listener) {
        class MyAsynchTask extends AsyncTask<String,String,List<Workshop>> {
            @Override
            protected List<Workshop> doInBackground(String... strings) {
                List<Workshop> stList = AppDatabase.db.workshopDao().getAll();
                return stList;
            }

            @Override
            protected void onPostExecute(List<Workshop> workshops) {
                super.onPostExecute(workshops);
                listener.onComplete(workshops);
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }

    static public void update(final Workshop w) {
        class MyAsynchTask extends AsyncTask<String,String,Workshop> {
            @Override
            protected Workshop doInBackground(String... strings) {
                if (w.getIsDeleted()) {
                    AppDatabase.db.workshopDao().delete(w.getId());
                }
                else {
                    AppDatabase.db.workshopDao().insertAll(w);
                }

                return w;
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }


    public static void insertAll(final List<Workshop> workshops, final WorkshopAsynchDaoListener<Boolean> listener){
        class MyAsynchTask extends AsyncTask<List<Workshop>,String,Boolean>{
            @Override
            protected Boolean doInBackground(List<Workshop>... workshops) {
                for (Workshop w:workshops[0]) {
                    AppDatabase.db.workshopDao().insertAll(w);
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                listener.onComplete(success);
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute(workshops);
    }
}
