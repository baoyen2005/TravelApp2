package com.example.travelapp.controller.Profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.travelapp.function_util.UpdateUserImageToFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChangeProfileController {
    private final String TAG = "ChangeProfileController";
    private UpdateEmailToFirebase updateEmailToFirebase;
    private final Activity activity;
    private final ProgressDialog progressDialog ;
    private final UpdateUserImageToFirebase updateUserImageToFirebase;
    public ChangeProfileController(Activity activity) {
        this.activity = activity;
        progressDialog = new ProgressDialog(activity);
        updateUserImageToFirebase = new UpdateUserImageToFirebase(activity);
    }

    public void updateProfile(String uid, String username, String newPassword, String newAddress, String newPhone, String newEmail, Uri mainAvatarUri) {
        progressDialog.setTitle("Updating information");
        progressDialog.show();

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users")
                .document(uid);
        Map<String, Object> values = new HashMap<>();
        values.put("username", username);
        values.put("phone", newPhone);
        values.put("address", newAddress);
        values.put("password", newPassword);
        values.put("email", newEmail);
        values.put("imageURL", "null");
        values.put("uid", uid);
        documentReference.set(values, SetOptions.merge()).addOnCompleteListener(task -> {
            if (activity.isDestroyed() || activity.isFinishing()) {
                Log.d("destroyed", "activity destoyed");
                return;
            }
            if (task.isSuccessful()) {
                updateUserImageToFirebase.uploadPhotoToStorage(mainAvatarUri,uid,progressDialog);
              //  uploadPhotoToStorage(mainAvatarUri, uid,progressDialog);
                Log.d(TAG, "updateProfile: isSuccessful");
            } else {
                Log.d(TAG, "updateProfile: isfail");
            }
        }).addOnFailureListener(e -> {
            Log.d(TAG, "updateProfile: addOnFailureListener");
        });


    }

    public void updateEmailToAuthen(String uid, String newEmail, String currentEmail, String currentPassword,
                                    UpdateEmailToFirebase updateEmail) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(currentEmail, currentPassword); // Current Login Credentials
        if (user != null) {
            user.reauthenticate(credential).addOnCompleteListener(task -> {

                Log.d(TAG, "User re-authenticated.");
                FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                Objects.requireNonNull(user1).updateEmail(newEmail).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        updateEmail.updateEmailSuccess();

                    }
                });
            });
        }
    }
    public void updatePasswordToAuthen(String newPassword,String currentEmail, UpdatePasswordToFirebase updatePasswordToFirebase){
        FirebaseAuth.getInstance().sendPasswordResetEmail(currentEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    updatePasswordToFirebase.updatePasswordSuccess();
                    Log.d(TAG, "onComplete: updatePasswordToAuthen");
                }
                else {
                    updatePasswordToFirebase.updatePasswordFailed();
                    Log.d(TAG, "on fail:updatePasswordToAuthen ");
                }
            }
        }).addOnFailureListener(e -> {
            Log.d(TAG, "updatePasswordToAuthen: addOnFailureListener"+e.getMessage());});
    }


    private void uploadPhotoToStorage(Uri uri, String uid, ProgressDialog loadingBar) {
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

