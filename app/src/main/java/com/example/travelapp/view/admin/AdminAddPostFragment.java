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
import com.example.travelapp.model.PositionImage;

import java.util.ArrayList;
import java.util.List;

public class AdminAddPostFragment extends BaseFragment implements AdminHomeFragmentViewInterface {
    private final ArrayList<String> listFilePath = new ArrayList<>();
    private final List<PositionImage> listUri = new ArrayList<>();
    private ImageView img_tourist_1, img_tourist_2, img_tourist_3, img_tourist_4, img_tourist_5, iconCamera;
    private EditText edt_touristDestinationName_addNewPost, edtTouristPlaceNewPost,
            edtLatitude_AddNewPost, edtLongitude_AddNewPost, edtContent_newAdd, edtType;
    private final List<ImageView> listImageView = new ArrayList<>();
    private Button btnSaveNewPost;
    private AddNewHomeController addNewHomeController;
    private ProgressDialog loadingBar;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_add_post_by_admin;
    }

    @Override
    public void initController() {
        addNewHomeController = new AddNewHomeController(this);

    }

    @Override
    public void initView(View view) {
        iconCamera = view.findViewById(R.id.iconCamera);
        img_tourist_1 = view.findViewById(R.id.imgTourist1InEditPost);
        img_tourist_2 = view.findViewById(R.id.img_tourist_2);
        img_tourist_3 = view.findViewById(R.id.img_tourist_3);
        img_tourist_4 = view.findViewById(R.id.img_tourist_4);
        img_tourist_5 = view.findViewById(R.id.img_tourist_5);
        edt_touristDestinationName_addNewPost = view.findViewById(R.id.edt_touristDestinationName_addNewPost);
        edtTouristPlaceNewPost = view.findViewById(R.id.edtTouristPlaceNewPost);
        edtLatitude_AddNewPost = view.findViewById(R.id.edtLatitude_AddNewPost);
        edtLongitude_AddNewPost = view.findViewById(R.id.edtLongitude_AddNewPost);
        edtContent_newAdd = view.findViewById(R.id.edtContent_newAdd);
        edtType = view.findViewById(R.id.edtType);
        btnSaveNewPost = view.findViewById(R.id.btnSaveNewPost);
    }

    @Override
    public void initData() {
        loadingBar = new ProgressDialog(requireContext());
        listImageView.add(img_tourist_1);
        listImageView.add(img_tourist_2);
        listImageView.add(img_tourist_3);
        listImageView.add(img_tourist_4);
        listImageView.add(img_tourist_5);
    }

    @Override
    public void initEvent() {
        setImageView();
        saveInfo();
    }

    private void saveInfo() {

        btnSaveNewPost.setOnClickListener(view -> {
            //  Log.d("listpath", "saveInfor: listUri "+ listUri.get(0).getPath());

            addNewHomeController.addPostToFirebase(listUri,
                    edt_touristDestinationName_addNewPost.getText().toString(),
                    edtTouristPlaceNewPost.getText().toString(),
                    edtLatitude_AddNewPost.getText().toString(),
                    edtLongitude_AddNewPost.getText().toString(),
                    edtContent_newAdd.getText().toString(), edtType.getText().toString(), loadingBar);
            Toast.makeText(requireContext(), "Add post successfully", Toast.LENGTH_SHORT).show();
        });
    }

    private void setImageView() {
        iconCamera.setOnClickListener(view -> openFolder());
    }

    public void openFolder() {

        Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FROM_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode!=PICK_FROM_GALLERY || requestCode !=Activity.RESULT_OK||data==null){
            Log.d("__add", "onActivityResult: ");
            Toast.makeText(requireContext(), "Please choose 5 images again", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == PICK_FROM_GALLERY && resultCode == Activity.RESULT_OK) {

            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();

                int CurrentImageSelect = 0;

                while (CurrentImageSelect < count) {
                    Uri imageuri = data.getClipData().getItemAt(CurrentImageSelect).getUri();
                    listUri.add(new PositionImage(imageuri, CurrentImageSelect));
                    listFilePath.add(imageuri.getPath());
                    CurrentImageSelect = CurrentImageSelect + 1;
                }
            }
            iconCamera.setVisibility(View.GONE);

            for (int i = 0; i < 5; i++) {
                if (listUri.get(i) == null) {
                    listImageView.get(i).setImageResource(R.drawable.ic_baseline_warning_24);
                } else {
                    listImageView.get(i).setImageURI(listUri.get(i).getUri());
                }
            }

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