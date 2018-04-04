package com.example.may.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.may.myapplication.model.Member;
import com.example.may.myapplication.model.Workshop;

/**
 * Created by May on 4/3/2018.
 */

public class ViewWorkshop extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_workshop);

        registerEvents();
    }

    private void registerEvents() {

        // Listen to button event
        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO
                Member currentLoginMember = Member.members.get(0);

                String workshopId = view.getTag().toString();

                for (Workshop workshop : MainActivity.allWorkshop) {
                    if (workshop.getId().equals(workshopId)) {
                        workshop.addMember(currentLoginMember.getId());
                    }
                }

                // UPDATE workshop in db
            }
        });
    }
}
