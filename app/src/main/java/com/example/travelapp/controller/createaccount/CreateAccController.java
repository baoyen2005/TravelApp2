package com.example.travelapp.controller.createaccount;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.travelapp.model.User;
import com.example.travelapp.view.activity.login.interface_login.InterfaceLoginView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class CreateAccController implements ICreateAccController {
    private InterfaceLoginView iLoginView;
    private Activity activity;
    private FirebaseAuth firebaseAuth;
    public CreateAccController(InterfaceLoginView iLoginView, Activity activity) {
        this.iLoginView = iLoginView;
        this.activity = activity;

    }


    @Override
    public void onCreateAcc(String username, String phone, String address, String password, String passwordAgain,String email, Uri url, ProgressDialog loadingBar) {
        if (TextUtils.isEmpty(username)) {
            iLoginView.OnUserLoginFail("Please enter your name");
        }  else if (TextUtils.isEmpty(phone)) {
            iLoginView.OnUserLoginFail("Please enter your phone");
        } else if (TextUtils.isEmpty(address)) {
            iLoginView.OnUserLoginFail("Please enter your address");
        }
        else if (TextUtils.isEmpty(password)) {
            iLoginView.OnUserLoginFail("Please enter your password");
        } else if (TextUtils.isEmpty(passwordAgain)) {
            iLoginView.OnUserLoginFail("Please enter your password again");
        }
        else if (!password.equals(passwordAgain)) {
            iLoginView.OnUserLoginFail("Password isn't match,Please enter your pass");
        }
        else if (TextUtils.isEmpty(email)) {
            iLoginView.OnUserLoginFail("Please enter your email again");
        }
        else if(password.length() <8){
            iLoginView.OnUserLoginFail("Password must be at least 6 characters");
        }
        else if (url == null) {
            iLoginView.OnUserLoginFail("Avatar is error, please check again");
        }
        else if(username.contains("admin")){
            iLoginView.OnUserLoginFail("Username can not contain admin");
        }
        else {
            loadingBar.show();
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(
                    new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String uid = user.getUid();

                            createAccount(username,phone,address,password,email,url,uid,loadingBar);
                            iLoginView.OnUserLoginSuccess("Create account success!");
                        }
                    }
            ).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    iLoginView.OnUserLoginFail("Create account fail. Try again");
                    loadingBar.dismiss();
                }
            });
        }


    }

    private void createAccount(String username, String phone, String address, String password,
                               String email, Uri url, String uid, ProgressDialog loadingBar) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users")
                .document(uid);

        Map<String, Object> values = new HashMap<>();
        values.put("username", username);
        values.put("phone", phone);
        values.put("address", address);
        values.put("password", password);
        values.put("email",email);
        values.put("imageURL", "null");
        values.put("uid", uid);
        documentReference.set(values, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (activity.isDestroyed() || activity.isFinishing()) {
                    return;
                }
                if (task.isSuccessful()) {
                    upLoadPhoto(url, documentReference.getId(), loadingBar);
                 //   iLoginView.OnUserLoginSuccess("Create account success!");
                    Log.d("user", "createAccount: thanh cong");
                } else {
//                    iLoginView.OnUserLoginFail("Create account fail. Try again");
                    Log.d("user", "createAccount:  loi");
                }
            }
        });


    }


    private void upLoadPhoto(Uri uri, String uid, ProgressDialog loadingBar) {
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
                Log.d("user", "upLoadPhoto: thanh cong");
                iLoginView.OnUserLoginSuccess("Create account successfully");
            });
        });
        uploadTask.addOnFailureListener(e -> {
            if (activity.isDestroyed() || activity.isFinishing()) {
                return;
            }
            if (loadingBar != null && loadingBar.isShowing()) {
                loadingBar.dismiss();
                Log.d("user", "upLoadPhoto: lỗi");
                iLoginView.OnUserLoginFail("Create account fail. Try again");
            }
        });
        uploadTask.addOnCanceledListener(() -> {
            if (activity.isDestroyed() || activity.isFinishing()) {
                return;
            }
            if (loadingBar != null && loadingBar.isShowing()) {
                loadingBar.dismiss();
                iLoginView.OnUserLoginFail("Create account fail. Try again");
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


}
