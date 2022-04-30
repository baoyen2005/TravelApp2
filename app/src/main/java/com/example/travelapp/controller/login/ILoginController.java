package com.example.travelapp.controller.login;

import android.app.ProgressDialog;

import com.example.travelapp.view.activity.login.interface_login.IOnLoadInfoListenerLogin;
import com.example.travelapp.view.activity.login.interface_login.IOnLoadUpdateInfoLogin;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

public interface ILoginController {
    public void onLoginByEmail(String email, String password, ProgressDialog loadingBar);
    public void handleFacebookAccessToken(AccessToken token, FirebaseAuth mAuth);
    public void handleGoogleLoginFirebase(String idToken, FirebaseAuth mAuth, GoogleSignInAccount googleSignInAccount);
    public void checkConfirmUserEmail(String userName, IOnLoadInfoListenerLogin IOnLoadInfoListenerLogin);
    public void updateNewPassword(String username, String newPassword, String userid, IOnLoadUpdateInfoLogin iOnLoadUpdateInfoLogin);
}
