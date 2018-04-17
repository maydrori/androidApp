package com.example.may.myapplication.dal.firebase;

import com.example.may.myapplication.models.User;
import com.example.may.myapplication.models.Workshop;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by May on 4/4/2018.
 */

public class WorkshopsFirebase {

    DatabaseReference ref;

    public WorkshopsFirebase(DatabaseReference ref) {
        this.ref = ref;
    }

    public String getNextWorkshopId() {
        return ref.push().getKey();
    }

    public void addWorkshop(Workshop w) {
        ref.child(w.getId()).setValue(w);
    }

    public void deleteWorkshop(String workshopId) { ref.child(workshopId).removeValue(); }

    public void getWorkshopById(String workshopId, final ModelFirebase.GetDataListener listener) {

        // Getting updates
        ref.child(workshopId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Workshop workshop = dataSnapshot.getValue(Workshop.class);
                listener.onComplete(workshop);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getAllWorkshops(final ModelFirebase.GetDataListener listener) {

        // Getting updates
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Workshop> workshops = new ArrayList<Workshop>();
                for (DataSnapshot elem: dataSnapshot.getChildren()) {
                    workshops.add(elem.getValue(Workshop.class));
                }
                listener.onComplete(workshops);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}