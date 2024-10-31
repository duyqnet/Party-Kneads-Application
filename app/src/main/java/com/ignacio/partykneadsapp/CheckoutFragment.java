package com.ignacio.partykneadsapp;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ignacio.partykneadsapp.adapters.CheckoutAdapter;
import com.ignacio.partykneadsapp.adapters.LocationAdapter;
import com.ignacio.partykneadsapp.model.CartItemModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckoutFragment extends Fragment {
    private RecyclerView recyclerView;
    private CheckoutAdapter coutAdapter;
    private List<CartItemModel> selectedItems;
    private TextView itemTotalTextView;
    private TextView totalCostTextView;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser cUser;
    private ImageView btnBack;
    private RecyclerView locationRecyclerView;
    private LocationAdapter locationAdapter;
    private List<String> activeLocations;
    private TextView txtUserName;
    private CustomGridLayoutManager layoutManager;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        // Initialize Firestore and Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        cUser = mAuth.getCurrentUser();

        // Initialize UI elements
        btnBack = view.findViewById(R.id.btnBack);
        recyclerView = view.findViewById(R.id.recyclerViewCart);
        itemTotalTextView = view.findViewById(R.id.itemTotal);
        totalCostTextView = view.findViewById(R.id.totalCost);
        txtUserName = view.findViewById(R.id.txtUserName);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Retrieve selected items from arguments if provided
        selectedItems = getArguments() != null ? getArguments().getParcelableArrayList("selectedItems") : new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        // Initialize CustomLinearLayoutManager and set it to the RecyclerView
//        layoutManager = new CustomGridLayoutManager(getContext()) {
//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
//        };

        // Log received selected items for debugging
        if (selectedItems != null && !selectedItems.isEmpty()) {
            for (CartItemModel item : selectedItems) {
                Log.d("CheckoutFragment", "Received Item: " + item.getProductName() + ", Quantity: " + item.getQuantity());
            }
        } else {
            Log.d("CheckoutFragment", "No selected items received from CartFragment.");
        }

        // Set up the adapter with selected items
        coutAdapter = new CheckoutAdapter(selectedItems);
        recyclerView.setAdapter(coutAdapter);

        // Initialize RecyclerView for locations
        locationRecyclerView = view.findViewById(R.id.locationRecycler);
        activeLocations = new ArrayList<>();
        locationAdapter = new LocationAdapter(activeLocations, ""); // Initialize with an empty userName
        locationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        locationRecyclerView.setAdapter(locationAdapter);

        // Fetch user name and locations
        fetchUserNameAndLocations();

        // Back button listener
        btnBack.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_checkoutFragment_to_cartFragment);
        });

        // Checkout button listener
        Button btnCheckout = view.findViewById(R.id.btncheckout);
        btnCheckout.setOnClickListener(v -> {
            saveOrderToDatabase();
            showSuccessDialog();
        });

        // Update totals based on selectedItems
        updateTotals();
        return view;
    }

    private void fetchUserNameAndLocations() {
        if (cUser != null) {
            String userId = cUser.getUid();
            db.collection("Users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                String firstName = documentSnapshot.getString("First Name");
                String lastName = documentSnapshot.getString("Last Name");

                // Combine first name and last name to form the user's full name
                String userName = firstName + " " + lastName;

                // Update the location adapter with user's name
                locationAdapter.setUserName(userName);
                locationAdapter.notifyDataSetChanged();

                // Fetch active locations after getting the user's name
                fetchActiveLocations();
            }).addOnFailureListener(e -> {
                Log.w("CheckoutFragment", "Error fetching user name", e);
            });
        }
    }

    private void fetchActiveLocations() {
        if (cUser != null) {
            String userId = cUser.getUid();
            db.collection("Users").document(userId).collection("Locations")
                    .whereEqualTo("status", "Active")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String location = document.getString("location");
                                if (location != null) {
                                    activeLocations.add(location);
                                }
                            }
                            locationAdapter.notifyDataSetChanged(); // Notify adapter about data change
                        } else {
                            Log.w("CheckoutFragment", "Error getting active locations.", task.getException());
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.w("CheckoutFragment", "Error fetching active locations", e);
                    });
        }
    }

    private void showSuccessDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.success_dialog, null);
        TextView btnContinue = dialogView.findViewById(R.id.btnContinue);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        // Set the dialog background to transparent
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        btnContinue.setOnClickListener(v -> {
            alertDialog.dismiss();
            Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_checkoutFragment_to_homePageFragment);
        });

        alertDialog.show();
    }

    private void updateTotals() {
        double itemTotal = 0;
        double discount = 0; // Set discount value if applicable

        for (CartItemModel item : selectedItems) {
            itemTotal += item.getTotalPriceAsDouble(); // Assuming getTotalPriceAsDouble() is implemented correctly
        }

        double totalCost = itemTotal - discount;

        // Update TextViews
        itemTotalTextView.setText("₱" + String.format("%.2f", itemTotal));
        totalCostTextView.setText("₱" + String.format("%.2f", totalCost));

        // Toggle TextView visibility
        toggleTextViewVisibility(!selectedItems.isEmpty());
    }

    private void toggleTextViewVisibility(boolean hasItems) {
        itemTotalTextView.setVisibility(hasItems ? View.VISIBLE : View.GONE);
        totalCostTextView.setVisibility(hasItems ? View.VISIBLE : View.GONE);
    }

    private void saveOrderToDatabase() {
        // Create a map to store order details
        HashMap<String, Object> orderData = new HashMap<>();
        orderData.put("status", "placed");

        // Get the current user's email
        if (cUser != null) {
            orderData.put("userEmail", cUser.getEmail());
        } else {
            Log.w("CheckoutFragment", "Current user is null. Unable to retrieve email.");
        }

        // List to hold item details
        List<HashMap<String, Object>> itemsList = new ArrayList<>();

        for (CartItemModel item : selectedItems) {
            HashMap<String, Object> itemData = new HashMap<>();
            itemData.put("productId", item.getProductId());
            itemData.put("productName", item.getProductName());
            itemData.put("quantity", item.getQuantity());
            itemData.put("totalPrice", item.getTotalPrice());
            itemData.put("imageUrl", item.getImageUrl());
            itemData.put("cakeSize", item.getCakeSize());

            itemsList.add(itemData);
        }

        orderData.put("items", itemsList);

        // Save order under the admin document's ID
        String adminEmail = "sweetkatrinabiancaignacio@gmail.com";
        db.collection("Users")
                .whereEqualTo("email", adminEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        String userDocId = task.getResult().getDocuments().get(0).getId();

                        db.collection("Users").document(userDocId).collection("Orders")
                                .add(orderData)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(getActivity(), "Order placed successfully!", Toast.LENGTH_SHORT).show();
                                    Log.d("CheckoutFragment", "Order successfully written with ID: " + documentReference.getId());
                                })
                                .addOnFailureListener(e -> Log.w("CheckoutFragment", "Error adding document", e));
                    } else {
                        Log.w("CheckoutFragment", "No user found with the specified email.");
                    }
                });
    }
}
