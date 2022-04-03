package com.example.travelapp.view.interfacefragment;

import com.example.travelapp.model.User;

public interface InterfaceEventGetCurrentUserListener {

    public void getCurrentUserSuccess(User user);

    public void getCurrentUserFail(String isFail);


}
