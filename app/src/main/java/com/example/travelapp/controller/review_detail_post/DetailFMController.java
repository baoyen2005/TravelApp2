package com.example.travelapp.controller.review_detail_post;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.travelapp.function_util.GetPostFromFirebaseStorage;
import com.example.travelapp.function_util.GetUserFromFireStorage;
import com.example.travelapp.model.Post;
import com.example.travelapp.model.User;
import com.example.travelapp.view.interfacefragment.InterfaceEventGetCurrentUserListener;
import com.example.travelapp.view.interfacefragment.InterfaceEventGetPostByIDListener;
import com.example.travelapp.view.interfacefragment.InterfaceObserverListFavorPostID;
import com.example.travelapp.view.interfacefragment.InterfaceRatingStarListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailFMController implements InterfaceDetailFMController {
    private GetPostFromFirebaseStorage getPostFromFirebaseStorage;
    private GetUserFromFireStorage getUserFromFireStorage;
    private Context context;
    double distance = 0f;
    private InterfaceRatingStarListener interfaceRatingStarListener;

    public DetailFMController(Context context, InterfaceRatingStarListener interfaceRatingStarListener) {
        this.context = context;
        this.interfaceRatingStarListener = interfaceRatingStarListener;
    }

    @Override
    public void setDataForDetailPostFM(List<ImageView> listImageView, List<TextView> listTextView, String postID) {
        getPostFromFirebaseStorage = new GetPostFromFirebaseStorage();
        getUserFromFireStorage = new GetUserFromFireStorage();
        Log.d("__postID", "setDataForDetailPostFM: postID = " + postID);
        getPostFromFirebaseStorage.getPostFromFirebaseByID(postID, new InterfaceEventGetPostByIDListener() {
            @Override
            public void getPostByIDSuccess(Post post) {
                setImageByGlide(post, listImageView);
                setTextForTextView(post, listTextView);
            }

            @Override
            public void getPostsFail(String isFail) {
            }
        });
    }

    @Override
    public void rateStarForApp(String ratingBar, String totalStar, String postID, String uid) {

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("star")
                .document();
        Map<String, Object> values = new HashMap<>();
        values.put("uid", uid);
        values.put("postid", postID);
        values.put("ratingStar", ratingBar);
        values.put("totalStar", totalStar);

        documentReference.set(values, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (context == null) {
                    Log.d("destroyed", "activity destoyed");
                    return;
                }
                if (task.isSuccessful()) {
                    String mes = "Thank you for rating app";
                    interfaceRatingStarListener.ratingStarSuccess(mes);
                    Log.d("ratingStar", "update rating star: thanh cong");
                } else {
                    Log.d("ratingStar", "onComplete: task.fail ");
                    interfaceRatingStarListener.ratingStarFail("update rating star failed");
                }
            }
        });
    }

    @Override
    public void pushDataToFirebase(String postId, InterfaceObserverListFavorPostID observerListFavorPostID, String uid) {
        getPostFromFirebaseStorage = new GetPostFromFirebaseStorage();
        getUserFromFireStorage = new GetUserFromFireStorage();
        Log.d("__postID", "setDataForDetailPostFM: postID = " + postId);
        getPostFromFirebaseStorage.getPostFromFirebaseByID(postId, new InterfaceEventGetPostByIDListener() {

            @Override
            public void getPostByIDSuccess(Post post) {
                Map<String, Object> map = new HashMap<>();
                map.put("uid", uid);
                map.put("postId", postId);
                map.put("touristName", post.getTouristName());
                map.put("touristDetailAddress", post.getTouristDetailAddress());
                map.put("content", post.getContent());
                map.put("type", post.getType());
                map.put("list_photos", post.getList_photos());
                map.put("latitude", post.getLatitude());
                map.put("longitude", post.getLongitude());


                FirebaseFirestore.getInstance().collection("favorite")
                        .document(postId)
                        .set(map, SetOptions.merge())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Đã thích", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Lỗi rồi ^^", Toast.LENGTH_SHORT).show();
                            }
                        });

            }

            @Override
            public void getPostsFail(String isFail) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setTextForTextView(Post post, List<TextView> listTextView) {
        listTextView.get(0).setText(post.getTouristName());
        calculateDistance(post, listTextView.get(1));

        listTextView.get(2).setText(post.getContent());
    }

    private void calculateDistance(Post post, TextView textView) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        getUserFromFireStorage.getUserFromFirebase(firebaseUser.getUid(), new InterfaceEventGetCurrentUserListener() {
            @Override
            public void getCurrentUserSuccess(User user) {
                Location location1 = new Location("");
                try {
                    double theta = Double.parseDouble(post.getLongitude()) - Double.parseDouble(user.getLongitude());
                    distance = Math.sin(deg2rad(Double.parseDouble(user.getLatitude())))
                            * Math.sin(deg2rad(Double.parseDouble(post.getLatitude())))
                            + Math.cos(deg2rad(Double.parseDouble(user.getLatitude())))
                            * Math.cos(deg2rad(Double.parseDouble(post.getLatitude())))
                            * Math.cos(deg2rad(theta));
                    distance = Math.acos(distance);
                    distance = rad2deg(distance);
                    distance = distance * 60 + 1.1515;

                    textView.setText(new StringBuilder().append("Cách khoảng ")
                            .append((int) distance)
                            .append(" met - ")
                            .append(post.getTouristDetailAddress()).toString());
                    Log.d("calculateDistance", "calculateDistance : " + distance);
                } catch (NumberFormatException e) {
                    Log.d("TAG", "NumberFormatException: " + e.getMessage());
                }

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void getCurrentUserFail(String isFail) {
                distance = 0f;
                textView.setText(distance + " " + post.getTouristDetailAddress());

            }
        });

    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void setImageByGlide(Post post, List<ImageView> listImageView) {
        Glide.with(context)
                .load(post.getList_photos().get(0).getUrl())
                .into(listImageView.get(0));
        Glide.with(context)
                .load(post.getList_photos().get(1).getUrl())
                .into(listImageView.get(1));
        Glide.with(context)
                .load(post.getList_photos().get(2).getUrl())
                .into(listImageView.get(2));
        Glide.with(context)
                .load(post.getList_photos().get(3).getUrl())
                .into(listImageView.get(3));
        Glide.with(context)
                .load(post.getList_photos().get(4).getUrl())
                .into(listImageView.get(4));
    }
}
