package com.example.travelapp.view.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.R;
import com.example.travelapp.model.Category;

import java.util.List;

public class CategoryUserHomeAdapter extends RecyclerView.Adapter<CategoryUserHomeAdapter.CategoryUserHomeViewHolder>{
    private List<Category> categoryList;
    private Context context;

    public CategoryUserHomeAdapter(List<Category> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryUserHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mcontext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        View view = layoutInflater.inflate(R.layout.item_categories_user_layout,parent, false);
        CategoryUserHomeViewHolder viewHolder = new CategoryUserHomeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryUserHomeViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.itemImageViewCategory.setImageResource(category.getDrawableImage());
        holder.itemTitleCategory.setText(category.getTitleCate());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    protected class CategoryUserHomeViewHolder extends RecyclerView.ViewHolder{
        public ImageView itemImageViewCategory;
        public TextView itemTitleCategory;
        public CategoryUserHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImageViewCategory = itemView.findViewById(R.id.item_image_category_user_home);
            itemTitleCategory = itemView.findViewById(R.id.item_title_category_user_home);
        }
    }
}
