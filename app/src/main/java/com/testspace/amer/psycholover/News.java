package com.testspace.amer.psycholover;

import android.graphics.Bitmap;

public class News {
    public static final Bitmap NO_IMGBITMAP = null;
    public static final String NO_DESCRIPTION = "NO_DESCRIPTION";
    public static final String NO_SECTION = "NO_SECTION";
    public static final String NO_DATE = "NO_DATE";
    public static final String NO_AUTHOR = "NO_AUTHOR";
    public static final String NO_URL = "NO_URL";
    private Bitmap imgBitmap;
    private String title;
    private String description;
    private String section;
    private String author;
    private String date;
    private String url;

    News(Bitmap imgBitmap, String title, String description, String section, String author, String date, String url) {
        this.imgBitmap = imgBitmap;
        this.title = title;
        this.description = description;
        this.section = section;
        this.author = author;
        this.date = date;
        this.url = url;
    }

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description.replaceAll("<br>", "");
    }

    public String getSection() {
        return section;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date.substring(0, 10);
    }

    public String getUrl() {
        return url;
    }
}