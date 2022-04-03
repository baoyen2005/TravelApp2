package com.example.travelapp.model;

import java.io.Serializable;
import java.util.List;

public class Post implements Serializable {
    private String postId;
    private String touristName,
            touristDetailAddress,
            content,type;
    private List<String> urlImgReview;
    private String image0, image1, image2,image3, imag4;
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

    public Post(String postId, String touristName, String touristDetailAddress, String content, String type, String image0, String image1, String image2, String image3, String imag4, String latitude, String longitude) {
        this.postId = postId;
        this.touristName = touristName;
        this.touristDetailAddress = touristDetailAddress;
        this.content = content;
        this.type = type;
        this.image0 = image0;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.imag4 = imag4;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getImage0() {
        return image0;
    }

    public void setImage0(String image0) {
        this.image0 = image0;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImag4() {
        return imag4;
    }

    public void setImag4(String imag4) {
        this.imag4 = imag4;
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
