package com.example.may.myapplication.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.may.myapplication.R;
import com.example.may.myapplication.fragments.ViewWorkshopFragment;

/**
 * Created by May on 4/22/2018.
 */

public class ViewWorkshop extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_workshop);

        Fragment fragment = ViewWorkshopFragment.instance(getIntent().getExtras().getString("workshopId"));
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }
}
