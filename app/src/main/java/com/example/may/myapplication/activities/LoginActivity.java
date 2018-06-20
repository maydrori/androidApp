package com.example.may.myapplication.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.may.myapplication.R;
import com.example.may.myapplication.fragments.LoginFragment;

/**
 * Created by May on 4/3/2018.
 */

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginFragment fragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment)
                .commit();
    }
}