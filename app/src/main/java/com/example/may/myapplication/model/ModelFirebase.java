package com.example.may.myapplication.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by May on 4/4/2018.
 */

public class ModelFirebase {

    DatabaseReference myRef;

    public ModelFirebase() {
        myRef = FirebaseDatabase.getInstance().getReference("workshops");
    }

    public void addWorkshop(Workshop w) {
        myRef.child(w.getId()).setValue(w);
    }

    public interface GetAllWorkshopsListener {
        void onComplete(List<Workshop> workshops);
    }

    public void getAllWorkshops(final GetAllWorkshopsListener listener) {

        // Getting updates
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Workshop> workshops = new ArrayList<Workshop>();

//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    workshops.add(snapshot.getValue(Workshop.class));
//                }
                listener.onComplete(workshops);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        // Read from the database
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                int n=5;
////                String value = dataSnapshot.getValue(String.class);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//            }
//        });
    }
}
