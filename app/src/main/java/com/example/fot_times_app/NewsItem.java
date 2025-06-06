package com.example.fot_times_app;

public class NewsItem {
    private String title;
    private String summary;
    private String date;
    private String image; // image filename from Firebase (e.g., "news_tech")
    private int imageResId; // resolved resource ID for drawable (not stored in Firebase)

    // Required default constructor for Firebase
    public NewsItem() {
    }

    // Constructor for Firebase parsing
    public NewsItem(String title, String summary, String date, String image) {
        this.title = title;
        this.summary = summary;
        this.date = date;
        this.image = image;
    }

    // Constructor for usage after resolving imageResId
    public NewsItem(String title, String summary, String date, int imageResId) {
        this.title = title;
        this.summary = summary;
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

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
