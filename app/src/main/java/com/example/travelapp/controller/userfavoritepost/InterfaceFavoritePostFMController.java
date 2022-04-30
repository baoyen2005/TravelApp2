package com.example.travelapp.controller.userfavoritepost;

import android.content.Context;

public interface InterfaceFavoritePostFMController {
    public void getAllFavoritePostFromFirebase(Context context,String uid, FavoritePostController.OnLoadDataListener listener);
}
