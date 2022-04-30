package com.example.travelapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Post implements Serializable {
    private String postId;
    private String touristName,
            touristDetailAddress,
            content,type;
    private List<String> urlImgReview;
    private List<Photo> list_photos = new ArrayList<>();
    private String latitude , longitude;

    public Post() {
    }

    public Post(String postId, String touristName, String touristDetailAddress, String content, List<String> urlImgReview,
                String latitude, String longitude, String type) {
        this.postId = postId;
        this.touristName = touristName;
        this.touristDetailAddress = touristDetailAddress;
        this.content = content;
        this.urlImgReview = urlImgReview;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

    public Post(String postId, String touristName, String touristDetailAddress, String content, String type, List<String> urlImgReview, List<Photo> list_photos, String latitude, String longitude) {
        this.postId = postId;
        this.touristName = touristName;
        this.touristDetailAddress = touristDetailAddress;
        this.content = content;
        this.type = type;
        this.urlImgReview = urlImgReview;
        this.list_photos = list_photos;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public List<Photo> getList_photos() {
        return list_photos;
    }

    public void setList_photos(List<Photo> list_photos) {
        this.list_photos = list_photos;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTouristName() {
        return touristName;
    }

    public void setTouristName(String touristName) {
        this.touristName = touristName;
    }

    public String getTouristDetailAddress() {
        return touristDetailAddress;
    }

    public void setTouristDetailAddress(String touristDetailAddress) {
        this.touristDetailAddress = touristDetailAddress;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



}
