package com.example.travelapp.function_util;

import android.app.Activity;
import android.content.Intent;

import com.example.travelapp.view.activity.login.activity_login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LogOutFunction {
    private Activity activity;

    public LogOutFunction(Activity activity) {
        this.activity = activity;
    }
    public void logOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}
