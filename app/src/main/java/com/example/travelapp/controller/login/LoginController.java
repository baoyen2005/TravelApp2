package com.example.travelapp.controller.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.travelapp.model.User;
import com.example.travelapp.view.login.interface_login.IOnLoadUpdateInfoLogin;
import com.example.travelapp.view.login.interface_login.InterfaceLoginView;
import com.example.travelapp.view.login.interface_login.IOnLoadInfoListenerLogin;
import com.facebook.AccessToken;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginController implements ILoginController, GoogleApiClient.OnConnectionFailedListener {
    private InterfaceLoginView iLoginView;
    private static final String TAG = "FacebookLogin";
    private static final int RC_SIGN_IN = 12345;
    private Activity activity;
    ProgressDialog progressDialog;
    private boolean checkUpdate ;

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
                        if (task.isSuccessful()) {
                            Log.d("FacebookLogin", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user)
                            iLoginView.OnLoginSuccess("Authentication Succeeded.");
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            Log.d("FacebookLogin", "signInWithCredential:failure", task.getException());
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
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential: success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // updateUI(user) ;
                            iLoginView.OnLoginSuccess("Authentication successfully");
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            Log.d(TAG, "signInWithCredential: failed");
                            iLoginView.OnLoginError("Authentication failed");
                        }
                    }
                });
    }





    @Override
    public void checkConfirmUserName(String userName, IOnLoadInfoListenerLogin listener) {
        FirebaseFirestore.getInstance().collection("users")
                .whereEqualTo("username", userName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<User> listUser = (ArrayList<User>) queryDocumentSnapshots.toObjects(User.class);
                        listener.onSuccess(listUser.size() > 0 && listUser.get(0) != null && listUser.get(0).getUsername().equals(userName)
                        , listUser.get(0).getUid());

                    }
                }).addOnFailureListener(e -> listener.onFailure());

    }

    @Override
    public void updateNewPassword(String username, String newPassword, String userid, IOnLoadUpdateInfoLogin loadUpdateInfoLogin) {

        Map<String, Object> map = new HashMap<>();
        map.put("password", newPassword);
        FirebaseFirestore.getInstance().collection("users")
                .document(userid)
                .set(map, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (activity.isDestroyed() || activity.isFinishing()) {
                            return;
                        }
                        if (task.isSuccessful()) {
                            Log.d("user", "update thanh cong info");
                            loadUpdateInfoLogin.onSuccess(true);
                        } else {
                            Log.d("user", "looixupdate info");
                            loadUpdateInfoLogin.onSuccess(false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadUpdateInfoLogin.onFailure();
            }
        });

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
