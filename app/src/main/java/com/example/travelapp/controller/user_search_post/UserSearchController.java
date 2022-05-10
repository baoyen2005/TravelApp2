package com.example.travelapp.controller.user_search_post;

import com.example.travelapp.model.Post;
import com.example.travelapp.view.adapter.user.UserSearchListPostAdapter;

import java.util.ArrayList;
import java.util.List;

public class UserSearchController implements InterfaceUserSearchController{
    @Override
    public void filter(String s, List<Post> postList, UserSearchListPostAdapter userSearchListPostAdapter) {
        List<Post> filteredList = new ArrayList<>();
        for(Post post : postList){
            if (post.getTouristName().toLowerCase().contains(s.toLowerCase())){
                filteredList.add(post);
            }
        }
        userSearchListPostAdapter.updateData(filteredList);
    }
}
