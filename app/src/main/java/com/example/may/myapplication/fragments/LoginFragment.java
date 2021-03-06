package com.example.may.myapplication.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.may.myapplication.activities.MainActivity;
import com.example.may.myapplication.R;
import com.example.may.myapplication.dal.Model;
import com.example.may.myapplication.dal.room.AppDatabase;
import com.example.may.myapplication.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.Date;
import java.util.List;

/**
 * Created by May on 4/7/2018.
 */

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        // Go straight to main activity if the user already logged in
        if (FirebaseAuth.getInstance().getCurrentUser() != null) goToMainActivity();
        else handleLoginButton();
    }

    private void goToMainActivity(){
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
    }

    private void createNewUser(String userEmail, String password) {
        mAuth.createUserWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String userUid = mAuth.getCurrentUser().getUid();

                        // Save the user in the db
                        Model.instance().saveUser(new User(userUid));

                        // Go to profile screen in order to edit his details
                        initProfileFragment(userUid);
                    } else {
                        authFailed();
                    }
                }
            });
    }

    private void signIn(String userEmail, String password) {
        mAuth.signInWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            goToMainActivity();
                        } else {
                            authFailed();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                authFailed();
            }
        });
    }

    private void authFailed () {
        String msg =  getContext().getResources().getString(R.string.authFailed);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void handleLoginButton() {

        getView().findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            // Save the user id in order to use it later
            EditText userIdInput = getView().findViewById(R.id.text_user_id);
            EditText passwordInput = getView().findViewById(R.id.text_password);
            final String userEmail = userIdInput.getText().toString().trim();
            final String userPassword = passwordInput.getText().toString().trim();

            // If one of the fields empty, alert fail
            if (userEmail.isEmpty() || userPassword.isEmpty()) authFailed();
            else {
                // Check if the user already in the system.
                // If the user doesn't exist, the result will be empty array and we will create a new one
                mAuth.fetchSignInMethodsForEmail(userEmail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.getResult().getSignInMethods().isEmpty()) {
                            createNewUser(userEmail, userPassword);
                        }
                        else signIn(userEmail, userPassword);
                    }
                });
            }
            }
        });
    }

    private void initProfileFragment(String newUserId) {
        UserProfileFragment fragment = UserProfileFragment.instance(newUserId);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment)
                .addToBackStack(null)
                .commit();
    }
}