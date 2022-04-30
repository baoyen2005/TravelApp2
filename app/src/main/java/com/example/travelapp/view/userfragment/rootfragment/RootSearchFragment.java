package com.example.travelapp.view.userfragment.rootfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.travelapp.R;
import com.example.travelapp.function_util.ReplaceFragment;
import com.example.travelapp.view.userfragment.SearchFragmentUser;

public class RootSearchFragment extends Fragment {

    public RootSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_root_search, container, false);
        ReplaceFragment replaceFragmentInit = new ReplaceFragment();
        replaceFragmentInit.replaceFragment(R.id.root_search_fragment, new SearchFragmentUser(),requireActivity());

        return view;
    }
}