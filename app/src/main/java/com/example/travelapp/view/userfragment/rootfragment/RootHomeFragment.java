package com.example.travelapp.view.userfragment.rootfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.travelapp.R;
import com.example.travelapp.function_util.ReplaceFragment;
import com.example.travelapp.view.userfragment.HomeFragmentUser;


public class RootHomeFragment extends Fragment {
    private ReplaceFragment replaceFragmentInit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceFragmentInit = new ReplaceFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_root_home, container, false);
        replaceFragmentInit.replaceFragment(R.id.root_home_frame, new HomeFragmentUser(),requireActivity());
        return view;
    }
}