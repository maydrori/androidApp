package com.example.may.myapplication.dal.room.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.may.myapplication.models.User;
import com.example.may.myapplication.models.Workshop;

import java.util.List;

/**
 * Created by May on 4/18/2018.
 */

@Dao
public interface WorkshopDao {

//    @Query("SELECT Workshop.*, User.name as teacherName, User.imageUrl as teacherImageUrl FROM Workshop "
//            + "INNER JOIN User ON Workshop.teacherId = User.id ")
//    LiveData<List<Workshop>> getAll();

    @Query("SELECT id, date FROM Workshop")
    LiveData<List<WorkshopMini>> getAllForCalendar();

    @Query("SELECT * FROM Workshop WHERE id LIKE :id LIMIT 1")
    LiveData<Workshop> findById(String id);

    @Query("SELECT Workshop.*, User.name as teacherName, User.imageUrl as teacherImageUrl FROM Workshop "
            + "INNER JOIN User ON Workshop.teacherId = User.id "
            + "WHERE Workshop.id IN (:ids)"
            + "ORDER BY Workshop.date ASC")
    LiveData<List<Workshop>> findByIds(List<String> ids);

    @Query("SELECT * FROM Workshop")
    List<Workshop> findAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Workshop... workshops);

    @Query("SELECT Workshop.*, User.name as teacherName, User.imageUrl as teacherImageUrl FROM Workshop "
            + "INNER JOIN User ON Workshop.teacherId = User.id "
            + "WHERE Workshop.id=:id")
    LiveData<Workshop> getFullWorkshop(String id);

    @Delete
    void delete(Workshop workshop);

    class WorkshopMini {
        String id;
        long date;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }
    }
}
