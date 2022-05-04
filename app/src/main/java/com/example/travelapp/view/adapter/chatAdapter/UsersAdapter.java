package com.example.travelapp.view.adapter.chatAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelapp.R;
import com.example.travelapp.model.User;
import com.example.travelapp.view.activity.chat.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> implements Filterable {

    Context context;
    ArrayList<User> users;
    ArrayList<User> usersold;

    public UsersAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
        this.usersold = users;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_conversations, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User user = users.get(position);
        String currentId = FirebaseAuth.getInstance().getUid();
        String senderRoom = currentId + user.getUid();
        Log.d("Admin UID", senderRoom);
        FirebaseDatabase.getInstance()
                .getReference().child("Chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String lastMsg = snapshot.child("lastMsg").getValue(String.class);
                            Log.d("lastMsg", lastMsg+"yy");
                            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                            Long time = snapshot.child("lastMsgTime").getValue(Long.class);
                            if (time != null) {
                                holder.tvTime.setText(dateFormat.format(new Date(time)));
                            }
                            holder.tvLastMessage.setText(lastMsg);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        holder.tvName.setText(user.getUsername());
        Glide.with(context).load(user.getImageURL())
                .placeholder(R.drawable.user_default)
                .into(holder.avatar);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("name", user.getUsername());
                intent.putExtra("uid", user.getUid());
                intent.putExtra("avatar", user.getImageURL());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String txtSearch = charSequence.toString();
                if (txtSearch.isEmpty()){
                    users = usersold;
                }else {
                    ArrayList<User> listUser = new ArrayList<>();
                    for (User user: usersold){
                        if (user.getUsername().toLowerCase().contains(txtSearch)){
                            listUser.add(user);
                        }
                    }
                    users = listUser;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = users;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                users = (ArrayList<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final ImageView avatar;
        private final TextView tvLastMessage;
        private final TextView tvTime;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.txtName);
            avatar = itemView.findViewById(R.id.image_avatar);
            tvLastMessage = itemView.findViewById(R.id.txtLastMessage);
            tvTime = itemView.findViewById(R.id.txtTime);
        }
    }
}