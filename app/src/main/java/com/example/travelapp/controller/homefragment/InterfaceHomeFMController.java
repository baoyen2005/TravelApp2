package com.example.travelapp.controller.homefragment;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelapp.model.Post;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public interface InterfaceHomeFMController {
    public  void onLoadInformationUser(Context context , FirebaseUser firebaseUser,
                                       TextView tvUserNameHomeFm, ImageView avatarHomeFragment);
    public List<Post> onLoadFullPostToRecommend();
    public void setTextForTVGoodmorning(TextView tvGoodMorningHomeFm);
}
