package com.example.may.myapplication.fragments;

import android.content.Intent;
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

import com.example.may.myapplication.MainActivity;
import com.example.may.myapplication.R;
import com.example.may.myapplication.dal.Model;
import com.example.may.myapplication.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) goToMainActivity();
        else handleLoginButton();
    }

    private void goToMainActivity(){
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
    }

    private void handleLoginButton() {

        getView().findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            // Save the user id in order to use it later
            EditText userIdInput = getView().findViewById(R.id.text_user_id);
            String userId = userIdInput.getText().toString().trim();

            mAuth.createUserWithEmailAndPassword(userId, "123565")
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Model.instance().saveUser(new User(user.getUid()));
                            initProfileFragment(user.getUid());
                        } else {
                            Toast.makeText(getActivity(), "FirebaseAuthentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void initProfileFragment(String newUserId) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        UserProfileFragment fragment = UserProfileFragment.instance(newUserId);
        fragmentTransaction.replace(R.id.content, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }
}