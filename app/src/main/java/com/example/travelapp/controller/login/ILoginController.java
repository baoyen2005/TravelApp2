package com.example.travelapp.controller.login;

import android.app.ProgressDialog;

import com.example.travelapp.view.login.interface_login.IOnLoadInfoListenerLogin;
import com.example.travelapp.view.login.interface_login.IOnLoadUpdateInfoLogin;
import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;

public interface ILoginController {
    public void onLoginByUsername(String username, String password, ProgressDialog loadingBar);
    public void handleFacebookAccessToken(AccessToken token, FirebaseAuth mAuth);
    public void handleGoogleLoginFirebase(String idToken,FirebaseAuth mAuth);
    public void checkConfirmUserName(String userName, IOnLoadInfoListenerLogin IOnLoadInfoListenerLogin);
    public void updateNewPassword(String username, String newPassword, String userid, IOnLoadUpdateInfoLogin iOnLoadUpdateInfoLogin);
}
