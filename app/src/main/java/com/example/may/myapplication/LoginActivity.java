package com.example.may.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by May on 4/3/2018.
 */

public class LoginActivity extends AppCompatActivity {

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sp = getSharedPreferences("login",MODE_PRIVATE);

        // If the user already logged in to the app, go straight to the main activity
        if(sp.getBoolean("logged",false)){
            goToMainActivity();
        }

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Save the user id in order to use it later
                EditText userIdInput = findViewById(R.id.text_user_id);
                int userId = Integer.valueOf(userIdInput.getText().toString());

                sp.edit().putBoolean("logged",true).apply();
                sp.edit().putInt("id", userId);

                goToMainActivity();
            }
        });
    }

    private void goToMainActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}