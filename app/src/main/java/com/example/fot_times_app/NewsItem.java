package com.example.fot_times_app;

public class NewsItem {
    private String title;
    private String summary;
    private String date;
    private String image;
    private String description;
    private int imageResId; // Not from Firebase, resolved at runtime

    public NewsItem() {
        // Required for Firebase
    }

    public NewsItem(String title, String summary, String date, String image, String description) {
        this.title = title;
        this.summary = summary;
        this.date = date;
        this.image = image;
        this.description = description;
    }

    public NewsItem(String title, String description, String date, int imageResId) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
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
