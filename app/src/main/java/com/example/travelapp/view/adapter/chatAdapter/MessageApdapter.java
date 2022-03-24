package com.example.travelapp.view.adapter.chatAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelapp.R;
import com.example.travelapp.constant.Data;
import com.example.travelapp.model.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

public class MessageApdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Message> messages;
    String senderAvatar, receiverAvatar;
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    FirebaseFirestore fs;
    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;

    public MessageApdapter(Context context, ArrayList<Message> messages, String receiverAvatar) {
        this.context = context;
        this.messages = messages;
        this.receiverAvatar = receiverAvatar;
        mAuth = FirebaseAuth.getInstance();

        String currentId = mAuth.getUid();
        fs = FirebaseFirestore.getInstance();
        fs.collection("users").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getData() != null) {
                    senderAvatar = (String) documentSnapshot.get("imageURL");
                }
            }
        });

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_send, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_receive, parent, false);
            return new ReceiverViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderId())) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (holder.getClass() == SentViewHolder.class) {
            SentViewHolder sentViewHolder = (SentViewHolder) holder;

            if (message.getMessage().equals("send the photo...")) {
                sentViewHolder.image_send.setVisibility(View.VISIBLE);
                sentViewHolder.tvSentMessage.setVisibility(View.GONE);
                Glide.with(context).
                        load(message.getImageUrl()).
                        placeholder(R.drawable.placeholder_image).
                        into(sentViewHolder.image_send);
            }

            sentViewHolder.tvSentMessage.setText(message.getMessage());
            if (FirebaseAuth.getInstance().getUid().equals(Data.adminUid)){
                Glide.with(context).load(senderAvatar).placeholder(R.drawable.customer_service).into(sentViewHolder.avatar_sender);
            }else {
                Glide.with(context).load(senderAvatar).placeholder(R.drawable.user_default).into(sentViewHolder.avatar_sender);
            }
        } else {
            ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder) holder;

            if (message.getMessage().equals("send the photo...")) {
                receiverViewHolder.image_receive.setVisibility(View.VISIBLE);
                receiverViewHolder.tvReceiveMessage.setVisibility(View.GONE);
                Glide.with(context).
                        load(message.getImageUrl()).
                        placeholder(R.drawable.placeholder_image).
                        into(receiverViewHolder.image_receive);
            }

            receiverViewHolder.tvReceiveMessage.setText(message.getMessage());
            if (FirebaseAuth.getInstance().getUid().equals(Data.adminUid)){
                Glide.with(context)
                        .load(senderAvatar)
                        .placeholder(R.drawable.user_default)
                        .into(receiverViewHolder.avatar_receiver);
            }else {
                Glide.with(context).load(receiverAvatar).placeholder(R.drawable.user_default).into(receiverViewHolder.avatar_receiver);
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class SentViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSentMessage;
        private ImageView avatar_sender;
        private ImageView image_send;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSentMessage = itemView.findViewById(R.id.tvMessageSend);
            avatar_sender = itemView.findViewById(R.id.image_sender);
            image_send = itemView.findViewById(R.id.image_send);
        }
    }

    public static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        private TextView tvReceiveMessage;
        private ImageView avatar_receiver;
        private ImageView image_receive;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReceiveMessage = itemView.findViewById(R.id.tvMessageRecive);
            avatar_receiver = itemView.findViewById(R.id.avatar_recever);
            image_receive = itemView.findViewById(R.id.image_receive);
        }
    }
}
