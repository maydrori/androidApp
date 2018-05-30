package com.example.may.myapplication.dal.firebase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
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
        updateLastUpdateDate(workshopId);
    }

    public void unregisterMemberFromWorkshop(final String workshopId, String userId) {
        ref.child(workshopId).child("registered").child(userId).removeValue();
        updateLastUpdateDate(workshopId);
    }

    public interface LeaveWaitingListListener {
        public void onLeave();
    }

    public void enterWaitingList(final String workshopId, final String userId, final LeaveWaitingListListener listener) {
        ref.child(workshopId).child("waitingList").child(userId).setValue(userId);
        updateLastUpdateDate(workshopId);

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
        updateLastUpdateDate(workshopId);
    }

    public void updateLastUpdateDate(String workshopId) {
        ref.child(workshopId).child("lastUpdated").setValue(new Date().getTime());
    }
}
