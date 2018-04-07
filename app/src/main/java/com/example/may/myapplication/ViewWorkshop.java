package com.example.may.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.may.myapplication.model.Model;
import com.example.may.myapplication.model.User;
import com.example.may.myapplication.model.firebase.ModelFirebase;
import com.example.may.myapplication.model.Workshop;
import com.example.may.myapplication.utils.DateFormatter;
import com.example.may.myapplication.utils.UsersManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Map;

/**
 * Created by May on 4/3/2018.
 */

public class ViewWorkshop extends AppCompatActivity {

    Workshop workshop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_workshop);

        workshop = (Workshop)getIntent().getExtras().get("workshop");

        initViewFields();

        Model.instance().getWorkshopMembers(workshop.getId(), new ModelFirebase.GetDataListener<Map<String, List<String>>>() {
            @Override
            public void onComplete(Map<String, List<String>> workshopMembers) {
                String userId = UsersManager.instance().getCurrentUser().getId();

                int btnActionToShow;
                boolean toShowSoldOutButton = false;
                if (!workshopMembers.containsKey("registered")) btnActionToShow = R.id.btn_register;
                else if (workshopMembers.get("registered").size() == workshop.getMaxParticipants()) {
                    btnActionToShow = (workshopMembers.get("waitingList").contains(userId)) ? R.id.btn_leave_waitinglist : R.id.btn_waitinglist;
                    toShowSoldOutButton = true;
                } else if (workshopMembers.get("registered").contains(userId)) {
                    btnActionToShow = R.id.btn_unregister;
                } else {
                    btnActionToShow = R.id.btn_register;
                }

                showRelevantActionButton(btnActionToShow);
                ((TextView)findViewById(R.id.text_sold_out)).setVisibility((toShowSoldOutButton) ? View.VISIBLE : View.INVISIBLE);
            }
        });

        registerEvents();
    }

    private void initViewFields() {
        ImageView teacherPhoto = findViewById(R.id.image_teacher);
        TextView genre = findViewById(R.id.text_genre);
        TextView place = findViewById(R.id.text_place);
        TextView level = findViewById(R.id.text_level);
        TextView date = findViewById(R.id.text_date);
        TextView time = findViewById(R.id.text_time);

        genre.setText(workshop.getGenre());
        place.setText(workshop.getPlace());
        level.setText(workshop.getLevel());
        date.setText(DateFormatter.toDateFormat(workshop.getDate()));
        time.setText(DateFormatter.toTimeFormat(workshop.getDate()));

        setTeacherName(workshop.getTeacherId());
    }

    private void setTeacherName(String id) {
        final TextView teacherName = findViewById(R.id.text_teacher);

        Model.instance().getUserById(id, new ModelFirebase.GetDataListener<User>() {
            @Override
            public void onComplete(User user) {
                teacherName.setText(user.getName());
            }
        });
    }

    private void showRelevantActionButton(int btnId) {
        int[] buttons = new int[]{R.id.btn_register, R.id.btn_unregister, R.id.btn_waitinglist, R.id.btn_leave_waitinglist};

        // Go through the buttons- hide all of them except the one we want to show
        for (int currId : buttons) {
            int visibility = (currId == btnId) ? View.VISIBLE : View.INVISIBLE;
            ((Button)findViewById(currId)).setVisibility(visibility);
        }
    }

    private void registerEvents() {

        final String userId = UsersManager.instance().getCurrentUser().getId();

        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Model.instance().registerMemberToWorkshop(workshop.getId(), userId);
            }
        });

        findViewById(R.id.btn_unregister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Model.instance().unregisterMemberFromWorkshop(workshop.getId(), userId);
            }
        });

        findViewById(R.id.btn_waitinglist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Model.instance().enterWaitingList(workshop.getId(), userId);
            }
        });

        findViewById(R.id.btn_leave_waitinglist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Model.instance().leaveWaitingList(workshop.getId(), userId);
            }
        });
    }
}
