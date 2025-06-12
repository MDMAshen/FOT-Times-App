package com.example.fot_times_app;

public class TrendingItem {
    private String image;
    private String title;
    private String description;

    // This field is not stored in Firebase, only used at runtime
    private int imageResId;

    // Required for Firebase
    public TrendingItem() {
    }

    public TrendingItem(String image, String title, String description) {
        this.image = image;
        this.title = title;
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
