package com.example.travelapp.view.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import com.example.travelapp.R;
import com.example.travelapp.base.BaseFragment;
import com.example.travelapp.model.User;
import com.example.travelapp.view.adapter.chatAdapter.UsersAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ChatAdminFragment extends BaseFragment {
    FirebaseDatabase db;
    FirebaseAuth mAth;
    ArrayList<User> users;
    UsersAdapter usersAdapter;
    RecyclerView recyclerView;
    SearchView searchView;
    FirebaseFirestore fs;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_chat_admin;
    }

    @Override
    public void initController() {

    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {
        fs = FirebaseFirestore.getInstance();
        mAth = FirebaseAuth.getInstance();
        recyclerView = getView().findViewById(R.id.recyclerviewUser);
        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(getContext(), users);
        recyclerView.setAdapter(usersAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fs.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("TAG", "Listen failed.", error);
                    return;
                }
                users.clear();
                for (QueryDocumentSnapshot doc : value) {
                   if (!FirebaseAuth.getInstance().getUid().equals(doc.getId())){
                       User user = new User();
                       user.setUsername((String) doc.get("username"));
                       user.setAddress((String) doc.get("address"));
                       user.setEmail((String) doc.get("email"));
                       user.setImageURL((String) doc.get("imageURL"));
                       user.setPhone((String) doc.get("phone"));
                       user.setUid((String) doc.getId());
                       users.add(user);
                   }
                }
                usersAdapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    public void initEvent() {

    }
}