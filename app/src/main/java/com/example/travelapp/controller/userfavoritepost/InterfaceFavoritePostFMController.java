package com.example.travelapp.controller.userfavoritepost;

import android.content.Context;

import com.example.travelapp.model.FavoritePost;
import com.example.travelapp.view.adapter.user.UserFavoriteListPostAdapter;

import java.util.List;

public interface InterfaceFavoritePostFMController {
    public void getAllFavoritePostFromFirebase(Context context,String uid, FavoritePostController.OnLoadDataListener listener);
    public void filter(String s, List<FavoritePost> favoritePostList, UserFavoriteListPostAdapter userFavoriteListPostAdapter);
}
