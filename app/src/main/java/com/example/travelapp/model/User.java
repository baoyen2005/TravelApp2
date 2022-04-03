package com.example.travelapp.model;

import com.google.firebase.database.Exclude;

public class User {
    private String imageURL;
    private String username, phone, address, password;
    private String key;
    private String uid, email;

    public User() {
    }

    public User(String imageURL, String username, String phone, String address, String password,
                String key, String uid, String email) {
        this.imageURL = imageURL;
        this.username = username;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.key = key;
        this.uid = uid;
        this.email = email;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKey() {
        return key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

