package com.example.travelapp.controller.review_detail_post;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.travelapp.view.interfacefragment.InterfaceDeletePostListener;
import com.example.travelapp.view.interfacefragment.InterfaceObserverListFavorPostID;

import java.util.List;

public interface InterfaceDetailFMController {
    void setIconForFavorite(String postId, LinearLayout imgIconFavorite, LinearLayout imgUnlike);
    public void setDataForDetailPostFM(List<ImageView> listImageView,
                                       List<TextView> listTextView,String postID);
    public void rateStarForApp(String ratingBar,String totalStar, String postID,String uid);

    public void pushDataToFirebase(String postId, InterfaceObserverListFavorPostID observerListFavorPostID,String uid);

    void removeFavoritePost(String postId, InterfaceDeletePostListener deletePostListener);

}
