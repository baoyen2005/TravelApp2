package com.example.travelapp.view.adapter.admin;

import android.annotation.SuppressLint;
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
import com.example.travelapp.model.Post;

import java.util.List;

public class DisplayAllPostAdminAdapter extends RecyclerView.Adapter<DisplayAllPostAdminAdapter.DisplayPostInAdminViewHolder> {

    private final List<Post> listPost;
    private final Context mContext;
    private final ClickListenerItemDisplayPost clickListenerItemDisplayPost;

    public DisplayAllPostAdminAdapter(List<Post> listPost, Context mContext, ClickListenerItemDisplayPost clickListenerItemDisplayPost) {
        this.listPost = listPost;
        this.mContext = mContext;
        this.clickListenerItemDisplayPost = clickListenerItemDisplayPost;
    }

    @NonNull
    @Override
    public DisplayPostInAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_post_in_admin_home, parent, false);

        DisplayPostInAdminViewHolder viewHolder = new DisplayPostInAdminViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayPostInAdminViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post currentPost = listPost.get(position);
        holder.tvAddressItem.setText(currentPost.getTouristDetailAddress());

        Glide.with(mContext)
                .load(currentPost.getList_photos().get(0).getUrl())
                .into(holder.imgTravelReviewInAdminHomeItem);
        holder.tvAddressItem.setOnClickListener(view ->
                clickListenerItemDisplayPost.onClickShowDetailPost(currentPost, position));
        holder.imgMoreInAdminHomeFM.setOnClickListener(view ->
                clickListenerItemDisplayPost.onOptionsMenuClicked(view, currentPost, position));
    }


    public void setListPost(List<Post> list) {
        listPost.clear();
        listPost.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listPost.size();
    }

    public interface ClickListenerItemDisplayPost {
        void onClickShowDetailPost(Post post, int position);

        void onOptionsMenuClicked(View view, Post post, int position);
    }

    public class DisplayPostInAdminViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAddressItem;
        public ImageView imgTravelReviewInAdminHomeItem;
        private ImageView imgMoreInAdminHomeFM;

        public DisplayPostInAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddressItem = itemView.findViewById(R.id.tvAddressTravelItem);
            imgTravelReviewInAdminHomeItem = itemView.findViewById(R.id.img_travel_review_item);
            imgMoreInAdminHomeFM = itemView.findViewById(R.id.imgMoreInAdminHomeFM);
        }
    }
}
