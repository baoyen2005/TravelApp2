package com.example.travelapp.model;

public class Star {
    private String uid;
    private String postID;
    private String ratingStar;

    public Star() {
    }

    public Star(String uid, String postID, String ratingStar) {
        this.uid = uid;
        this.postID = postID;
        this.ratingStar = ratingStar;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(String ratingStar) {
        this.ratingStar = ratingStar;
    }
}
