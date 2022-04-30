package com.example.travelapp.view.adapter.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelapp.R;
import com.example.travelapp.model.FavoritePost;

import java.util.ArrayList;
import java.util.List;

public class UserFavoriteListPostAdapter extends
        RecyclerView.Adapter<UserFavoriteListPostAdapter.UserFavoriteListPostViewHolder> implements Filterable {
    private final List<FavoritePost> favoritePostList;
    private Context context;
    List<FavoritePost> favoriteListCopy = new ArrayList<>();
    private final OnItemFavoritePostClickListener onItemFavoritePostClickListener;

    public UserFavoriteListPostAdapter(List<FavoritePost> favoritePostList, Context context, OnItemFavoritePostClickListener onItemFavoritePostClickListener) {
        this.favoritePostList = favoritePostList;
        this.context = context;
        this.onItemFavoritePostClickListener = onItemFavoritePostClickListener;
    }

    @NonNull
    @Override
    public UserFavoriteListPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mcontext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        View view = layoutInflater.inflate(R.layout.item_user_favorite_layout,parent, false);
        return new UserFavoriteListPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserFavoriteListPostViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FavoritePost favoritePost = favoritePostList.get(position);
        Glide.with(context)
                .load(favoritePost.getList_photos().get(0).getUrl())
                .into(holder.imgItemFavorite);
        holder.txtTitleFavoritePost.setText(favoritePost.getTouristName());
        holder.constraintFavoriteItem.setOnClickListener(view ->
                onItemFavoritePostClickListener.onItemFavoritePostClick(favoritePost,position));

    }

    @Override
    public int getItemCount() {
        return favoritePostList.size();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<FavoritePost> newListPost) {
        favoritePostList.clear();
        favoritePostList.addAll(newListPost);
         //       Log.d("aaa", "updateData: " + newList.size)
        notifyDataSetChanged();
    }
    public interface OnItemFavoritePostClickListener{
        void onItemFavoritePostClick(FavoritePost post, int position);
    }
    protected static class UserFavoriteListPostViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgItemFavorite;
        public TextView txtTitleFavoritePost;
        public ConstraintLayout constraintFavoriteItem;
        public UserFavoriteListPostViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemFavorite = itemView.findViewById(R.id.imgItemFavorite);
            txtTitleFavoritePost = itemView.findViewById(R.id.txtTitleFavoritePost);
            constraintFavoriteItem = itemView.findViewById(R.id.constraintFavoriteItem);
        }
    }
    @Override
    public Filter getFilter() {

        return new Filter() {

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List<FavoritePost> newList = new ArrayList<>();
                newList.addAll((ArrayList<FavoritePost>) results.values);
                updateData(newList);
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSearch = constraint.toString();

                if (charSearch.isEmpty()) {
                    favoriteListCopy.clear();
                    favoriteListCopy.addAll(favoritePostList);
                }
                else {
                    List<FavoritePost> res = new ArrayList<>();
                    for (int i = 0; i < favoritePostList.size(); i++) {
                        String data = favoritePostList.get(i).getTouristName();
                        if (data.toLowerCase().contains(charSearch.toLowerCase()))  {
                            res.add(favoritePostList.get(i));
                        }
                    }
                    favoriteListCopy.clear();
                    favoriteListCopy.addAll(res);
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = favoriteListCopy;
                return filterResults;
            }
        };
    }
}
