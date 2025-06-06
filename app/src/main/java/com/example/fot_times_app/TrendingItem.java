package com.example.fot_times_app;

public class TrendingItem {
    private String image;
    private String title;

    private int imageResId; //  Add this for local drawable reference

    public TrendingItem() {
        // Required for Firebase
    }

    public TrendingItem(String image, String title) {
        this.image = image;
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    //  Add this getter and setter for imageResId
    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
