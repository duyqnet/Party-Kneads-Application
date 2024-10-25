package com.ignacio.partykneadsapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ignacio.partykneadsapp.model.ProductShopModel;

public class Cake_Description extends Fragment {

    private TextView productName, productPrice, productDescription, ratePercent, numReviews;
    private ImageView productImage;
    private ProductShopModel productShopModel;
    private FirebaseFirestore firestore;
    private ListenerRegistration productListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cake__description, container, false);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Get the passed arguments
        Bundle args = getArguments();
        if (args != null) {
            productShopModel = (ProductShopModel) args.getSerializable("detailed");
        }

        // Initialize views
        productImage = view.findViewById(R.id.productImage);
        productName = view.findViewById(R.id.productName);
        productPrice = view.findViewById(R.id.productPrice);
        productDescription = view.findViewById(R.id.productDescription);
        ratePercent = view.findViewById(R.id.ratePercent);
        numReviews = view.findViewById(R.id.numReviews);

        // Check if productShopModel is available and load the product details
        if (productShopModel != null) {
            loadProductDetails(productShopModel.getId()); // Assuming getId() returns the document ID
        }

        return view;
    }

    private void loadProductDetails(String productId) {
        DocumentReference productRef = firestore.collection("products").document(productId);

        productListener = productRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    // Handle the error
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Fetch and display product details
                    String imageUrl = documentSnapshot.getString("imageUrl");
                    String name = documentSnapshot.getString("name");
                    String price = documentSnapshot.getString("price");
                    String description = documentSnapshot.getString("description");
                    String rate = documentSnapshot.getString("rate");
                    String numReviewsStr = documentSnapshot.getString("numreviews");

                    // Populate the views with data
                    Glide.with(Cake_Description.this).load(imageUrl).into(productImage);
                    productName.setText(name);
                    productPrice.setText(price);
                    productDescription.setText(description);
                    ratePercent.setText(rate);
                    numReviews.setText(numReviewsStr);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove the listener to prevent memory leaks
        if (productListener != null) {
            productListener.remove();
        }
    }
}
