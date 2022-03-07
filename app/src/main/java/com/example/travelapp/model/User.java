package com.example.travelapp.model;

import android.net.Uri;

import com.google.firebase.database.Exclude;

public class User {
    private String filePath;
    private String username, phone, address, password;
    private String key;
    private String uid;

    public User() {
    }

    public User(String filePath, String username, String phone, String address, String password, String key, String uid) {
        this.filePath = filePath;
        this.username = username;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.key = key;
        this.uid = uid;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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
}

