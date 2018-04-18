package com.example.may.myapplication.dal.room.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.may.myapplication.models.Workshop;
import com.example.may.myapplication.models.WorkshopMembers;

import java.util.List;

/**
 * Created by May on 4/18/2018.
 */

@Dao
public interface WorkshopDao {
    @Query("SELECT * FROM Workshop")
    LiveData<List<Workshop>> getAll();

//    @Query("SELECT * FROM user WHERE id IN (:userIds)")
//    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM Workshop WHERE id LIKE :id LIMIT 1")
    Workshop findById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Workshop... workshops);

    @Delete
    void delete(Workshop workshop);

//    @Query("SELECT * FROM workshops "
//            + "INNER JOIN workshopMembers ON workshopMembers.workshopId = workshops.id "
//            + "WHERE workshops.id LIKE :workshopId")
//    public LiveData<Workshop> getWorkshopAndMembers(String workshopId);






//    @Query("SELECT Foo.*, Bar.* FROM Foo INNER JOIN Bar ON Foo.bar = Bar.id")
//    List<FooAndBar> findAllFooAndBar();
//
//    public class WorkshopAndMembers {
//        @Embedded
//        Workshop workshop;
//
//        @Embedded
//        WorkshopMembers workshopMembers;

        //Getter and setter...
//    }

//    @Query("SELECT user.name AS userName, pet.name AS petName "
//            + "FROM user, pet "
//            + "WHERE user.id = pet.user_id")
//    public LiveData<Workshop> loadUserAndPetNames();
//
//    // You can also define this class in a separate file, as long as you add the
//    // "public" access modifier.
//    static class WorkshopAndMembers {
//        public String userName;
//        public String petName;
//    }
}
