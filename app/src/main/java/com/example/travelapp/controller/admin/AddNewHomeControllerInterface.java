package com.example.travelapp.controller.admin;

import android.app.ProgressDialog;

import com.example.travelapp.model.PositionImage;

import java.util.List;

public interface AddNewHomeControllerInterface {
    public void addPostToFirebase(List<PositionImage> listFilePath, String edtTouristName, String edtTouristPlace,
                                  String edtLatitude, String edtLongitude, String content, String type, ProgressDialog loadingBar);
}
