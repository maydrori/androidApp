package com.example.may.myapplication.model.firebase;

import com.example.may.myapplication.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by May on 4/4/2018.
 */

public class UsersFirebase {
    DatabaseReference ref;

    public UsersFirebase(DatabaseReference ref) {
        this.ref = ref;
    }

    public void addUser(User u) {
        ref.child(u.getId()).setValue(u);
    }

    public void getUserById(String id, final ModelFirebase.GetDataListener listener) {

        ref.child(id).addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                listener.onComplete(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }));
    }

    public void updateUser(User u) {
        ref.child(u.getId()).setValue(u);
    }
}
