package com.example.travelapp.view.activity.login.interface_login;

public interface InterfaceLoginView {
    void OnUserLoginSuccess(String message);
    void OnUserLoginFail(String message);
    void OnAdminLoginSuccess(String message);
    void OnAdminLoginFail(String message);
}
