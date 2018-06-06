package com.example.may.myapplication.dal.room.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.may.myapplication.models.User;
import com.example.may.myapplication.models.Workshop;

import java.util.List;

/**
 * Created by May on 4/18/2018.
 */
@Dao
public interface UserDao {
    @Query("SELECT * FROM User")
    List<User> getAll();

    @Query("SELECT * FROM User WHERE id LIKE :id LIMIT 1")
    LiveData<User> findById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... user);

    @Delete
    void delete(User user);
}