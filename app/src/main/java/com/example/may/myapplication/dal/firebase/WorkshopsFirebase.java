package com.example.may.myapplication.dal.firebase;

import com.example.may.myapplication.models.Workshop;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by May on 4/4/2018.
 */

public class WorkshopsFirebase {

    DatabaseReference ref;
    ValueEventListener eventListener;

    public WorkshopsFirebase(DatabaseReference ref) {
        this.ref = ref;
    }

    public String getNextWorkshopId() {
        return ref.push().getKey();
    }

    public void saveWorkshop(Workshop w) {
        w.setLastUpdated(new Date().getTime());
        ref.child(w.getId()).setValue(w);
    }

    public void deleteWorkshop(String workshopId) {
        ref.child(workshopId).child("isDeleted").setValue(true);
        updateLastUpdateDate(workshopId);
    }

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

    public void getAllWorkshops(long lastUpdateDate, final ModelFirebase.GetDataListener listener) {

        // Getting updates
        Query query = ref.orderByChild("lastUpdated").startAt(lastUpdateDate);
        eventListener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Workshop> workshops = new ArrayList<Workshop>();
                for (DataSnapshot elem: dataSnapshot.getChildren()) {
                    Map workshopMap = (Map) elem.getValue();
                    Workshop workshop = elem.getValue(Workshop.class);

                    Map registeredMap = (Map) workshopMap.get("registered");
                    Map waitingListMap = (Map) workshopMap.get("waitingList");

                    workshop.registeredMembers = (registeredMap != null) ? new ArrayList<String>(registeredMap.values()) : new ArrayList<String>();
                    workshop.waitingListMembers = (waitingListMap != null) ? (new ArrayList<String>(waitingListMap.values())) : new ArrayList<String>();

                    workshops.add(workshop);
                }
                listener.onComplete(workshops);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void registerMemberToWorkshop(String workshopId, String userId) {
        ref.child(workshopId).child("registered").child(userId).setValue(userId);
        updateLastUpdateDate(workshopId);
    }

    public void unregisterMemberFromWorkshop(final String workshopId, String userId) {
        ref.child(workshopId).child("registered").child(userId).removeValue();
        updateLastUpdateDate(workshopId);

        handleWaitingList(workshopId);
    }

    private void handleWaitingList(final String workshopId) {

        // Register the first student waiting in the waiting list for this workshop
        ref.child(workshopId).child("waitingList").limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();

                if (it.hasNext()) {
                    String firstUserWaiting = it.next().getKey();

                    leaveWaitingList(workshopId, firstUserWaiting);
                    registerMemberToWorkshop(workshopId, firstUserWaiting);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void enterWaitingList(final String workshopId, final String userId) {
        ref.child(workshopId).child("waitingList").child(userId).setValue(userId);
        updateLastUpdateDate(workshopId);
    }

    public void leaveWaitingList(String workshopId, String userId) {
        ref.child(workshopId).child("waitingList").child(userId).removeValue();
        updateLastUpdateDate(workshopId);
    }

    public void updateLastUpdateDate(String workshopId) {
        ref.child(workshopId).child("lastUpdated").setValue(new Date().getTime());
    }
}
