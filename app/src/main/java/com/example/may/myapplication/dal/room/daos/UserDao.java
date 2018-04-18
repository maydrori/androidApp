package com.example.may.myapplication.dal.room.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.may.myapplication.models.User;

import java.util.List;

/**
 * Created by May on 4/18/2018.
 */
@Dao
public interface UserDao {
    @Query("SELECT * FROM User")
    List<User> getAll();

//    @Query("SELECT * FROM user WHERE id IN (:userIds)")
//    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM User WHERE id LIKE :id LIMIT 1")
    User findById(String id);

//    @Insert
//    void insertAll(User... users);

    @Delete
    void delete(User user);
}