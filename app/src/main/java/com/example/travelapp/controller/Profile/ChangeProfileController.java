package com.example.travelapp.controller.Profile;

import android.app.Activity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ChangeProfileController {
    private Activity activity;
    public void updateProfile(String username, String newPassword,String newAddress,String newPhone, String newEmail, String userid) {

        Map<String, Object> map = new HashMap<>();
        map.put("address", newAddress);
        FirebaseFirestore.getInstance().collection("users")
                .document(userid)
                .set(map, SetOptions.merge());
        map.put("password", newPassword);
        FirebaseFirestore.getInstance().collection("users")
                .document(userid)
                .set(map, SetOptions.merge());
        map.put("phone", newPhone);
        FirebaseFirestore.getInstance().collection("users")
                .document(userid)
                .set(map, SetOptions.merge());
        map.put("email", newEmail);
        FirebaseFirestore.getInstance().collection("users")
                .document(userid)
                .set(map, SetOptions.merge());

    }

}

