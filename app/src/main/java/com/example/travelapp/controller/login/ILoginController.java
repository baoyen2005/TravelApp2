package com.example.travelapp.controller.login;

import android.app.ProgressDialog;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;

public interface ILoginController {
    public void onLoginByUsername(String username, String password, ProgressDialog loadingBar);
    public void handleFacebookAccessToken(AccessToken token, FirebaseAuth mAuth);
}
