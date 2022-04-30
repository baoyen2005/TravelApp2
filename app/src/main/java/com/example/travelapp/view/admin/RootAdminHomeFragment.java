package com.example.travelapp.view.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.travelapp.R;


public class RootAdminHomeFragment extends Fragment {


    public RootAdminHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_root_admin_home, container, false);

        FragmentTransaction transaction= requireActivity().getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.fragmentRootAdminHome, new AdminHomeFragment());

        transaction.commit();

        return view;
    }
}