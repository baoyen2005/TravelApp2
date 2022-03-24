package com.example.travelapp.view.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.travelapp.R;
import com.example.travelapp.base.BaseFragment;
import com.example.travelapp.constant.Data;
import com.example.travelapp.view.activity.chat.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatFragmentUser extends BaseFragment {

    private LinearLayout item_chat;
    private TextView tvLastMsg, tvLastMsgTime;
    private ImageView imgAdmin;

    public ChatFragmentUser() {

    }



    @Override
    public int getLayoutResId() {
        return R.layout.fragment_chat_user;
    }

    @Override
    public void initController() {

    }

    @Override
    public void initView(View view) {
        item_chat = getView().findViewById(R.id.item_admin_chat);
        tvLastMsg = getView().findViewById(R.id.tvLastMsg);
        tvLastMsgTime = getView().findViewById(R.id.tvLastMsgTime);
        imgAdmin = getView().findViewById(R.id.imgAdmin);
    }

    @Override
    public void initData() {
        String currentId = FirebaseAuth.getInstance().getUid();
        String senderRoom = currentId + Data.adminUid;
        FirebaseDatabase.getInstance()
                .getReference().child("Chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String lastMsg = snapshot.child("lastMsg").getValue(String.class);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                            Long time = snapshot.child("lastMsgTime").getValue(Long.class);
                            if (time != null) {
                                tvLastMsgTime.setText(dateFormat.format(new Date(time)));
                            }
                            tvLastMsg.setText(lastMsg);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//        Glide.with(this).load("https://cdn.icon-icons.com/icons2/534/PNG/512/customer-service_icon-icons.com_52843.png")
//                .placeholder(R.drawable.customer_service)
//                .into(imgAdmin);
        imgAdmin.setImageResource(R.drawable.customer_service);
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