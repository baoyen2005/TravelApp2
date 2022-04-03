package com.example.travelapp.function_util;

import static com.example.travelapp.constant.UserHomeConstant.TAG_USER_DETAIL_POST;
import static com.example.travelapp.constant.UserHomeConstant.TAG_USER_HOME;
import static com.example.travelapp.constant.UserHomeConstant.TAG_USER_HOME_POST;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.travelapp.model.Post;
import com.example.travelapp.model.User;
import com.example.travelapp.view.interfacefragment.InterfaceEventGetPostByIDListener;
import com.example.travelapp.view.interfacefragment.InterfaceEventGetPostListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GetAllPostFromFirebaseStorage {
    public void getAllPostFromFirebase(InterfaceEventGetPostListener listener) {
        FirebaseFirestore.getInstance().collection("posts")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Post> listPost = (ArrayList<Post>) queryDocumentSnapshots.toObjects(Post.class);
                        if (listPost != null) {
                            listener.getAllPostSuccess(listPost);
                            Log.d(TAG_USER_HOME_POST, "getpost onSuccess: postList = " + listPost.size());

                        } else {
                            listener.getPostsFail(" get post fail");
                            Log.d(TAG_USER_HOME_POST, "getpost null");

                        }


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
                        new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                ArrayList<Post> listPost = (ArrayList<Post>) queryDocumentSnapshots.toObjects(Post.class);
                                for (int i = 0; i < listPost.size(); i++) {
                                    if (listPost.get(i) != null) {
                                        Post post = listPost.get(i);
                                        listener.getPostByIDSuccess(post);
                                        Log.d(TAG_USER_DETAIL_POST, "getpostByid onSuccess: current post " + post.getImage0());
                                    } else {
                                        listener.getPostsFail(" this post not exits");

                                    }
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
}
