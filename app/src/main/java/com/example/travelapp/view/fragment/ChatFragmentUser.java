package com.example.travelapp.view.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.travelapp.R;
import com.example.travelapp.base.BaseFragment;
import com.example.travelapp.view.activity.chat.ChatActivity;

public class ChatFragmentUser extends BaseFragment {

    private LinearLayout item_chat;
    private TextView tvLastMsg, tvLastMsgTime;

    public ChatFragmentUser() {
    }



    @Override
    public int getLayoutResId() {
        return R.layout.fragment_chat_admin;
    }

    @Override
    public void initController() {

    }

    @Override
    public void initView(View view) {
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