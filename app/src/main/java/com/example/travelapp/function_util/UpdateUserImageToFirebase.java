package com.example.travelapp.function_util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class UpdateUserImageToFirebase {
    private final Activity activity;
    private final String TAG = "UpdateUserImageToFirebase";

    public UpdateUserImageToFirebase(Activity activity) {
        this.activity = activity;
    }

    @SuppressLint("LongLogTag")
    public void uploadPhotoToStorage(Uri uri, String uid, ProgressDialog loadingBar) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String name = "avatar";
        StorageReference storageRef = storage.getReference().child("photos").child(uid).child(name);
        UploadTask uploadTask = storageRef.putFile(uri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
            storageRef.getDownloadUrl().addOnSuccessListener(url -> {
                updateUserImageToFireStorage(url.toString(), uid);
                Log.d(TAG, "upLoadPhoto: thanh cong");
                loadingBar.dismiss();
            });
        });
        uploadTask.addOnFailureListener(e -> {
            if (activity.isDestroyed() || activity.isFinishing()) {
                return;
            }
            if (loadingBar != null && loadingBar.isShowing()) {
                loadingBar.dismiss();
                Log.d(TAG, "upLoadPhoto: lỗi");

            }
        });
        uploadTask.addOnCanceledListener(() -> {
            if (activity.isDestroyed() || activity.isFinishing()) {
                return;
            }
            if (loadingBar != null && loadingBar.isShowing()) {
                loadingBar.dismiss();

            }
        });

    }


    @SuppressLint("LongLogTag")
    private void updateUserImageToFireStorage(String url, String uid) {
        Map<String, Object> map = new HashMap<>();
        map.put("imageURL", url);
        FirebaseFirestore.getInstance().collection("users")
                .document(uid)
                .set(map, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    if (activity.isDestroyed() || activity.isFinishing()) {
                        return;
                    }
                    if (task.isSuccessful()) {
                        Log.d(TAG, "update thanh cong info");


                    } else {
                        Log.d(TAG, "looixupdate info");

                    }
                });
    }

}
