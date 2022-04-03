package com.example.travelapp.view.interfacefragment;

public interface InterfaceGetLocation {
    public void getLocationSuccess(String longitude, String latitude);
    public void getLocationFailed(String mes );
}
