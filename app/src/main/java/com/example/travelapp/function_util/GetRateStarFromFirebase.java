package com.example.travelapp.function_util;

import android.util.Log;

import com.example.travelapp.model.Star;
import com.example.travelapp.view.interfacefragment.InterfaceEventCheckedRateStarListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class GetRateStarFromFirebase {
    private final String TAG= "GetRateStarFromFirebase";
    public void isCheckedUserRateApp(String uid, InterfaceEventCheckedRateStarListener listener){

        FirebaseFirestore.getInstance().collection("star")
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Star> listStar = (ArrayList<Star>) queryDocumentSnapshots.toObjects(Star.class);
                    if(listStar!=null){
                        for (int i = 0; i < listStar.size(); i++) {
                            if (listStar.get(i) != null) {
                                listener.isExist(listStar.get(i).getRatingStar());
                                Log.d(TAG, "isCheckedUserRateApp onSuccess list not null " );
                            } else {
                                listener.notExist();
                                Log.d(TAG, "isCheckedUserRateApp onSuccess list.get(i) null " );
                            }
                        }
                    }
                    else{
                        listener.notExist();

                        Log.d(TAG, "isCheckedUserRateApp onSuccess list not null " );

                    }

                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "onFailure: "+e);
                    listener.notExist();

                });
    }
}
