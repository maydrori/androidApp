package com.example.may.myapplication.model.firebase;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by May on 4/4/2018.
 */

public class ModelFirebase {

    public WorkshopsFirebase workshops;
    public WorkshopsMembersFirebase workshopsMembers;
    public UsersFirebase users;

    public ModelFirebase() {
        FirebaseDatabase fbInstance = FirebaseDatabase.getInstance();
        workshops = new WorkshopsFirebase(fbInstance.getReference("workshops"));
        workshopsMembers = new WorkshopsMembersFirebase(fbInstance.getReference("workshopsMembers"));
        users = new UsersFirebase(fbInstance.getReference("users"));
    }

    public interface GetDataListener<T> {
        void onComplete(T data);
    }
}
