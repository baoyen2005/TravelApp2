package com.example.travelapp.controller.admin;

import static com.example.travelapp.constant.AddPostByAdminConstant.TAGAdmin;

import android.app.ProgressDialog;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.travelapp.model.Post;
import com.example.travelapp.model.PositionImage;
import com.example.travelapp.view.admin.AdminEditPostInterface;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditPostAdminController implements EditPostAdminControllerInterface {
    private final FragmentActivity fragmentActivity;
    private AdminEditPostInterface adminEditPostInterface;

    public EditPostAdminController(FragmentActivity fragmentActivity, AdminEditPostInterface adminEditPostInterface) {
        this.fragmentActivity = fragmentActivity;
        this.adminEditPostInterface = adminEditPostInterface;
    }

    @Override
    public void editPostInFirebase(Post post, List<PositionImage> listImageUri,
                                   String edtTouristName,
                                   String edtTouristPlace,
                                   String edtLatitude,
                                   String edtLongitude,
                                   String content,
                                   String type,
                                   ProgressDialog loadingBar) {
        if (TextUtils.isEmpty(edtTouristName)) {
            adminEditPostInterface.editPostFailed("Please enter tourist name");
        } else if (TextUtils.isEmpty(edtTouristPlace)) {
            adminEditPostInterface.editPostFailed("Please enter tourist place");
        } else if (TextUtils.isEmpty(edtLatitude)) {
            adminEditPostInterface.editPostFailed("Please enter latitude");
        } else if (TextUtils.isEmpty(edtLongitude)) {
            adminEditPostInterface.editPostFailed("Please enter longitude");
        } else if (TextUtils.isEmpty(content)) {
            adminEditPostInterface.editPostFailed("Please enter content");
        } else if (listImageUri == null) {
            adminEditPostInterface.editPostFailed("List file image is null, please check again");
        } else if (type == null) {
            adminEditPostInterface.editPostFailed("Type is null, please check again");
        } else {
            loadingBar.setMessage("Edit post");
            loadingBar.show();
            Map<String, Object> values = new HashMap<>();
            values.put("touristName", edtTouristName);
            values.put("touristDetailAddress", edtTouristPlace);
            values.put("latitude", edtLatitude);
            values.put("longitude", edtLongitude);
            values.put("type", type);
            values.put("content", content);
            values.put("postId", post.getPostId());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("posts").
                    document(post.getPostId()).
                    update(values).
                    addOnSuccessListener(aVoid -> {

                        if (listImageUri.size() > 0) {
                            upLoadPhoto(listImageUri.get(countUpload).getUri(), post.getPostId(), loadingBar, listImageUri, post);
                        } else {
                            adminEditPostInterface.editPostSuccess("Edit post successfully!");
                            loadingBar.dismiss();
                        }


                    }).addOnFailureListener(
                    e -> adminEditPostInterface.editPostFailed("Add new post fail. Try again"));
        }

    }

    @Override
    public void setDetailDataForView(Post post, List<ImageView> imageViewList, List<EditText> editTextList) {

        setImageview(post.getList_photos().get(0).getUrl(), imageViewList.get(0));
        setImageview(post.getList_photos().get(1).getUrl(), imageViewList.get(1));
        setImageview(post.getList_photos().get(2).getUrl(), imageViewList.get(2));
        setImageview(post.getList_photos().get(3).getUrl(), imageViewList.get(3));
        setImageview(post.getList_photos().get(4).getUrl(), imageViewList.get(4));
        setEdittext(post.getTouristName(), editTextList.get(0));
        setEdittext(post.getTouristDetailAddress(), editTextList.get(1));
        setEdittext(post.getLatitude(), editTextList.get(2));
        setEdittext(post.getLongitude(), editTextList.get(3));
        setEdittext(post.getType(), editTextList.get(4));
        setEdittext(post.getContent(), editTextList.get(5));
    }

    public interface OnUpLoadPhoto {
        void onSuccess();

        void onFailure();
    }

    private void setImageview(String filepath, ImageView imageView) {
        Glide.with(fragmentActivity)
                .load(Uri.parse(filepath))
                .into(imageView);
    }

    private void setEdittext(String text, EditText editText) {
        editText.setText(text);
    }


    //listPhoto.get(countUpload).setUrl(url);
    //countUpload++;
    private int countUpload = 0;

    private void upLoadPhoto(Uri uri, String id, ProgressDialog loadingBar, List<PositionImage> list, Post post) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String name = String.valueOf(System.currentTimeMillis());
        StorageReference storageRef = storage.getReference().child("photos_review_tourist").child(id).child(name);
        UploadTask uploadTask = storageRef.putFile(uri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            storageRef.getDownloadUrl().addOnSuccessListener(url -> {
                updateInfo(url.toString(), post, list.get(countUpload).getPosition(), list, id, loadingBar);
                loadingBar.dismiss();
            });
        });
      //
        uploadTask.addOnFailureListener(e -> {

            if (loadingBar != null && loadingBar.isShowing()) {
                loadingBar.dismiss();
            }
        });
        uploadTask.addOnCanceledListener(() -> {

            if (loadingBar != null && loadingBar.isShowing()) {
                loadingBar.dismiss();
            }
        });
    }

    private void updateInfo(String url, Post post, int po, List<PositionImage> listPt, String postId, ProgressDialog loadingBar) {
        post.getList_photos().get(po).setUrl(url);
        countUpload++;
        if (countUpload >= listPt.size()) {
            updateData(postId, post, loadingBar);
        } else {
            upLoadPhoto(listPt.get(countUpload).getUri(), postId, loadingBar, listPt, post);
        }
    }

    private void updateData(String postid, Post post, ProgressDialog loadingBar) {
        Map<String, Object> map = new HashMap<>();
        map.put("list_photos", post.getList_photos());
        FirebaseFirestore.getInstance().collection("posts")
                .document(postid)
                .set(map, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    if (loadingBar != null && loadingBar.isShowing()) {
                        loadingBar.dismiss();
                    }
                    if (task.isSuccessful()) {
                        Log.d(TAGAdmin, "update thanh cong post");

                    } else {
                        Log.d(TAGAdmin, "fail update imge info");

                    }
                });


    }

}
