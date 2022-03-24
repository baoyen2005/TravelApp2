package com.example.travelapp.controller.createaccount;

import android.app.ProgressDialog;
import android.net.Uri;

public interface ICreateAccController {
    public void onCreateAcc(String username, String phone, String address, String password, String passwordAgain,
                            String email,
                            Uri url, ProgressDialog loadingBar);

}
