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
import com.example.travelapp.model.Post;

import java.util.ArrayList;
import java.util.List;

public class UserSearchListPostAdapter extends
        RecyclerView.Adapter<UserSearchListPostAdapter.UserSearchListPostViewHolder> implements Filterable {
    private final List<Post> postList;
    private final Context context;
    List<Post> postListCopy = new ArrayList<>();
    private final OnItemPostClickListenerInSearchFragment onItemPostClickListenerInSearchFragment;

    public UserSearchListPostAdapter(List<Post> postList, Context context, OnItemPostClickListenerInSearchFragment onItemPostClickListenerInSearchFragment) {
        this.postList = postList;
        this.context = context;
        this.onItemPostClickListenerInSearchFragment = onItemPostClickListenerInSearchFragment;
    }

    @NonNull
    @Override
    public UserSearchListPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mcontext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        View view = layoutInflater.inflate(R.layout.item_post_in_searchview,parent, false);
        return new UserSearchListPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserSearchListPostViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post post = postList.get(position);
        Glide.with(context)
                .load(post.getList_photos().get(0).getUrl())
                .into(holder.img_travel_searchView_item);
        holder.tvAddressTravelInSearchViewItem.setText(post.getTouristName());
        holder.constraintItemFragmentSearch.setOnClickListener(view ->
                onItemPostClickListenerInSearchFragment.onItemFavoritePostClick(post,position));

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

//du
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Post> newListPost) {
        postList.clear();
        postList.addAll(newListPost);
        notifyDataSetChanged();
    }
    public interface OnItemPostClickListenerInSearchFragment {
        void onItemFavoritePostClick(Post post, int position);
    }
    protected static class UserSearchListPostViewHolder extends RecyclerView.ViewHolder{
        public ImageView img_travel_searchView_item;
        public TextView tvAddressTravelInSearchViewItem;
        private ConstraintLayout constraintItemFragmentSearch;
        public UserSearchListPostViewHolder(@NonNull View itemView) {
            super(itemView);
            img_travel_searchView_item = itemView.findViewById(R.id.img_travel_searchView_item);
            tvAddressTravelInSearchViewItem = itemView.findViewById(R.id.tvAddressTravelInSearchViewItem);
            constraintItemFragmentSearch = itemView.findViewById(R.id.constraintItemFragmentSearch);
        }
    }
    @Override
    public Filter getFilter() {

        return new Filter() {

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List<Post> newList = new ArrayList<>();
                newList.clear();
                newList.addAll((ArrayList<FavoritePost>)results.values);
                updateData(newList);
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSearch = constraint.toString();

                if (charSearch.isEmpty()) {
                  //  notifyDataSetChanged();
                    postListCopy.clear();
                    postListCopy.addAll(postList);
                    updateData(postListCopy);
                }
                else {
                    List<Post> res = new ArrayList<>();
                    for (int i = 0; i < postList.size(); i++) {
                        String data = postList.get(i).getTouristName();
                        if (data.toLowerCase().contains(charSearch.toLowerCase()))  {
                            res.add(postList.get(i));
                        }
                    }
                    postListCopy.clear();
                    postListCopy.addAll(res);
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = postListCopy;
                return filterResults;
            }
        };
    }
}
