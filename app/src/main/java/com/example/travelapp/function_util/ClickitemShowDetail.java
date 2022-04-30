package com.example.travelapp.function_util;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.travelapp.view.userfragment.DetailFragmentHomeUser;

public class ClickitemShowDetail {


    public  void ClickItemShowDetailInfor(String idObject, int idFragment, FragmentManager fragmentManager, String tag){
        DetailFragmentHomeUser detailFragmentHomeUser = DetailFragmentHomeUser.newInstance(idObject,tag);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.addToBackStack(null);
        transaction.replace(idFragment, detailFragmentHomeUser);
        transaction.commit();
    }
}

