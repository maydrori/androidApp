package com.example.may.myapplication.model.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by May on 4/4/2018.
 */

public class WorkshopsMembersFirebase {

    DatabaseReference ref;

    public WorkshopsMembersFirebase(DatabaseReference ref) {
        this.ref = ref;
    }

    public void registerMemberToWorkshop(String workshopId, String userId) {
        ref.child(workshopId).child("registered").child(userId).setValue(userId);
    }

    public void unregisterMemberFromWorkshop(String workshopId, String userId) {
        ref.child(workshopId).child("registered").child(userId).removeValue();
    }

    public void enterWaitingList(String workshopId, String userId) {
        ref.child(workshopId).child("waitingList").child(userId).setValue(userId);
    }

    public void leaveWaitingList(String workshopId, String userId) {
        ref.child(workshopId).child("waitingList").child(userId).removeValue();
    }

    public void getWorkshopMembers(String workshopId, final ModelFirebase.GetDataListener listener) {
        ref.child(workshopId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, List<String>> workshopMembers = new HashMap<>();
                for (DataSnapshot elem: dataSnapshot.getChildren()) {

                    List<String> users = convertToList(elem.getChildren());
                    workshopMembers.put(elem.getKey(), users);
                }
                listener.onComplete(workshopMembers);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private <T> List<T> convertToList(Iterable<DataSnapshot> data) {
        List<T> lst = new ArrayList<>();
        for (DataSnapshot curr : data) {
            lst.add((T)curr.getValue());
        }

        return lst;
    }
}
