package com.ignacio.partykneadsapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItemModel implements Parcelable {
    private String productName;
    private String cakeSize; // Add this field
    private int quantity; // Assuming this is an integer
    private String totalPrice; // Assuming price is stored as a string
    private String rate; // Add this field if you want to display the rate
    private String numReviews; // Add this field if you want to display the number of reviews
    private String imageUrl;
    private boolean isSelected;


    // Required empty constructor for Firestore
    public CartItemModel() {}

    // Parcelable constructor
    protected CartItemModel(Parcel in) {
        productName = in.readString();
        cakeSize = in.readString(); // Read the new field
        quantity = in.readInt();
        totalPrice = in.readString();
        rate = in.readString();
        numReviews = in.readString();
        imageUrl = in.readString();
    }

    // Parcelable Creator
    public static final Creator<CartItemModel> CREATOR = new Creator<CartItemModel>() {
        @Override
        public CartItemModel createFromParcel(Parcel in) {
            return new CartItemModel(in);
        }

        @Override
        public CartItemModel[] newArray(int size) {
            return new CartItemModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productName);
        dest.writeString(cakeSize); // Write the new field
        dest.writeInt(quantity);
        dest.writeString(totalPrice);
        dest.writeString(rate);
        dest.writeString(numReviews);
        dest.writeString(imageUrl);
    }

    // Getters
    public String getProductName() {
        return productName;
    }

    public String getCakeSize() {
        return cakeSize;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getTotalPrice() {
        return totalPrice; // Changed from getPrice() for clarity
    }

    public String getRate() {
        return rate;
    }

    public String getNumReviews() {
        return numReviews;
    }

    public String getImageUrl() {
        return imageUrl; // Add this method
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public double getTotalPriceAsDouble() {
        try {
            return Double.parseDouble(totalPrice); // Convert String to double
        } catch (NumberFormatException e) {
            return 0; // Return 0 if the conversion fails
        }
    }
}
