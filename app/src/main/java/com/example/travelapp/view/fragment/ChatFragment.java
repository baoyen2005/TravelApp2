package com.example.travelapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelapp.R;
import com.example.travelapp.base.BaseFragment;
import com.example.travelapp.view.activity.chat.ChatActivity;

public class ChatFragment extends BaseFragment {

    private LinearLayout item_chat;
    private TextView tvLastMsg, tvLastMsgTime;

    public ChatFragment() {
        // Required empty public constructor
    }



    @Override
    public int getLayoutResId() {
        return R.layout.fragment_chat;
    }

    @Override
    public void initController() {

    }

    @Override
    public void initView() {
        item_chat = getView().findViewById(R.id.item_admin_chat);
        tvLastMsg = getView().findViewById(R.id.tvLastMsg);
        tvLastMsgTime = getView().findViewById(R.id.tvLastMsgTime);

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        item_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                startActivity(intent);
            }
        });
    }
}