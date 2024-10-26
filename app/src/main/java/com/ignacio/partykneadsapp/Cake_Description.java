package com.ignacio.partykneadsapp;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ignacio.partykneadsapp.adapters.CakeSizeAdapter;
import com.ignacio.partykneadsapp.menus.ShopFragment;
import com.ignacio.partykneadsapp.model.CakeSizeModel;
import com.ignacio.partykneadsapp.model.ProductShopModel;
import com.ignacio.partykneadsapp.model.AddToCartModel;

import java.util.ArrayList;
import java.util.List;

public class Cake_Description extends Fragment {

    private TextView productName, productPrice, productDescription, ratePercent, numReviews;
    private ImageView productImage;
    private TextView minusButton, quantityTextView, plusButton; // Quantity controls
    private Button btnAddtoCart; // Add to Cart button
    private ProductShopModel productShopModel;
    private FirebaseFirestore firestore;
    private ListenerRegistration productListener;

    private int quantity = 1; // Initial quantity
    private CakeSizeModel selectedCakeSize; // Currently selected cake size

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        // Initialize quantity controls
        minusButton = view.findViewById(R.id.minus);
        quantityTextView = view.findViewById(R.id.quantity);
        plusButton = view.findViewById(R.id.plus);
        btnAddtoCart = view.findViewById(R.id.btnAddtoCart); // Initialize Add to Cart button

        // Set button click listener
        btnAddtoCart.setOnClickListener(v -> showAddToCartDialog());

        minusButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantityAndPrice();
            }
        });

        plusButton.setOnClickListener(v -> {
            quantity++;
            updateQuantityAndPrice();
        });

        if (productShopModel != null) {
            loadProductDetails(productShopModel.getId());
        }

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.cakeSizes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Prepare cake sizes data
        List<CakeSizeModel> cakeSizes = new ArrayList<>();
        cakeSizes.add(new CakeSizeModel("Bento Cake", "Serves 1-2", "P280"));
        cakeSizes.add(new CakeSizeModel("6'' Cake", "Serves 6-8", "P420"));
        cakeSizes.add(new CakeSizeModel("8'' Cake", "Serves 10-12", "P600"));
        cakeSizes.add(new CakeSizeModel("9'' Cake", "Serves 12-16", "P850"));
        cakeSizes.add(new CakeSizeModel("10'' Cake", "Serves 16-20", "P1000"));

        // Set the adapter with the listener
        CakeSizeAdapter adapter = new CakeSizeAdapter(cakeSizes, cakeSize -> {
            selectedCakeSize = cakeSize; // Update selected cake size
            updateQuantityAndPrice(); // Update price based on the new selection
        });
        recyclerView.setAdapter(adapter);

        // Set default selection to Bento Cake
        selectedCakeSize = cakeSizes.get(0); // Bento Cake
        updateQuantityAndPrice(); // Initialize price display

        return view; // Return the inflated view
    }

    private void showAddToCartDialog() {
        // Create a dialog
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.addtocartdialog); // Inflate your dialog layout
        dialog.setCancelable(true);

        // Reference to FirebaseAuth
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        // Automatically add item to the cart when the dialog is shown
        if (userId != null) {
            saveCartItem(userId);
            Toast.makeText(getActivity(), "Item added to cart!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Please log in to add items to the cart.", Toast.LENGTH_SHORT).show();
        }

        Button btnCart = dialog.findViewById(R.id.btnCart);
        btnCart.setOnClickListener(v -> {
            dialog.dismiss(); // Close the dialog
            goToCart(); // Navigate to the cart
        });

        // Use findViewById to get the TextView and set an OnClickListener
        TextView btnContinue = dialog.findViewById(R.id.btnContinue); // Keep it as TextView
        btnContinue.setOnClickListener(v -> {
            dialog.dismiss(); // Close the dialog
            goToShop(); // Navigate back to the shop fragment
        });

        // Show the dialog
        dialog.show();
    }



    private void saveCartItem(String userId) {
        // Create a new AddToCartModel with imageUrl
        String imageUrl = productShopModel.getimageUrl(); // Ensure this accesses the image URL correctly

        AddToCartModel cartItem = new AddToCartModel(
                productShopModel.getId(),
                productShopModel.getName(),
                selectedCakeSize.getSize(),
                quantity,
                productPrice.getText().toString(),
                imageUrl
        );

        // Save to Firestore under the user's document
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(userId).collection("cartItems").add(cartItem)
                .addOnSuccessListener(documentReference -> {
                    // Successfully added to cart
                    Toast.makeText(getActivity(), "Item added to cart!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(getActivity(), "Failed to add item to cart.", Toast.LENGTH_SHORT).show();
                });
    }


    private void updateQuantityAndPrice() {
        quantityTextView.setText(String.valueOf(quantity));
        if (selectedCakeSize != null) {
            int price = Integer.parseInt(selectedCakeSize.getPrice().replace("P", ""));
            int totalPrice = price * quantity;
            productPrice.setText("P" + totalPrice); // Update displayed price
        }
        // Enable or disable the minus button based on quantity
        minusButton.setEnabled(quantity > 1);
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
                    String imageUrl = documentSnapshot.getString("imageUrl"); // Fetch the imageUrl
                    String name = documentSnapshot.getString("name");
                    String description = documentSnapshot.getString("description");
                    String rate = documentSnapshot.getString("rate");
                    String numReviewsStr = documentSnapshot.getString("numreviews");

                    // Populate the views with data
                    Glide.with(Cake_Description.this).load(imageUrl).into(productImage); // Load image with Glide
                    productName.setText(name);
                    productDescription.setText(description);
                    ratePercent.setText(rate);
                    numReviews.setText(numReviewsStr);
                    productShopModel.setimageUrl(imageUrl); // Save image URL to productShopModel
                }
            }
        });
    }


    private void goToCart() {
        Fragment cartFragment = new CartFragment();
        // Replace the current fragment with the CartFragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, cartFragment) // Use your actual container ID
                .addToBackStack(null) // Add to back stack if you want to allow navigation back
                .commit();
    }

    private void goToShop() {
        Fragment shopFragment = new ShopFragment(); // Ensure this matches your actual shop fragment class name
        // Replace the current fragment with the shop fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, shopFragment) // Use your actual container ID
                .addToBackStack(null) // Add to back stack if you want to allow navigation back
                .commit();
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
