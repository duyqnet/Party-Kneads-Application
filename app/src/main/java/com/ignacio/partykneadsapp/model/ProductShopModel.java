package com.ignacio.partykneadsapp.model;

import java.io.Serializable;

public class ProductShopModel implements Serializable {
    private String id; // Add this field
    private String imageUrl;
    private String name;
    private String price;
    private String description; // Ensure this exists
    private String rate;
    private String numreviews;

    // Constructor
    public ProductShopModel(String id, String imageUrl, String name, String price, String description, String rate, String numreviews) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
        this.description = description;
        this.rate = rate;
        this.numreviews = numreviews;
    }

    // Getter methods
    public String getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getRate() {
        return rate;
    }

    public String getNumreviews() {
        return numreviews;
    }
}
