package com.example.travelapp.view.userfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.travelapp.R;
import com.example.travelapp.base.BaseFragment;
import com.example.travelapp.controller.review_detail_post.DetailFMController;
import com.example.travelapp.function_util.GetRateStarFromFirebase;
import com.example.travelapp.function_util.ReplaceFragment;
import com.example.travelapp.view.interfacefragment.InterfaceDeletePostListener;
import com.example.travelapp.view.interfacefragment.InterfaceEventCheckedRateStarListener;
import com.example.travelapp.view.interfacefragment.InterfaceObserverListFavorPostID;
import com.example.travelapp.view.interfacefragment.InterfaceRatingStarListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class DetailFragmentHomeUser extends BaseFragment implements InterfaceRatingStarListener {
    private ImageView imgTourist1InUserHome, imgTourist2InUserHome, imgTourist3InUserHome, imgTourist4InUserHome,
            imgTourist5InUserHome;
    private LinearLayout imgIconBackInUserHome,imgIconFavorInUserHome, imgIconUnlikeInUserHome;
    private TextView txtTouristDestinationNameInUserHome, txtTouristPlaceInUserHome,
            txtContentInUserHome, txtVoteInUserHome;
    private InterfaceObserverListFavorPostID observerListFavorPostIDInterface;
    private RatingBar ratingBarInUserHome;
    private DetailFMController detailFMController;
    private ConstraintLayout constraintRatingbarDetailUser;
    private final List<ImageView> listImageView = new ArrayList<>();
    private final List<TextView> listTextView = new ArrayList<>();
    private static final String ARG_VALUE = "postID";
    private static final String FRAGMENT_TAG = "fragment_tag";
    private String postID, fragmentTag;
    String rateStar, totalStar;
    private GetRateStarFromFirebase getRateStarFromFirebase;
    private FirebaseAuth firebaseAuth;
    private final String homeFragmentTag = "HomeFragment";
    private final  String favoriteFragmentTag = "favoriteFragment";

    public DetailFragmentHomeUser() {

    }

    @NonNull
    public static DetailFragmentHomeUser newInstance(String param1, String param2) {
        DetailFragmentHomeUser fragment = new DetailFragmentHomeUser();
        Bundle args = new Bundle();
        args.putString(ARG_VALUE, param1);
        args.putString(FRAGMENT_TAG, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_detail_home_user;
    }

    @Override
    public void initController() {
        detailFMController = new DetailFMController(requireContext(), this);
    }

    @Override
    public void initView(View view) {
        if (getArguments() != null) {
            postID = getArguments().getString(ARG_VALUE);
            fragmentTag = getArguments().getString(FRAGMENT_TAG);
            Log.d("__postID", "initData in DetailFM: fragmentTag = " + fragmentTag);

        }
        initFindViewById(view);

    }

    private void initFindViewById(View view) {

        firebaseAuth = FirebaseAuth.getInstance();

        imgTourist1InUserHome = view.findViewById(R.id.imgTourist1InUserHome);
        imgTourist2InUserHome = view.findViewById(R.id.imgTourist2InUserHome);
        imgTourist3InUserHome = view.findViewById(R.id.imgTourist3InUserHome);
        imgTourist4InUserHome = view.findViewById(R.id.imgTourist4InUserHome);
        imgTourist5InUserHome = view.findViewById(R.id.imgTourist5InUserHome);
        txtTouristDestinationNameInUserHome = view.findViewById(R.id.txtTouristDestinationNameInUserHome);
        txtTouristPlaceInUserHome = view.findViewById(R.id.txtTouristPlaceInUserHome);
        imgIconBackInUserHome = view.findViewById(R.id.imgIconBackInUserHome);
        imgIconFavorInUserHome = view.findViewById(R.id.imgIconFavorInUserHome);
        txtContentInUserHome = view.findViewById(R.id.txtContentInUserHome);
        ratingBarInUserHome = view.findViewById(R.id.ratingBarInUserHome);
        txtVoteInUserHome = view.findViewById(R.id.txtVoteStarInUserHome);
        imgIconUnlikeInUserHome  = view.findViewById(R.id.imgIconUnlikeInUserHome);
        constraintRatingbarDetailUser = view.findViewById(R.id.constraintRatingbarDetailUser);



    }

    @Override
    public void initData() {

        checkFavoritePost();
        setListImageView();
        setListTextView();


    }
    private void checkFavoritePost() {
        Log.d("__postID", "checkFavoritePost in DetailFM: postID = " + postID);

        detailFMController.setIconForFavorite(postID,imgIconFavorInUserHome,imgIconUnlikeInUserHome);
    }

    private void setListTextView() {
        listImageView.add(imgTourist1InUserHome);
        listImageView.add(imgTourist2InUserHome);
        listImageView.add(imgTourist3InUserHome);
        listImageView.add(imgTourist4InUserHome);
        listImageView.add(imgTourist5InUserHome);
    }

    private void setListImageView() {
        listTextView.add(txtTouristDestinationNameInUserHome);
        listTextView.add(txtTouristPlaceInUserHome);
        listTextView.add(txtContentInUserHome);
    }

    @Override
    public void initEvent() {
        detailFMController.setDataForDetailPostFM(listImageView, listTextView, postID);
        clickIconBack();
        setVisible();
        clickIconFavorite();
        removeFavoritePost();
        constraintRatingbarDetailUser.setVisibility(View.VISIBLE);
        ratingBarInUserHome.setOnRatingBarChangeListener((ratingBar, v, b) -> rateStar = v + "");
        txtVoteInUserHome.setOnClickListener(view -> ratingStar());

        getRateStarFromFirebase = new GetRateStarFromFirebase();
    }

    private void removeFavoritePost() {
       imgIconUnlikeInUserHome.setOnClickListener(view ->
       {
           detailFMController.removeFavoritePost(postID, new InterfaceDeletePostListener() {
               @Override
               public void deletePostSuccessfully() {
                   if(fragmentTag.equals("favoriteFragment")){
                       ReplaceFragment replaceFragment = new ReplaceFragment();
                       replaceFragment.replaceFragment(R.id.root_favorite_frame,new FavoriteFragmentUser(),requireActivity());
                   }
                   else {
                       imgIconBackInUserHome.setVisibility(View.VISIBLE);
                       imgIconFavorInUserHome.setVisibility(View.VISIBLE);
                       imgIconUnlikeInUserHome.setVisibility(View.GONE);
                   }
               }

               @Override
               public void deletePostFail(String mes) {

               }
           });
       });

    }

    private void clickIconFavorite() {
        imgIconFavorInUserHome.setOnClickListener(view -> {
            detailFMController.pushDataToFirebase(postID, observerListFavorPostIDInterface,
                    Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());
            setVisible();
            imgIconUnlikeInUserHome.setVisibility(View.VISIBLE);
            imgIconFavorInUserHome.setVisibility(View.GONE);
        });
    }

    private void clickIconBack() {
        imgIconBackInUserHome.setOnClickListener(view -> {
            Log.d("__postid", "imgIconBackInUserHome.setOnClickListener(: ");

            onBackPress();
        });
    }

    private void setVisible() {
        if(fragmentTag.equals(favoriteFragmentTag)){
            imgIconBackInUserHome.setVisibility(View.GONE);
        }
        else{
            imgIconBackInUserHome.setVisibility(View.VISIBLE);

        }
    }


    private void onBackPress() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(fragmentTag.equals(homeFragmentTag)){
            transaction.replace(R.id.root_home_frame, new HomeFragmentUser());
        }
        transaction.commit();
    }

    private void ratingStar() {
        totalStar = "Total Stars:: " + ratingBarInUserHome.getNumStars();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        getRateStarFromFirebase.isCheckedUserRateApp(firebaseUser.getUid(), new InterfaceEventCheckedRateStarListener() {
            @Override
            public void isExist(String ratingStar) {
                ratingBarInUserHome.setRating(Float.parseFloat(ratingStar));
                detailFMController.rateStarForApp(rateStar, totalStar, postID, firebaseUser.getUid());
                constraintRatingbarDetailUser.setVisibility(View.GONE);
            }

            @Override
            public void notExist() {
                ratingBarInUserHome.setRating(0);
                detailFMController.rateStarForApp(rateStar, totalStar, postID, firebaseUser.getUid());
                constraintRatingbarDetailUser.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void ratingStarSuccess(String mes) {
        Toast.makeText(requireContext(), mes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ratingStarFail(String mes) {
        Toast.makeText(requireContext(), "rating app fail.please try again", Toast.LENGTH_SHORT).show();
        constraintRatingbarDetailUser.setVisibility(View.VISIBLE);
        ratingStar();
    }
}