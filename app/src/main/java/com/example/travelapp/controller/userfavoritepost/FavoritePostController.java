package com.example.travelapp.controller.userfavoritepost;

import android.content.Context;

import com.example.travelapp.model.FavoritePost;
import com.example.travelapp.view.adapter.user.UserFavoriteListPostAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FavoritePostController implements InterfaceFavoritePostFMController {
    private List<FavoritePost> favoritePostList;

    public interface OnLoadDataListener {
        void onSuccess(List<FavoritePost> list);

        void onFailure();

        void onAuthNull();

    }

    @Override
    public void getAllFavoritePostFromFirebase(Context context, String uid, OnLoadDataListener listener) {
        FirebaseFirestore.getInstance().collection("favorite").whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (context == null) {
                        return;
                    }
                    List<FavoritePost> list = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        FavoritePost favoritePost = documentSnapshot.toObject(FavoritePost.class);
                        if (favoritePost != null) {
                            list.add(favoritePost);
                        }
                        listener.onSuccess(list);
                    }
                }).addOnFailureListener(e -> {
            if (context == null) {
                return;
            }
            listener.onFailure();

        });

    }

    @Override
    public void filter(String s, List<FavoritePost> favoritePostList, UserFavoriteListPostAdapter userFavoriteListPostAdapter) {
        List<FavoritePost> filteredList = new ArrayList<>();
        for(FavoritePost post : favoritePostList){
            if (post.getTouristName().toLowerCase().contains(s.toLowerCase())){
                filteredList.add(post);
            }
        }
        userFavoriteListPostAdapter.updateData(filteredList);
    }
}
