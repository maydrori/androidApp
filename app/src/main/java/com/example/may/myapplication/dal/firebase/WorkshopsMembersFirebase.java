package com.example.may.myapplication.dal.firebase;

import com.example.may.myapplication.models.WorkshopMembers;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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

    public void unregisterMemberFromWorkshop(final String workshopId, String userId) {
        ref.child(workshopId).child("registered").child(userId).removeValue();
    }

    public interface LeaveWaitingListListener {
        public void onLeave();
    }

    public void enterWaitingList(final String workshopId, final String userId, final LeaveWaitingListListener listener) {
        ref.child(workshopId).child("waitingList").child(userId).setValue(userId);

        ref.child(workshopId).child("registered").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                registerMemberToWorkshop(workshopId, userId);
                listener.onLeave();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void leaveWaitingList(String workshopId, String userId) {
        ref.child(workshopId).child("waitingList").child(userId).removeValue();
    }

    public void getWorkshopMembers(final String workshopId, final ModelFirebase.GetDataListener listener) {

        ref.child(workshopId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<String> registered = new ArrayList<>();
                List<String> waitingList = new ArrayList<>();

                Map value = (Map) dataSnapshot.getValue();

                if (value != null) {
                    Map registeredMap = (Map) value.get("registered");
                    Map waitingListMap = (Map) value.get("waitingList");

                    if (registeredMap != null) registered = new ArrayList<>(registeredMap.values());
                    if (waitingListMap != null)waitingList = new ArrayList<>(waitingListMap.values());
                }

                listener.onComplete(new WorkshopMembers(registered, waitingList));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteWorkshop(String workshopId) {
        ref.child(workshopId).removeValue();
    }
}
