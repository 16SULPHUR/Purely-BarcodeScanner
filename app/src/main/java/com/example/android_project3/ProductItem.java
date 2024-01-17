package com.example.android_project3;

public class ProductItem {
    private String title;
    private String code;
    private String imageUrl;

    public ProductItem(String title, String code, String imageUrl) {
        this.title = title;
        this.code = code;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return code;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

