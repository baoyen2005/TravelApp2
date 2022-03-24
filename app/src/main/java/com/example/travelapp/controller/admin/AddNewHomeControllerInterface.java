package com.example.travelapp.controller.admin;

import android.app.ProgressDialog;
import android.net.Uri;

import java.util.List;

public interface AddNewHomeControllerInterface {
    public void addPostToFirebase(List<Uri> listFilePath, String edtTouristName, String edtTouristPlace,
                                  String edtLatitude, String edtLongitude, String content,String type, ProgressDialog loadingBar);
}
