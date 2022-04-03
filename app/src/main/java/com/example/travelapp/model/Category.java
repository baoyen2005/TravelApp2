package com.example.travelapp.model;

public class Category {
    private int drawableImage;
    private String titleCate;

    public Category() {

    }

    public Category(int drawableImage, String titleCate) {
        this.drawableImage = drawableImage;
        this.titleCate = titleCate;
    }

    public int getDrawableImage() {
        return drawableImage;
    }

    public void setDrawableImage(int drawableImage) {
        this.drawableImage = drawableImage;
    }

    public String getTitleCate() {
        return titleCate;
    }

    public void setTitleCate(String titleCate) {
        this.titleCate = titleCate;
    }
}
