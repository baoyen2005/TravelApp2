package com.example.travelapp.view.adapter.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelapp.R;
import com.example.travelapp.model.Post;

import java.util.List;

public class RecommendUserHomeAdapter extends RecyclerView.Adapter<RecommendUserHomeAdapter.RecommendUserHomeViewHolder>{
    private List<Post> postList;
    private Context context;
    private  OnItemRecommendClickListener onItemRecommendClickListener;

    public RecommendUserHomeAdapter(List<Post> postList, Context context, OnItemRecommendClickListener onItemRecommendClickListener) {
        this.postList = postList;
        this.context = context;
        this.onItemRecommendClickListener = onItemRecommendClickListener;
    }

    @NonNull
    @Override
    public RecommendUserHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mcontext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        View view = layoutInflater.inflate(R.layout.item_recomment_layout_user_home,parent, false);
        RecommendUserHomeViewHolder viewHolder = new RecommendUserHomeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendUserHomeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post post = postList.get(position);
        Glide.with(context)
                .load(post.getList_photos().get(0).getUrl())
                .into(holder.itemImageViewRecommend);
        holder.itemNameofLocation.setText(post.getTouristName());
        holder.itemAddressOfLocation.setText(post.getTouristDetailAddress());
        holder.cardViewRecommendItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemRecommendClickListener.onItemClick(post,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public interface OnItemRecommendClickListener{
        public  void onItemClick(Post post,int position);
    }
    protected class RecommendUserHomeViewHolder extends RecyclerView.ViewHolder{
        public ImageView itemImageViewRecommend;
        public TextView itemNameofLocation;
        public  TextView itemAddressOfLocation;
        private CardView cardViewRecommendItem;
        public RecommendUserHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImageViewRecommend = itemView.findViewById(R.id.item_image_recommend_user_home);
            itemNameofLocation = itemView.findViewById(R.id.item_name_of_location_recommend_home_user);
            itemAddressOfLocation = itemView.findViewById(R.id.item_address_of_location_recommend_home_user);
            cardViewRecommendItem = itemView.findViewById(R.id.cardViewRecommendItem);
        }
    }
}
