package com.example.travelapp.controller.admin;

import android.app.ProgressDialog;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.travelapp.model.Post;
import com.example.travelapp.model.PositionImage;

import java.util.List;

public interface EditPostAdminControllerInterface {
    public void editPostInFirebase(Post postID, List<PositionImage> listFilePath, String edtTouristName, String edtTouristPlace,
                                   String edtLatitude, String edtLongitude, String content, String type,
                                   ProgressDialog loadingBar);

    void setDetailDataForView(Post post, List<ImageView>imageViewList, List<EditText> editTextList);
}
