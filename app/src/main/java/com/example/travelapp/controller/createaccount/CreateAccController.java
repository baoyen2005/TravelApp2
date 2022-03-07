package com.example.travelapp.controller.createaccount;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.travelapp.view.login.InterfaceLoginView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

    public CreateAccController(InterfaceLoginView iLoginView, Activity activity) {
        this.iLoginView = iLoginView;
        this.activity = activity;
    }


    @Override
    public void onCreateAcc(String username, String phone, String address, String password, String passwordAgain, Uri url, ProgressDialog loadingBar) {
        if (TextUtils.isEmpty(username)) {
            iLoginView.OnLoginError("Please enter your name");
        }  else if (TextUtils.isEmpty(phone)) {
            iLoginView.OnLoginError("Please enter your phone");
        } else if (TextUtils.isEmpty(address)) {
            iLoginView.OnLoginError("Please enter your address");
        }
        else if (TextUtils.isEmpty(password)) {
            iLoginView.OnLoginError("Please enter your password");
        } else if (TextUtils.isEmpty(passwordAgain)) {
            iLoginView.OnLoginError("Please enter your password again");
        }
        else if (!password.equals(passwordAgain)) {
            iLoginView.OnLoginError("Password isn't match,Please enter your pass");
        }
        else if(password.length() <8){
            iLoginView.OnLoginError("Password must be at least 6 characters");
        }
        else if (url == null) {
            iLoginView.OnLoginError("Avatar is error, please check again");
        } else {
            loadingBar.show();
            FirebaseFirestore.getInstance().collection("users")
                    .whereEqualTo("username", username)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots != null) {
                                if (queryDocumentSnapshots.size() > 0) {
                                    //ddax ton tai
                                    Log.d("user", "acc da ton tai");
                                    iLoginView.OnLoginError("Account is invalid, it is exits. Please choose another username");
                                    loadingBar.dismiss();
                                } else {
                                    createAccount(username, phone, address, password, passwordAgain, url, loadingBar);
                                    iLoginView.OnLoginSuccess("Create account successfully!");
                                    Log.d("user", "create acc onSuccess: ");
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("__tag", "onFailure: ");
                }
            });
        }


    }

    private void createAccount(String username, String phone, String address, String password, String passwordAgain, Uri url, ProgressDialog loadingBar) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users")
                .document();

        Map<String, Object> values = new HashMap<>();
        values.put("username", username);
        values.put("phone", phone);
        values.put("address", address);
        values.put("password", password);
        values.put("imageURL", "null");
        values.put("uid", documentReference.getId());
        documentReference.set(values, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (activity.isDestroyed() || activity.isFinishing()) {
                    return;
                }
                if (task.isSuccessful()) {
                    upLoadPhoto(url, documentReference.getId(), loadingBar);
                    iLoginView.OnLoginSuccess("Create account success!");
                    Log.d("user", "createAccount: thanh cong");
                } else {
                    iLoginView.OnLoginError("Create account fail. Try again");
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
                iLoginView.OnLoginSuccess("Create account successfully");
            });
        });
        uploadTask.addOnFailureListener(e -> {
            if (activity.isDestroyed() || activity.isFinishing()) {
                return;
            }
            if (loadingBar != null && loadingBar.isShowing()) {
                loadingBar.dismiss();
                Log.d("user", "upLoadPhoto: lỗi");
                iLoginView.OnLoginError("Create account fail. Try again");
            }
        });
        uploadTask.addOnCanceledListener(() -> {
            if (activity.isDestroyed() || activity.isFinishing()) {
                return;
            }
            if (loadingBar != null && loadingBar.isShowing()) {
                loadingBar.dismiss();
                iLoginView.OnLoginError("Create account fail. Try again");
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
                            iLoginView.OnLoginSuccess("Create account fail successfully");
                        } else {
                            Log.d("user", "looixupdate info");
                            iLoginView.OnLoginError("Create account fail. Try again");
                        }
                    }
                });
    }

    private void getData() {

    }
}
