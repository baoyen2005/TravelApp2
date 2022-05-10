package com.example.travelapp.function_util;

import static com.example.travelapp.constant.UserHomeConstant.TAG_USER_DETAIL_POST;
import static com.example.travelapp.constant.UserHomeConstant.TAG_USER_HOME;
import static com.example.travelapp.constant.UserHomeConstant.TAG_USER_HOME_POST;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.travelapp.model.FavoritePost;
import com.example.travelapp.model.Post;
import com.example.travelapp.view.interfacefragment.InterfaceEventGetFavoritePostByIDListener;
import com.example.travelapp.view.interfacefragment.InterfaceEventGetPostByIDListener;
import com.example.travelapp.view.interfacefragment.InterfaceEventGetPostListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class GetPostFromFirebaseStorage {
    public void getAllPostFromFirebase(InterfaceEventGetPostListener listener) {
        FirebaseFirestore.getInstance().collection("posts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Post> listPost = (ArrayList<Post>) queryDocumentSnapshots.toObjects(Post.class);
                    if (listPost != null) {
                        listener.getAllPostSuccess(listPost);
                        Log.d(TAG_USER_HOME_POST, "getpost onSuccess: postList = " + listPost.size());

                    } else {
                        listener.getPostsFail(" get post fail");
                        Log.d(TAG_USER_HOME_POST, "getpost null");

                    }


                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG_USER_HOME, "onFailure: " + e);
                        listener.getPostsFail("get post fail");

                    }
                });
    }

    public void getPostFromFirebaseByID(String postID, InterfaceEventGetPostByIDListener listener) {
        FirebaseFirestore.getInstance().collection("posts")
                .whereEqualTo("postId", postID)
                .get()
                .addOnSuccessListener(
                        queryDocumentSnapshots -> {
                            ArrayList<Post> listPost = (ArrayList<Post>) queryDocumentSnapshots.toObjects(Post.class);
                            for (int i = 0; i < listPost.size(); i++) {
                                if (listPost.get(i) != null) {
                                    Post post = listPost.get(i);
                                    listener.getPostByIDSuccess(post);
                                } else {
                                    listener.getPostsFail(" this post not exits");

                                }
                            }

                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG_USER_DETAIL_POST, "onFailure: " + e);
                        listener.getPostsFail("get post fail");

                    }
                });
    }


    public void getFavoritePostFromFirebaseByID(String postID,
                                                InterfaceEventGetFavoritePostByIDListener listener) {
        FirebaseFirestore.getInstance().collection("favorite")
                .whereEqualTo("postId", postID)
                .get()
                .addOnSuccessListener(
                        queryDocumentSnapshots -> {
                            ArrayList<FavoritePost> listFavoritePost = (ArrayList<FavoritePost>) queryDocumentSnapshots.toObjects(FavoritePost.class);
                            for (int i = 0; i < listFavoritePost.size(); i++) {
                                if (listFavoritePost.get(i) != null) {
                                    FavoritePost favoritePost = listFavoritePost.get(i);
                                    listener.getFavoritePostByIDSuccess(favoritePost);
                                } else {
                                    listener.getFavoritePostsFail(" this post not exits");

                                }
                            }

                        })
                .addOnFailureListener(e -> {
                    Log.d(TAG_USER_DETAIL_POST, "onFailure: " + e);
                    listener.getFavoritePostsFail("get post fail");

                });
    }

}
