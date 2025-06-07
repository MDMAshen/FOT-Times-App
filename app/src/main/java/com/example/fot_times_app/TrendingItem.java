package com.example.fot_times_app;

public class TrendingItem {
    private String title;
    private String image;
    private String description;

    public TrendingItem() {
        // Required for Firebase
    }

    public TrendingItem(String title, String image, String description) {
        this.title = title;
        this.image = image;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }
}
