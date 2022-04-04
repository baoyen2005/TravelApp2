package com.example.travelapp.controller.homefragment;

import static com.example.travelapp.constant.UserHomeConstant.TAG_USER_HOME;
import static com.example.travelapp.constant.UserHomeConstant.TAG_USER_HOME_POST;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.travelapp.R;
import com.example.travelapp.function_util.GetAllPostFromFirebaseStorage;
import com.example.travelapp.function_util.GetUserFromFireStorage;
import com.example.travelapp.model.Post;
import com.example.travelapp.model.User;
import com.example.travelapp.view.interfacefragment.InterfaceEventGetCurrentUserListener;
import com.example.travelapp.view.interfacefragment.InterfaceEventGetPostListener;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFMController implements InterfaceHomeFMController{
    private GetUserFromFireStorage getUserFromFireStorage;

    @Override
    public void onLoadInformationUser(Context context , FirebaseUser firebaseUser,
                                      TextView tvUserNameHomeFm, ImageView avatarHomeFragment) {


        getUserFromFireStorage = new GetUserFromFireStorage();
        getUserFromFireStorage.getUserFromFirebase(firebaseUser.getUid(), new InterfaceEventGetCurrentUserListener() {
            @Override
            public void getCurrentUserSuccess(User user) {
                if(user!=null){
                    User currentUser = setCurrentUser(user);
                    tvUserNameHomeFm.setText(currentUser.getUsername());
                    if(currentUser.getImageURL()==null){
                        avatarHomeFragment.setImageResource(R.drawable.useravatar);
                    }
                    else{
                        Glide.with(context)
                                .load(Uri.parse(currentUser.getImageURL()))
                                .into(avatarHomeFragment);
                        Log.d(TAG_USER_HOME, "onSuccess: current user " + currentUser.getImageURL());

                    }

                }
                else{
                    Toast.makeText(context, "current user is null", Toast.LENGTH_SHORT).show();
                    Log.d(TAG_USER_HOME, "on null: current user null");
                }
            }

            @Override
            public void getCurrentUserFail(String isFail) {
                Toast.makeText(context, "get currentUser is failed", Toast.LENGTH_SHORT).show();
                Log.d(TAG_USER_HOME, "get currentUser is failed");
            }
        });
    }

    @Override
    public List<Post> onLoadFullPostToRecommend() {
//        List<Post> postList = new ArrayList<>();
//        getAllPostFromFirebaseStorage = new GetAllPostFromFirebaseStorage();
//        getAllPostFromFirebaseStorage.getAllPostFromFirebase(new InterfaceEventGetPostListener() {
//            @Override
//            public void getPostsSuccess(List<Post> postList) {
//                postList.addAll(postList);
//                Log.d(TAG_USER_HOME_POST, "getPostsSuccess: homefmcontroller"+postList.get(0).getTouristName());
//            }
//
//            @Override
//            public void getPostsFail(String isFail) {
//                Log.d(TAG_USER_HOME_POST, "getPostsFail: homefmcontroller");
//            }
//        });
//        return postList;
        return null;
    }

    private User setCurrentUser(User user ) {
        User currentUser = new User();
        currentUser.setUid(user.getUid());
        currentUser.setUsername(user.getUsername());
        currentUser.setImageURL(user.getImageURL());
        currentUser.setEmail(user.getEmail());
        currentUser.setAddress(user.getAddress());
        currentUser.setPassword(user.getPassword());
        currentUser.setPhone(user.getPhone());
        return currentUser;
    }

    @Override
    public void setTextForTVGoodmorning(TextView tvGoodMorningHomeFm) {
        Date currentTime = Calendar.getInstance().getTime();
        if (6 <= currentTime.getHours() && currentTime.getHours() < 12) {
            tvGoodMorningHomeFm.setText("Good morning");
        } else if (12 <= currentTime.getHours() && currentTime.getHours() < 18) {
            tvGoodMorningHomeFm.setText("Good afternoon");
        } else if (18 <= currentTime.getHours() && currentTime.getHours() < 24) {
            tvGoodMorningHomeFm.setText("Good evening");
        } else {
            tvGoodMorningHomeFm.setText("Good night");
        }
    }
}
