package com.example.may.myapplication;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.may.myapplication.fragments.LoginFragment;

/**
 * Created by May on 4/3/2018.
 */

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void onStart() {
        super.onStart();

        initLoginFragment();
    }

    private void initLoginFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        LoginFragment fragment = new LoginFragment();
        fragmentTransaction.add(R.id.content, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }
}