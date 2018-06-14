package com.example.may.myapplication.dal.firebase;

import com.example.may.myapplication.models.User;
import com.example.may.myapplication.models.Workshop;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by May on 4/4/2018.
 */

public class UsersFirebase {
    DatabaseReference ref;

    public UsersFirebase(DatabaseReference ref) {
        this.ref = ref;
    }

    public void saveUser(User u) {
        ref.child(u.getId()).setValue(u);
    }

    public void getAllUsers(long lastUpdateDate, final ModelFirebase.GetDataListener listener) {

        // Getting updates
        Query query = ref.orderByChild("lastUpdated").startAt(lastUpdateDate);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<User> users = new ArrayList<User>();
                for (DataSnapshot elem: dataSnapshot.getChildren()) {
                    users.add(elem.getValue(User.class));
                }
                listener.onComplete(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUserById(String id, final ModelFirebase.GetDataListener listener) {

        ref.child(id).addValueEventListener((new ValueEventListener() {
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
}
