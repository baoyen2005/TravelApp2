package com.example.travelapp.model;

import java.util.List;

public class Post {
    private String postId;
    private String touristSpotName, address, content,type;
    private List<String> urlImgReview;
    private float latitude , longitude;

    public Post(String postId,String touristSpotName, String address, String content, List<String> urlImgReview,
                float latitude, float longitude,String type) {
        this.postId = postId;
        this.touristSpotName = touristSpotName;
        this.address = address;
        this.content = content;
        this.urlImgReview = urlImgReview;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTouristSpotName() {
        return touristSpotName;
    }

    public void setTouristSpotName(String touristSpotName) {
        this.touristSpotName = touristSpotName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getUrlImgReview() {
        return urlImgReview;
    }

    public void setUrlImgReview(List<String> urlImgReview) {
        this.urlImgReview = urlImgReview;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
