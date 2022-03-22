package com.example.travelapp.controller.admin;

import static com.example.travelapp.constant.AddPostByAdminConstant.TAGAdmin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.travelapp.view.admin.AdminHomeFragmentViewInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNewHomeController implements AddNewHomeControllerInterface {
    private DatabaseReference databaseReference;
    private AdminHomeFragmentViewInterface adNewPostViewInterface;
    private Activity fragment;

    public AddNewHomeController(AdminHomeFragmentViewInterface adNewPostViewInterface) {
        this.adNewPostViewInterface = adNewPostViewInterface;
        this.fragment = this.fragment;
    }

    @Override
    public void addPostToFirebase(List<Uri> listFilePath, String edtTouristName, String edtTouristPlace,
                                  String edtLatitude, String edtLongitude, String content, String type, ProgressDialog loadingBar) {
        if (TextUtils.isEmpty(edtTouristName)) {
            adNewPostViewInterface.addNewPostFailed("Please enter tourist name");
        } else if (TextUtils.isEmpty(edtTouristPlace)) {
            adNewPostViewInterface.addNewPostFailed("Please enter tourist place");
        } else if (TextUtils.isEmpty(edtLatitude)) {
            adNewPostViewInterface.addNewPostFailed("Please enter latitude");
        } else if (TextUtils.isEmpty(edtLongitude)) {
            adNewPostViewInterface.addNewPostFailed("Please enter longitude");
        } else if (TextUtils.isEmpty(content)) {
            adNewPostViewInterface.addNewPostFailed("Please enter content");
        } else if (listFilePath == null) {
            adNewPostViewInterface.addNewPostFailed("List file image is null, please check again");
        }
        else if (type == null) {
            adNewPostViewInterface.addNewPostFailed("Type is null, please check again");
        }
        else {
            loadingBar.setMessage("Add new post");
            loadingBar.show();
            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("posts")
                    .document();

            Map<String, Object> values = new HashMap<>();
            values.put("touristName", edtTouristName);
            values.put("touristDetailAddress", edtTouristPlace);
            values.put("latitude", edtLatitude);
            values.put("longitude", edtLongitude);
            values.put("type",type);
            values.put("image1", "null");
            values.put("image2", "null");
            values.put("image3", "null");
            values.put("image4", "null");
            values.put("image0", "null");
            values.put("content",content);
            values.put("postId", documentReference.getId());
            documentReference.set(values, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        upLoadPhotos(listFilePath, documentReference.getId(),loadingBar);
                        adNewPostViewInterface.addNewPostSuccess("Add new post successfully!");
                        loadingBar.dismiss();
                        Log.d(TAGAdmin, "them bai moi : thanh cong");
                    } else {
                        adNewPostViewInterface.addNewPostFailed("Add new post fail. Try again");
                        Log.d(TAGAdmin, "them bai moi:  loi");
                    }
                }
            });

        }

    }

    private void upLoadPhotos(List<Uri> listFilePath, String id,ProgressDialog loadingBar) {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference().child("photos_review_tourist").child(id);

        for (int uploads = 0; uploads < listFilePath.size(); uploads++) {
            Uri imageUri = listFilePath.get(uploads);
            final StorageReference imagename = storageRef.child("image/" + imageUri.getLastPathSegment());

            int finalUploads = uploads;
            imagename.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String url = String.valueOf(uri);
                            updateData(url, id, finalUploads);
                            Log.d("user", "upLoadPhoto: thanh cong");
                           // adNewPostViewInterface.addNewPostSuccess("Upload list images successfully");
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    if (loadingBar != null && loadingBar.isShowing()) {
                        loadingBar.dismiss();
                        Log.d("user", "upLoadPhoto: lỗi");
                        adNewPostViewInterface.addNewPostFailed("Add images fail. Try again");
                    }
                }
            }).addOnCanceledListener(() -> {
                if (fragment.isDestroyed() || fragment.isFinishing()) {
                    return;
                }
                if (loadingBar != null && loadingBar.isShowing()) {
                    loadingBar.dismiss();
                    adNewPostViewInterface.addNewPostFailed("Add images fail. Try again");
                }
            });
        }
    }


    private void updateData(String url, String postid, int finalUploads) {
        Map<String, Object> map = new HashMap<>();
        map.put("image"+finalUploads, url);
        FirebaseFirestore.getInstance().collection("posts")
                .document(postid)
                .set(map, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Log.d(TAGAdmin, "update thanh cong post");
                           // adNewPostViewInterface.addNewPostSuccess("Create new post successfully");
                        } else {
                            Log.d(TAGAdmin, "fail update imge info");
                          ///  adNewPostViewInterface.addNewPostFailed("Create new post fail. Try again");
                        }
                    }
                });
    }

}
