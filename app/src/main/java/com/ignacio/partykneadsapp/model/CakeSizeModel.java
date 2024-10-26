package com.ignacio.partykneadsapp.model;

public class CakeSizeModel {
    private String size;
    private String servings;
    private String price; // New field for price

    // Updated constructor to include price
    public CakeSizeModel(String size, String servings, String price) {
        this.size = size;
        this.servings = servings;
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public String getServings() {
        return servings;
    }

    public String getPrice() {
        return price; // Getter for price
    }
}