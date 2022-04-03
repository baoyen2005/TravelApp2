package com.example.travelapp.function_util;

import static com.example.travelapp.constant.UserHomeConstant.TAG_USER_HOME;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.travelapp.model.User;
import com.example.travelapp.view.interfacefragment.InterfaceEventGetCurrentUserListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GetUserFromFireStorage {
    public void getUserFromFirebase(String uid, InterfaceEventGetCurrentUserListener listener){

        FirebaseFirestore.getInstance().collection("users")
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<User> listUser = (ArrayList<User>) queryDocumentSnapshots.toObjects(User.class);
                        for (int i = 0; i < listUser.size(); i++) {
                            if (listUser.get(i) != null) {
                                User user = listUser.get(i);
                                listener.getCurrentUserSuccess(user);
                                Log.d(TAG_USER_HOME, "getUserFromFirebase onSuccess: current user " + user.getImageURL());
                            } else {
                                listener.getCurrentUserFail(" current not exits");

                            }
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG_USER_HOME, "onFailure: "+e);
                listener.getCurrentUserFail("authentication failed");

            }
        });
    }
}
