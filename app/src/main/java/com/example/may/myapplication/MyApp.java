package com.example.may.myapplication;

import android.app.Application;
import android.content.Context;

import com.example.may.myapplication.dal.Model;

/**
 * Created by May on 4/4/2018.
 */

public class MyApp extends Application {
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = super.getApplicationContext();
    }
}
