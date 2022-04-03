package com.example.travelapp.controller.login;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.LocationManager;
import android.net.Uri;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginController implements ILoginController, GoogleApiClient.OnConnectionFailedListener {
    private InterfaceLoginView iLoginView;
    private static final String TAG = "FacebookLogin";
    private static final int RC_SIGN_IN = 12345;
    private Activity activity;
    ProgressDialog progressDialog;
    private boolean checkUpdate;
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
        ActivityCompat.requestPermissions( activity,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

    }


    @Override
    public void onLoginByEmail(String email, String password, ProgressDialog loadingBar) {
        if (TextUtils.isEmpty(email)) {
            iLoginView.OnUserLoginFail("Please enter your username...");
        } else if (TextUtils.isEmpty(password)) {
            iLoginView.OnUserLoginFail("Please enter your password...");
        }
        else if (email.equals("admin123@gmail.com")) {

            loadingBar.show();
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            loadingBar.dismiss();
                            iLoginView.OnAdminLoginSuccess("Admin login success, welcome to travel app");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                         @Override
                        public void onFailure(@NonNull Exception e) {
                             loadingBar.dismiss();
                             iLoginView.OnAdminLoginFail("Admin with this password does not exist, please check account again");
                        }
                    });

            }

        else {

            loadingBar.show();
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            loadingBar.dismiss();
                            iLoginView.OnUserLoginSuccess("User login success, welcome to travel app");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadingBar.dismiss();
                            iLoginView.OnUserLoginFail("User with this password does not exist, please create account");
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
                            createAccount(user);
                            iLoginView.OnUserLoginSuccess("Authentication Succeeded.");
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            Log.d("FacebookLogin", "signInWithCredential:failure", task.getException());
                            iLoginView.OnUserLoginFail("Authentication failed.");
                        }
                    }

                });
    }

    private void createAccount(FirebaseUser user){

        Log.d("user11", "imgpath by gg "+ user.getPhotoUrl().getPath());

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
                    values.put("username", user.getDisplayName());
                    values.put("phone", user.getPhoneNumber());
                    values.put("password", "null");
                    values.put("email",user.getEmail());
                    values.put("imageURL", "null");
                    values.put("uid", user.getUid());
                    values.put("address", longitude +"/" +latitude);

                    documentReference.set(values, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (activity.isDestroyed() || activity.isFinishing()) {
                                return;
                            }
                            if (task.isSuccessful()) {
                                Log.d("user121", "imgpath by gg "+ user.getPhotoUrl().getPath());
                                upLoadPhoto(Uri.parse(user.getPhotoUrl().getPath()), user.getUid());
                                //   iLoginView.OnUserLoginSuccess("Create account success!");
                                Log.d("user122", "imgpath by gg "+ user.getPhotoUrl().getPath());
                            } else {
//                    iLoginView.OnUserLoginFail("Create account fail. Try again");
                                Log.d("user", "createAccount:  loi");
                            }
                        }
                    });

                }

                @Override
                public void getLocationFailed(String mes) {
                    values.put("address","null");
                }
            });


        }





    }


    private void upLoadPhoto(Uri uri, String uid) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String name = "avatar";
        StorageReference storageRef = storage.getReference().child("photos").child(uid).child(name);
        UploadTask uploadTask = storageRef.putFile(uri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
            storageRef.getDownloadUrl().addOnSuccessListener(url -> {
                updateInfo(url.toString(), uid);
                Log.d("user", "upLoadPhoto by gg: thanh cong");
            });
        });
        uploadTask.addOnFailureListener(e -> {
            if (activity.isDestroyed() || activity.isFinishing()) {
                return;
            }
        });
        uploadTask.addOnCanceledListener(() -> {
            if (activity.isDestroyed() || activity.isFinishing()) {
                return;
            }

        });

    }


    private void updateInfo(String url, String uid) {
        Map<String, Object> map = new HashMap<>();
        map.put("imageURL", url);
        FirebaseFirestore.getInstance().collection("users")
                .document(uid)
                .set(map, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (activity.isDestroyed() || activity.isFinishing()) {
                            return;
                        }
                        if (task.isSuccessful()) {
                            Log.d("user", "update thanh cong info");
                            iLoginView.OnUserLoginSuccess("Create account fail successfully");
                        } else {
                            Log.d("user", "looixupdate info");
                            iLoginView.OnUserLoginFail("Create account fail. Try again");
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
                            createAccount(user);
                            iLoginView.OnUserLoginSuccess("Authentication successfully");
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            Log.d(TAG, "signInWithCredential: failed");
                            iLoginView.OnUserLoginFail("Authentication failed");
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
