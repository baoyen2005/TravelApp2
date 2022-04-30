package com.example.travelapp.controller.review_detail_post;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelapp.view.interfacefragment.InterfaceObserverListFavorPostID;

import java.util.List;

public interface InterfaceDetailFMController {
//    public  void onLoadInformationUser(Context context , FirebaseUser firebaseUser,
//                                       TextView tvUserNameHomeFm, ImageView avatarHomeFragment);
//    public List<Post> onLoadFullPostToRecommend();
//    public void setTextForTVGoodmorning(TextView tvGoodMorningHomeFm);
    public void setDataForDetailPostFM(List<ImageView> listImageView,
                                       List<TextView> listTextView,String postID);
    public void rateStarForApp(String ratingBar,String totalStar, String postID,String uid);

    public void pushDataToFirebase(String postId, InterfaceObserverListFavorPostID observerListFavorPostID,String uid);
}
