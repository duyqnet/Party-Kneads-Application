package com.ignacio.partykneadsapp.model;

public class ProductShopModel {
    private String imageUrl;
    private String name;
    private String price;

    public ProductShopModel(String imageUrl, String name, String price) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
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
}
