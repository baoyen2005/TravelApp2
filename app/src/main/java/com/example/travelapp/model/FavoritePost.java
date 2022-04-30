package com.example.travelapp.model;

import java.util.List;

public class FavoritePost extends Post{
    private String uid;


    public FavoritePost() {
    }

    public FavoritePost(String uid, List<String> listPostID) {
        this.uid = uid;

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
