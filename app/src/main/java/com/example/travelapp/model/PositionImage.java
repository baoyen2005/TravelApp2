package com.example.travelapp.model;

import android.net.Uri;

public class PositionImage {
    private Uri uri;
    private int position;

    public PositionImage(Uri uri, int position) {
        this.uri = uri;
        this.position = position;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
