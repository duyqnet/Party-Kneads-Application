package com.ignacio.partykneadsapp.model;

public class OrderHistoryModel {
    private String productName;
    private String price;
    private int imageResource; // Resource ID for the image

    public OrderHistoryModel(String productName, String price, int imageResource) {
        this.productName = productName;
        this.price = price;
        this.imageResource = imageResource;
    }

    public String getProductName() {
        return productName;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResource() {
        return imageResource;
    }
}
