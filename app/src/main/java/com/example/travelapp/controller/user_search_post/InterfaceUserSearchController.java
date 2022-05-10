package com.example.travelapp.controller.user_search_post;

import com.example.travelapp.model.Post;
import com.example.travelapp.view.adapter.user.UserSearchListPostAdapter;

import java.util.List;

public interface InterfaceUserSearchController {
    public void filter(String s, List<Post> postList, UserSearchListPostAdapter userSearchListPostAdapter);

}
