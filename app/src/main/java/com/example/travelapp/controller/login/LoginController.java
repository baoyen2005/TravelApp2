package com.example.travelapp.controller.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.travelapp.model.User;
import com.example.travelapp.view.home.HomeActivity;
import com.example.travelapp.view.login.InterfaceLoginView;
import com.example.travelapp.view.login.LoginActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LoginController implements ILoginController,  GoogleApiClient.OnConnectionFailedListener{
    private InterfaceLoginView iLoginView;
    private static final String TAG = "FacebookLogin";
    private static final int RC_SIGN_IN = 12345;
    private Activity activity;
    ProgressDialog progressDialog;

    public LoginController() {
    }

    public LoginController(InterfaceLoginView loginView, Activity activity) {
        this.iLoginView = loginView;
        this.activity = activity;
        progressDialog = new ProgressDialog(activity);

    }


    @Override
    public void onLoginByUsername(String username, String password, ProgressDialog loadingBar) {
        if (TextUtils.isEmpty(username)) {
            iLoginView.OnLoginError("Please enter your username...");
        } else if (TextUtils.isEmpty(password)) {
            iLoginView.OnLoginError("Please enter your password...");
        } else {
            loadingBar.show();
            FirebaseFirestore.getInstance().collection("users")
                    .whereEqualTo("username", username)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            ArrayList<User> listUser = (ArrayList<User>) queryDocumentSnapshots.toObjects(User.class);
                            for (int i = 0; i < listUser.size(); i++) {
                                if (listUser.get(i) != null) {
                                    if (password.equals(listUser.get(i).getPassword())) {
                                        loadingBar.dismiss();
                                        iLoginView.OnLoginSuccess("Login success, welcome to travel app");
                                    } else {
                                        loadingBar.dismiss();
                                        iLoginView.OnLoginError("Password is wrong, please enter password again!");
                                    }
                                } else {
                                    loadingBar.dismiss();
                                    iLoginView.OnLoginError("User with this password does not exist, please create account");
                                }
                            }

                        }
                    });

        }
    }

    @Override
    public void handleFacebookAccessToken(@NonNull AccessToken token, FirebaseAuth mAuth) {
        Log.d("FacebookLogin", "handleFacebookAccessToken:" + token);
        progressDialog.setMessage("Facebook Sign in...");
        progressDialog.show();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("FacebookLogin", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user)
                            iLoginView.OnLoginSuccess("Authentication Succeeded.");
                            progressDialog.dismiss();
                        }
                        else {
                            progressDialog.dismiss();
                            Log.w("FacebookLogin", "signInWithCredential:failure", task.getException());
                            iLoginView.OnLoginError("Authentication failed.");
                        }
                    }

                });
    }

    @Override
    public void handleGoogleLoginFirebase(String idToken, FirebaseAuth mAuth) {
        progressDialog.setMessage("Google Sign In...");
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "signInWithCredential: success");
                            FirebaseUser user = mAuth.getCurrentUser();
                           // updateUI(user) ;
                            iLoginView.OnLoginSuccess("Authentication successfully");
                            progressDialog.dismiss();
                        }
                        else{
                            progressDialog.dismiss();
                            Log.d(TAG, "signInWithCredential: failed");
                            iLoginView.OnLoginError("Authentication failed");
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(activity , HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
