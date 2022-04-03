package com.example.travelapp.view.interfacefragment;

import com.example.travelapp.model.Post;
import com.example.travelapp.model.User;

import java.util.List;

public interface InterfaceEventGetPostListener {

    public void getAllPostSuccess(List<Post> postList);

    public void getPostsFail(String isFail);

//


}
