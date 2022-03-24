package com.example.travelapp.view.fragment;

import android.content.Intent;
import android.view.View;

import com.example.travelapp.R;
import com.example.travelapp.base.BaseFragment;
import com.example.travelapp.view.activity.chat.ChatActivity;


public class FavoriteFragmentUser extends BaseFragment {

    public FavoriteFragmentUser() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_favorite_user;
    }

    @Override
    public void initController() {

    }
    
    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
//        item_chat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), ChatActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}