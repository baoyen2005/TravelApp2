package com.example.travelapp.controller.login;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.LocationManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.travelapp.function_util.GetCurrentLocation;
import com.example.travelapp.model.User;
import com.example.travelapp.view.activity.login.interface_login.IOnLoadUpdateInfoLogin;
import com.example.travelapp.view.activity.login.interface_login.InterfaceLoginView;
import com.example.travelapp.view.activity.login.interface_login.IOnLoadInfoListenerLogin;
import com.example.travelapp.view.interfacefragment.InterfaceGetLocation;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginController implements ILoginController, GoogleApiClient.OnConnectionFailedListener {
    private InterfaceLoginView iLoginView;
    private static final String TAG = "FacebookLogin";
    private Activity activity;
    ProgressDialog progressDialog;

    private static final int REQUEST_LOCATION = 1;
    private GetCurrentLocation getCurrentLocation;
    LocationManager locationManager;

    public LoginController() {
    }

    public LoginController(InterfaceLoginView loginView, Activity activity) {
        this.iLoginView = loginView;
        this.activity = activity;
        progressDialog = new ProgressDialog(activity);
        getCurrentLocation = new GetCurrentLocation(activity);
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

    }


    @Override
    public void onLoginByEmail(String email, String password, ProgressDialog loadingBar) {
        if (TextUtils.isEmpty(email)) {
            iLoginView.OnUserLoginFail("Please enter your username...");
        } else if (TextUtils.isEmpty(password)) {
            iLoginView.OnUserLoginFail("Please enter your password...");
        } else if (email.equals("admin123@gmail.com")) {

            loadingBar.show();
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        loadingBar.dismiss();
                        iLoginView.OnAdminLoginSuccess("Admin login success, welcome to travel app");
                    })
                    .addOnFailureListener(e -> {
                        loadingBar.dismiss();
                        iLoginView.OnAdminLoginFail("Admin with this password does not exist, please check account again");
                    });

        } else {

            loadingBar.show();
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        loadingBar.dismiss();
                        iLoginView.OnUserLoginSuccess("User login success, welcome to travel app");
                    })
                    .addOnFailureListener(e -> {
                        loadingBar.dismiss();
                        iLoginView.OnUserLoginFail("User with this password does not exist, please create account");
                    });

        }
    }

    // bạn ơi bỏ đoạn này đi, nó đang lỗi kệ nó
    @Override
    public void handleFacebookAccessToken(@NonNull AccessToken token, FirebaseAuth mAuth) {
        Log.d("FacebookLogin", "handleFacebookAccessToken:" + token);
        progressDialog.setMessage("Facebook Sign in...");
        progressDialog.show();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        Log.d("FacebookLogin", "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        //     updateAccountToFirebase(user, progressDialog);
                        iLoginView.OnUserLoginSuccess("User login success, welcome to travel app");
                    } else {
                        progressDialog.dismiss();
                        Log.d("FacebookLogin", "signInWithCredential:failure", task.getException());
                        iLoginView.OnUserLoginFail("Authentication failed.");
                    }
                });
    }


    @Override
    public void handleGoogleLoginFirebase(String idToken, FirebaseAuth mAuth, GoogleSignInAccount account) {
        progressDialog.setMessage("Google Sign In...");
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential: success");
                        String url = Objects.requireNonNull(account.getPhotoUrl()).toString();
                        Log.d("__url", "onComplete: "+url);
                        // add cai link nay vao cho nay
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        updateAccountToFirebase(user, account,progressDialog);
                        //iLoginView.OnUserLoginSuccess("Authentication successfully");
                        // progressDialog.dismiss();


                    } else {
                        progressDialog.dismiss();
                        Log.d(TAG, "signInWithCredential: failed");
                        iLoginView.OnUserLoginFail("Authentication failed");
                    }
                });
    }


    @Override
    public void checkConfirmUserName(String userName, IOnLoadInfoListenerLogin listener) {
        FirebaseFirestore.getInstance().collection("users")
                .whereEqualTo("username", userName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<User> listUser = (ArrayList<User>) queryDocumentSnapshots.toObjects(User.class);
                    listener.onSuccess(listUser.size() > 0 && listUser.get(0) != null && listUser.get(0).getUsername().equals(userName)
                            , listUser.get(0).getUid());

                }).addOnFailureListener(e -> listener.onFailure());

    }

    @Override
    public void updateNewPassword(String username, String newPassword, String userid, IOnLoadUpdateInfoLogin loadUpdateInfoLogin) {

        Map<String, Object> map = new HashMap<>();
        map.put("password", newPassword);
        FirebaseFirestore.getInstance().collection("users")
                .document(userid)
                .set(map, SetOptions.merge())
                .addOnCompleteListener(task -> {
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
                })
                .addOnFailureListener(e -> loadUpdateInfoLogin.onFailure());

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void updateAccountToFirebase(FirebaseUser user, GoogleSignInAccount account, ProgressDialog progressDialog) {

        Log.d("user11", "imgpath by gg " + Objects.requireNonNull(user.getPhotoUrl()).getPath());

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users")
                .document(user.getUid());
        Map<String, Object> values = new HashMap<>();
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getCurrentLocation.OnGPS();
        } else {
            getCurrentLocation.getLocation(locationManager, new InterfaceGetLocation() {
                @Override
                public void getLocationSuccess(String longitude, String latitude) {
                    values.put("username", account.getDisplayName());
                    values.put("phone", user.getPhoneNumber());
                    values.put("password", "null");
                    values.put("email", account.getEmail());
                    values.put("imageURL", Objects.requireNonNull(account.getPhotoUrl()).toString());//alt + space && haha
                    values.put("uid", user.getUid());
                    values.put("address", "null");
                    values.put("latitude", latitude);
                    values.put("longitude", longitude);

                    documentReference.set(values, SetOptions.merge()).addOnCompleteListener(task -> {
                        if (activity.isDestroyed() || activity.isFinishing()) {
                            Log.d("updateAccountToFirebase", "updateAccountToFirebase onComplete: activity destroy");

                            return;
                        } else if (task.isSuccessful()) {
                            Log.d("user121", "imgpath by gg " + user.getPhotoUrl().getPath());
                            progressDialog.dismiss();
                            iLoginView.OnUserLoginSuccess("Login gg successfully");
                        } else {
                            Log.d("user", "login gg :  loi");
                            iLoginView.OnUserLoginFail("Login gg failed. Try again");
                        }
                    });

                }

                @Override
                public void getLocationFailed(String mes) {
                    //  values.put("address","null");
                    values.put("longitude", "null");
                    values.put("latitude", "null");
                }
            });


        }
    }
    /*
    private void upLoadPhoto(Uri uri, String uid, ProgressDialog progressDialog) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String name = "avatar";
        StorageReference storageRef = storage.getReference().child("photos").
                child(uid).child(name);
        UploadTask uploadTask = storageRef.putFile(uri);
        Log.d("user", "upLoadPhoto :uri = "+uri);
        Log.d("user", "upLoadPhoto :UploadTask = "+uploadTask.toString());
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            if (activity.isFinishing() || activity.isDestroyed()) {
                Log.d("user", "upLoadPhoto :activity.isFinishing()");
                return;
            }
            storageRef.getDownloadUrl().addOnSuccessListener(url -> {
                updateInfo(url.toString(), uid,progressDialog);
                Log.d("user", "upLoadPhoto by gg: thanh cong");
                progressDialog.dismiss();
                iLoginView.OnUserLoginSuccess("Create account fail successfully");
            });
        });
        uploadTask.addOnFailureListener(e -> {
            if (activity.isDestroyed() || activity.isFinishing()) {
                return;
            }
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                Log.d("user", "upLoadPhoto: lỗi" + e.getMessage());

            }
            iLoginView.OnUserLoginFail("Create account fail. Try again");


        });
        uploadTask.addOnCanceledListener(() -> {
            if (activity.isDestroyed() || activity.isFinishing()) {
                return;
            }
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();

            }
            iLoginView.OnUserLoginFail("Create account fail. Try again");

        });

    }


    private void updateInfo(String url, String uid, ProgressDialog progressDialog) {
        Map<String, Object> map = new HashMap<>();
        map.put("imageURL", url);
        FirebaseFirestore.getInstance().collection("users")
                .document(uid)
                .set(map, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (activity.isDestroyed() || activity.isFinishing()) {
                            Log.d("user", "updateInfo  info : activity.isDestroyed()");
                            return;
                        }
                        if (task.isSuccessful()) {
                            Log.d("user", "update thanh cong info");

                        } else {
                            Log.d("user", "looixupdate info");
                            iLoginView.OnUserLoginFail("Create account fail. Try again");
                        }
                    }
                });
    }

     */
}
