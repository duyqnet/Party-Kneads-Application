package com.ignacio.partykneadsapp.model;

import java.io.Serializable;

public class AddToCartModel implements Serializable {
    private String productId;
    private String productName;
    private String cakeSize;
    private int quantity;
    private String totalPrice;
    private String imageUrl;
    private long timestamp;

    public AddToCartModel(String productId, String productName, String cakeSize, int quantity, String totalPrice, String imageUrl, long timestamp) {
        this.productId = productId;
        this.productName = productName;
        this.cakeSize = cakeSize;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.imageUrl = imageUrl; // Corrected assignment
        this.timestamp = timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Getters and setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCakeSize() {
        return cakeSize;
    }

    public void setCakeSize(String cakeSize) {
        this.cakeSize = cakeSize;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getTimestamp() {
        return timestamp; // Getter for timestamp
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp; // Setter for timestamp
    }
}
