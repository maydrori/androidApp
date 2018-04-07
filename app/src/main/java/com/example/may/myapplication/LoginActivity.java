package com.example.may.myapplication;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.may.myapplication.fragments.LoginFragment;
import com.example.may.myapplication.fragments.ProfileFragment;
import com.example.may.myapplication.fragments.WorkshopsCalendarFragment;
import com.example.may.myapplication.model.Model;
import com.example.may.myapplication.model.User;
import com.example.may.myapplication.model.firebase.ModelFirebase;
import com.example.may.myapplication.utils.UsersManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        LoginFragment fragment = new LoginFragment();
        fragmentTransaction.add(R.id.content, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }
}