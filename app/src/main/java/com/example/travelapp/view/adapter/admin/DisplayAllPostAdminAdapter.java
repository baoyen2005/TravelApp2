package com.example.travelapp.view.adapter.admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.R;
import com.example.travelapp.model.Post;

import java.util.ArrayList;

public class DisplayAllPostAdminAdapter extends RecyclerView.Adapter<DisplayAllPostAdminAdapter.DisplayPostViewHolder>{

    private ArrayList<Post> listPost;
    private Context mContext;
    private ClickListenerItemDisplayPost clickListenerItemDisplayPost;
    public DisplayAllPostAdminAdapter(ArrayList<Post> listPost, Context mContext,ClickListenerItemDisplayPost clickListenerItemDisplayPost) {
        this.listPost = listPost;
        this.mContext = mContext;
        this.clickListenerItemDisplayPost = clickListenerItemDisplayPost;
    }

    @NonNull
    @Override
    public DisplayPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_post, parent, false);

        DisplayPostViewHolder viewHolder = new DisplayPostViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayPostViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post currentPost = listPost.get(position);
        holder.tvAddressItem.setText( currentPost.getTouristDetailAddress());
        Uri uri =  Uri.parse(currentPost.getUrlImgReview().get(0));
        holder.imgTravelReviewItem.setImageURI(uri);
        holder.tvAddressItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListenerItemDisplayPost.onClickShowDetailPost(currentPost,position);
            }
        });
    }



    public void setListPost(ArrayList<Post> list){
        listPost.clear();
        listPost.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listPost.size();
    }

    interface ClickListenerItemDisplayPost  {
         void onClickShowDetailPost(Post post , int position);
    }
    public class DisplayPostViewHolder extends RecyclerView.ViewHolder{
        public TextView tvAddressItem;
        public ImageView imgTravelReviewItem;
        public DisplayPostViewHolder(@NonNull View itemView) {
            super(itemView);
             tvAddressItem = itemView.findViewById(R.id.tvAddressTravelItem);
             imgTravelReviewItem = itemView.findViewById(R.id.img_travel_review_item);
        }
    }
}
