package com.ignacio.partykneadsapp.model;

public class ProductShopModel {
    private String name;
    private String price;
    private String rating; // Or double, if you prefer
    private int soldCount;
    private int imageResource; // Image resource ID

    public ProductShopModel(String name, String price, String rating, int soldCount, int imageResource) {
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.soldCount = soldCount;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getRating() {
        return rating;
    }

    public int getSoldCount() {
        return soldCount;
    }

    public int getImageResource() {
        return imageResource;
    }
}

