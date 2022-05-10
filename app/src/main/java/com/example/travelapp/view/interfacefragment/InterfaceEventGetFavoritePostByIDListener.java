package com.example.travelapp.view.interfacefragment;

import com.example.travelapp.model.FavoritePost;

public interface InterfaceEventGetFavoritePostByIDListener {

    public void getFavoritePostByIDSuccess(FavoritePost favoritePost);

    public void getFavoritePostsFail(String isFail);

//


}
