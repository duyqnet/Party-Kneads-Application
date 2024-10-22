package com.ignacio.partykneadsapp.model;

public class OrderHistoryModel {
    private String name;
    private String rate;
    private String sold;
    private String price;
    private int imageResource;

    public OrderHistoryModel(String name, String rate, String price, int imageResource, String sold) {
        this.name = name;
        this.rate = rate;
        this.price = price;
        this.sold = sold;
        this.imageResource = imageResource;
    }


    public String getName() {
        return name;
    }

    public String getRate() {
        return rate;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getSold() {
        return sold;
    }
}
