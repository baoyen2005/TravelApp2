package com.example.travelapp.controller.admin;

import static com.example.travelapp.constant.AddPostByAdminConstant.TAGAdmin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.travelapp.model.Photo;
import com.example.travelapp.model.PositionImage;
import com.example.travelapp.view.admin.AdminHomeFragmentViewInterface;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
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
    public void addPostToFirebase(List<PositionImage> listFilePath, String edtTouristName, String edtTouristPlace,
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
        } else if (type == null) {
            adNewPostViewInterface.addNewPostFailed("Type is null, please check again");
        } else {
            loadingBar.setMessage("Add new post");
            loadingBar.show();
            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("posts")
                    .document();

            Map<String, Object> values = new HashMap<>();
            values.put("touristName", edtTouristName);
            values.put("touristDetailAddress", edtTouristPlace);
            values.put("latitude", edtLatitude);
            values.put("longitude", edtLongitude);
            values.put("type", type);
            values.put("content", content);
            values.put("postId", documentReference.getId());
            documentReference.set(values, SetOptions.merge()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (listFilePath.size() > 0) {
                        upLoadPhotos(listFilePath, documentReference.getId(), loadingBar);
                    } else {
                        adNewPostViewInterface.addNewPostSuccess("Add new post successfully!");
                        loadingBar.dismiss();
                        Log.d(TAGAdmin, "them bai moi : thanh cong");
                    }


                } else {
                    adNewPostViewInterface.addNewPostFailed("Add new post fail. Try again");
                    loadingBar.dismiss();
                    Log.d(TAGAdmin, "them bai moi:  loi");
                }
            });

        }

    }

    private void upLoadPhotos(List<PositionImage> listFilePath, String id, ProgressDialog loadingBar) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("photos_review_tourist").child(id);
        Uri imageUri = listFilePath.get(countUpload).getUri();
        final StorageReference imagename = storageRef.child("image/" + imageUri.getLastPathSegment());
        imagename.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                imagename.getDownloadUrl().addOnSuccessListener(uri -> {
                    String url = String.valueOf(uri);
                    updateData(url, id, listFilePath.size(), imageUri.getLastPathSegment(), listFilePath, loadingBar);
                    Log.d("user", "upLoadPhoto: thanh cong");
                    // adNewPostViewInterface.addNewPostSuccess("Upload list images successfully");
                })).addOnFailureListener(e -> {

                    if (loadingBar != null && loadingBar.isShowing()) {
                        loadingBar.dismiss();
                        Log.d("user", "upLoadPhoto: lỗi");
                        adNewPostViewInterface.addNewPostFailed("Add images fail. Try again");
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

    int countUpload = 0;
    List<Photo> photoList = new ArrayList<>();

    private void updateData(String url, String postid, int size, String name, List<PositionImage> listFilePath, ProgressDialog loadingBar) {
        countUpload++;
        photoList.add(new Photo(url, name));
        if (countUpload >= size) {
            Map<String, Object> map = new HashMap<>();
            map.put("list_photos", photoList);
            FirebaseFirestore.getInstance().collection("posts")
                    .document(postid)
                    .set(map, SetOptions.merge())
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            Log.d(TAGAdmin, "update thanh cong post");
                            // adNewPostViewInterface.addNewPostSuccess("Create new post successfully");
                        } else {
                            Log.d(TAGAdmin, "fail update imge info");
                            ///  adNewPostViewInterface.addNewPostFailed("Create new post fail. Try again");
                        }
                    });
        } else {
            upLoadPhotos(listFilePath, postid, loadingBar);
        }

    }

}
