package com.example.travelapp.view.adapter.user;

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

public class KnowYourWorldUserHomeAdapter extends RecyclerView.Adapter<KnowYourWorldUserHomeAdapter.RecommendUserHomeViewHolder>{
    private List<Post> postList;
    private Context context;

    public KnowYourWorldUserHomeAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecommendUserHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mcontext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        View view = layoutInflater.inflate(R.layout.item_know_your_world_layout_user_home,parent, false);
        RecommendUserHomeViewHolder viewHolder = new RecommendUserHomeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendUserHomeViewHolder holder, int position) {
        Post post = postList.get(position);
        Glide.with(context)
                .load(post.getImage2())
                .into(holder.itemImageViewKnowWorld);
        holder.itemNameofLocationKnowWorld.setText(post.getTouristName());
        holder.itemAddressOfLocationKnowWorld.setText(post.getTouristDetailAddress());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    protected class RecommendUserHomeViewHolder extends RecyclerView.ViewHolder{
        public ImageView itemImageViewKnowWorld;
        public TextView itemNameofLocationKnowWorld;
        public  TextView itemAddressOfLocationKnowWorld;
        public RecommendUserHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImageViewKnowWorld = itemView.findViewById(R.id.item_image_know_world_user_home);
            itemNameofLocationKnowWorld = itemView.findViewById(R.id.item_name_of_location_know_world_home_user);
            itemAddressOfLocationKnowWorld = itemView.findViewById(R.id.item_address_of_location_know_world_home_user);
        }
    }
}
