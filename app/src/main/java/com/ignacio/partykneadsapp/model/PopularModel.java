package com.ignacio.partykneadsapp.model;

public class PopularModel {
    private String name;
    private String description;
    private String price;
    private int imageResource; // Resource ID for the image

    public PopularModel(String name, String description, String price, int imageResource) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResource() {
        return imageResource;
    }
}