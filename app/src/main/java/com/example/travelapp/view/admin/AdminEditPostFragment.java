package com.example.travelapp.view.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.travelapp.R;
import com.example.travelapp.base.BaseFragment;
import com.example.travelapp.controller.admin.EditPostAdminController;
import com.example.travelapp.function_util.GetPostFromFirebaseStorage;
import com.example.travelapp.function_util.ReplaceFragment;
import com.example.travelapp.model.Post;
import com.example.travelapp.model.PositionImage;
import com.example.travelapp.view.interfacefragment.InterfaceEventGetPostByIDListener;

import java.util.ArrayList;
import java.util.List;

public class AdminEditPostFragment extends BaseFragment implements AdminEditPostInterface {
    private final List<PositionImage> listUriPositionImage = new ArrayList<>();
    private ImageView imgTourist1InEditPost, imgTourist2EditPost, imgTourist3InEditPost,
            imgTourist4InEditPost, imgTourist5InEditPost;
    private EditText edtTouristDestinationNameEditPost, edtTouristPlaceEditPost,
            edtLatitudeEditPost, edtLongitudeEditPost, edtTypeInEditPost, edtContentEditPost;
    private final List<ImageView> listImageView = new ArrayList<>();
    private final List<EditText> editTextList = new ArrayList<>();
    private Button btnSaveEditPost;
    private EditPostAdminController editPostAdminController;
    private ProgressDialog loadingBar;
    private final int GALLERY_REQUEST = 100;

    private static final String ARG_POSTID = "postid";

    private String postID;
    private int positionPicker = -1;

    public AdminEditPostFragment() {
        // Required empty public constructor
    }

    public static AdminEditPostFragment newInstance(String param1) {
        AdminEditPostFragment fragment = new AdminEditPostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_POSTID, param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_edit_post_by_admin;
    }

    @Override
    public void initController() {
        editPostAdminController = new EditPostAdminController(requireActivity(), this);

    }

    @Override
    public void initView(View view) {
        imgTourist1InEditPost = view.findViewById(R.id.imgTourist1InEditPost);
        imgTourist2EditPost = view.findViewById(R.id.imgTourist2EditPost);
        imgTourist3InEditPost = view.findViewById(R.id.imgTourist3InEditPost);
        imgTourist4InEditPost = view.findViewById(R.id.imgTourist4InEditPost);
        imgTourist5InEditPost = view.findViewById(R.id.imgTourist5InEditPost);
        edtTouristDestinationNameEditPost = view.findViewById(R.id.edtTouristDestinationNameEditPost);
        edtTouristPlaceEditPost = view.findViewById(R.id.edtTouristPlaceEditPost);
        edtLatitudeEditPost = view.findViewById(R.id.edtLatitudeEditPost);
        edtLongitudeEditPost = view.findViewById(R.id.edtLongitudeEditPost);
        edtContentEditPost = view.findViewById(R.id.edtContentEditPost);
        edtTypeInEditPost = view.findViewById(R.id.edtTypeInEditPost);
        btnSaveEditPost = view.findViewById(R.id.btnSaveEditPost);

    }

    @Override
    public void initData() {

        if (getArguments() != null) {
            postID = getArguments().getString(ARG_POSTID);
        }
        loadingBar = new ProgressDialog(requireContext());
        initListEditText();
        initListImageView();
        initAllDataPost();
    }


    private void initListImageView() {
        listImageView.add(imgTourist1InEditPost);
        listImageView.add(imgTourist2EditPost);
        listImageView.add(imgTourist3InEditPost);
        listImageView.add(imgTourist4InEditPost);
        listImageView.add(imgTourist5InEditPost);
    }

    private void initListEditText() {
        editTextList.add(edtTouristDestinationNameEditPost);
        editTextList.add(edtTouristPlaceEditPost);
        editTextList.add(edtLatitudeEditPost);
        editTextList.add(edtLongitudeEditPost);
        editTextList.add(edtTypeInEditPost);
        editTextList.add(edtContentEditPost);
    }

    private Post postTmp = null;

    private void initAllDataPost() {
        GetPostFromFirebaseStorage getPostFromFirebaseStorage = new GetPostFromFirebaseStorage();
        getPostFromFirebaseStorage.getPostFromFirebaseByID(postID, new InterfaceEventGetPostByIDListener() {
            @Override
            public void getPostByIDSuccess(Post post) {
                postTmp = post;
                editPostAdminController.setDetailDataForView(post, listImageView, editTextList);
            }

            @Override
            public void getPostsFail(String isFail) {

            }
        });
    }

    @Override
    public void initEvent() {
        setImageView();
        saveInfo();
    }

    private void saveInfo() {
        GetPostFromFirebaseStorage getPostFromFirebaseStorage = new GetPostFromFirebaseStorage();

        btnSaveEditPost.setOnClickListener(view -> {
            getPostFromFirebaseStorage.getPostFromFirebaseByID(postID,
                    new InterfaceEventGetPostByIDListener() {
                        @Override
                        public void getPostByIDSuccess(Post post) {
                            editPostAdminController.editPostInFirebase(
                                    post,
                                    listUriPositionImage,
                                    edtTouristDestinationNameEditPost.getText().toString(),
                                    edtTouristPlaceEditPost.getText().toString(),
                                    edtLatitudeEditPost.getText().toString(),
                                    edtLongitudeEditPost.getText().toString(),
                                    edtContentEditPost.getText().toString(),
                                    edtTypeInEditPost.getText().toString(),
                                    loadingBar);
                        }

                        @Override
                        public void getPostsFail(String isFail) {

                        }
                    });
        });
    }

    private void setImageView() {
        for (int i = 0; i < listImageView.size(); i++) {
            int pos = i;
            listImageView.get(i).setOnClickListener(view -> {
                positionPicker = pos;
                chooseImageFromLocal();
            });
        }
    }


    private void chooseImageFromLocal() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        imagePickerActivityResult.launch(galleryIntent);
    }

    ActivityResultLauncher<Intent> imagePickerActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result != null) {
                    Uri imageUri = result.getData().getData();
                    Log.d("___Yenlb", "imagePickerActivityResult: imageUri " + imageUri);
                    if (imageUri == null) {
                        Toast.makeText(requireContext(), "Please choose image", Toast.LENGTH_SHORT).show();
                    } else {
                        listImageView.get(positionPicker).setImageURI(imageUri);

                        if (listUriPositionImage.size() > 0) {
                            for (int i = 0; i < listUriPositionImage.size(); i++) {
                                if (listUriPositionImage.get(i).getPosition() == positionPicker) {
                                    listUriPositionImage.get(i).setUri(imageUri);
                                    listUriPositionImage.set(i, listUriPositionImage.get(i));
                                    return;
                                } else {
                                    if (i >= listUriPositionImage.size()) {
                                        listUriPositionImage.add(new PositionImage(imageUri, positionPicker));
                                    }
                                }
                            }
                        } else {
                            listUriPositionImage.add(new PositionImage(imageUri, positionPicker));
                        }
                    }
                }
            }
    );

    @Override
    public void editPostSuccess(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        backAdminHome();
    }



    @Override
    public void editPostFailed(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        backAdminHome();
    }

    private void backAdminHome() {
        ReplaceFragment replaceFragmentInit = new ReplaceFragment();
        replaceFragmentInit.replaceFragment(R.id.fragmentRootAdminHome, new AdminHomeFragment(),requireActivity());
    }
}