package com.example.travelapp.view.userfragment;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.travelapp.R;
import com.example.travelapp.base.BaseFragment;
import com.example.travelapp.controller.review_detail_post.DetailFMController;

import java.util.ArrayList;
import java.util.List;


public class DetailFragmentHomeUser extends BaseFragment {
    private ImageView imgTourist1InUserHome, imgTourist2InUserHome,imgTourist3InUserHome, imgTourist4InUserHome,
            imgTourist5InUserHome, imgIconFavorInUserHome, imgIconBackInUserHome;
    private TextView txtTouristDestinationNameInUserHome, txtTouristPlaceInUserHome,
            txtContentInUserHome , txtVoteInUserHome;
    private RatingBar ratingBarInUserHome;
    private DetailFMController detailFMController;
    private List<ImageView> listImageView = new ArrayList<>();
    private List<TextView> listTextView = new ArrayList<>();
    private static final String ARG_VALUE = "postID";
    // TODO: Rename and change types of parameters
    private String postID;


    public DetailFragmentHomeUser() {

    }

    public static DetailFragmentHomeUser newInstance(String param1) {
        DetailFragmentHomeUser fragment = new DetailFragmentHomeUser();
        Bundle args = new Bundle();
        args.putString(ARG_VALUE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_detail_home_user;
    }

    @Override
    public void initController() {
        detailFMController = new DetailFMController(requireContext());
    }

    @Override
    public void initView(View view) {
        onBackPress();
        initFindViewById(view);
    }

    private void initFindViewById(View view) {

        imgTourist1InUserHome = view.findViewById(R.id.imgTourist1InUserHome);
        imgTourist2InUserHome = view.findViewById(R.id.imgTourist2InUserHome);
        imgTourist3InUserHome = view.findViewById(R.id.imgTourist3InUserHome);
        imgTourist4InUserHome = view.findViewById(R.id.imgTourist4InUserHome);
        imgTourist5InUserHome = view.findViewById(R.id.imgTourist5InUserHome);
        txtTouristDestinationNameInUserHome = view.findViewById(R.id.txtTouristDestinationNameInUserHome);
        txtTouristPlaceInUserHome = view.findViewById(R.id. txtTouristPlaceInUserHome);
        imgIconBackInUserHome = view.findViewById(R.id.imgIconBackInUserHome);
        imgIconFavorInUserHome = view.findViewById(R.id.imgIconFavorInUserHome);
        txtContentInUserHome = view.findViewById(R.id.txtContentInUserHome);
        ratingBarInUserHome = view.findViewById(R.id.ratingBarInUserHome);
        txtVoteInUserHome = view.findViewById(R.id.txtVoteStarInUserHome);
    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            postID = getArguments().getString(ARG_VALUE);

        }
        setListImageView();
        setListTextView();
    }

    private void setListTextView() {
        listImageView.add(imgTourist1InUserHome);
        listImageView.add(imgTourist2InUserHome);
        listImageView.add(imgTourist3InUserHome);
        listImageView.add(imgTourist4InUserHome);
        listImageView.add(imgTourist5InUserHome);
        listImageView.add(imgIconFavorInUserHome);

    }

    private void setListImageView() {
        listTextView.add(txtTouristDestinationNameInUserHome);
        listTextView.add(txtTouristPlaceInUserHome);
        listTextView.add(txtContentInUserHome);
    }

    @Override
    public void initEvent() {


    }
    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.root_home_frame, new HomeFragmentUser());
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);
                trans.commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }
}