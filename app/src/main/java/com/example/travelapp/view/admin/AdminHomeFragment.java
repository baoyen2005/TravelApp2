package com.example.travelapp.view.admin;

import static com.example.travelapp.constant.AddPostByAdminConstant.PICK_FROM_GALLERY;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.travelapp.R;
import com.example.travelapp.base.BaseFragment;
import com.example.travelapp.controller.admin.AddNewHomeController;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class AdminHomeFragment extends BaseFragment implements AdminHomeFragmentViewInterface {
    private ArrayList<String> listFilePath = new ArrayList<>() ;
    private ArrayList<Uri> listUri = new ArrayList<>();
    private ImageView img_tourist_1, img_tourist_2,img_tourist_3, img_tourist_4, img_tourist_5, iconCamera;
    private EditText edt_touristDestinationName_addNewPost, edtTouristPlaceNewPost,
            edtLatitude_AddNewPost, edtLongitude_AddNewPost, edtContent_newAdd,edtType;
    private Button btnSaveNewPost;
    private AddNewHomeController addNewHomeController;
    private     ProgressDialog loadingBar;

    @Override
    public int getLayoutResId() {
         return R.layout.activity_add_post_by_admin;
    }

    @Override
    public void initController() {
        addNewHomeController = new AddNewHomeController(this);

    }

    @Override
    public void initView(View view) {
        iconCamera = view.findViewById(R.id.iconCamera);
        img_tourist_1 = view.findViewById(R.id.img_tourist_1);
        img_tourist_2 = view.findViewById(R.id.img_tourist_2);
        img_tourist_3 = view.findViewById(R.id.img_tourist_3);
        img_tourist_4 = view.findViewById(R.id.img_tourist_4);
        img_tourist_5 = view.findViewById(R.id.img_tourist_5);
        edt_touristDestinationName_addNewPost = view.findViewById(R.id.edt_touristDestinationName_addNewPost);
        edtTouristPlaceNewPost = view.findViewById(R.id. edtTouristPlaceNewPost);
        edtLatitude_AddNewPost = view.findViewById(R.id.edtLatitude_AddNewPost);
        edtLongitude_AddNewPost = view.findViewById(R.id.edtLongitude_AddNewPost);
        edtContent_newAdd = view.findViewById(R.id.edtContent_newAdd);
        edtType = view.findViewById(R.id.edtType);
        btnSaveNewPost = view.findViewById(R.id.btnSaveNewPost);
    }

    @Override
    public void initData() {
        loadingBar = new ProgressDialog(requireContext());
    }

    @Override
    public void initEvent() {
        setImageView();
        saveInfor();
    }

    private void saveInfor() {

        btnSaveNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Log.d("listpath", "saveInfor: listUri "+ listUri.get(0).getPath());

                addNewHomeController.addPostToFirebase(listUri,
                        edt_touristDestinationName_addNewPost.getText().toString(),
                        edtTouristPlaceNewPost.getText().toString(),
                        edtLatitude_AddNewPost.getText().toString(),
                        edtLongitude_AddNewPost.getText().toString(),
                        edtContent_newAdd.getText().toString(),edtType.getText().toString() , loadingBar);
            }
        });
    }

    private void setImageView() {
        iconCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFolder();


            }
        });
    }

    public void openFolder() {

        Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FROM_GALLERY);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY && resultCode == Activity.RESULT_OK) {

            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();

                int CurrentImageSelect = 0;

                while (CurrentImageSelect < count) {
                    Uri imageuri = data.getClipData().getItemAt(CurrentImageSelect).getUri();
                    listUri.add(imageuri);
                    listFilePath.add(imageuri.getPath());
                    CurrentImageSelect = CurrentImageSelect + 1;
                }
            }
            Log.d("listpath", "onActivityResult: listUri "+ listUri.get(0).getPath());
            iconCamera.setVisibility(View.GONE);
            img_tourist_1.setImageURI(listUri.get(0));
            img_tourist_2.setImageURI(listUri.get(1));
            img_tourist_3.setImageURI(listUri.get(2));
            img_tourist_4.setImageURI(listUri.get(3));
            img_tourist_5.setImageURI(listUri.get(4));
        }


    }

    @Override
    public void addNewPostSuccess(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        resetData();
    }

    private void resetData() {
        edt_touristDestinationName_addNewPost.setText("");
        edtType.setText("");
        edtContent_newAdd.setText("");
        edtLatitude_AddNewPost.setText("");
        edtLongitude_AddNewPost.setText("");
        edtTouristPlaceNewPost.setText("");
        img_tourist_1.setImageURI(null);
        img_tourist_2.setImageURI(null);
        img_tourist_3.setImageURI(null);
        img_tourist_4.setImageURI(null);
        img_tourist_5.setImageURI(null);
        iconCamera.setVisibility(View.VISIBLE);

    }

    @Override
    public void addNewPostFailed(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

    }
}