package com.example.travelapp.function_util;

import com.example.travelapp.model.Category;

public class SetCategoryListUtil {
    public Category setCategoryList(String title, int drawable){
        Category category = new Category();
        category.setTitleCate(title);
        category.setDrawableImage(drawable);
        return category;
    }
}
