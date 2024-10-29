package com.ignacio.partykneadsapp;

import android.animation.LayoutTransition;
import android.app.AlertDialog;
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
import android.widget.Button; // Import Button
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ignacio.partykneadsapp.adapters.CartAdapter;
import com.ignacio.partykneadsapp.adapters.CheckoutAdapter;
import com.ignacio.partykneadsapp.model.CartItemModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckoutFragment extends Fragment {
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItemModel> selectedItems;
    private TextView itemTotalTextView;
    private TextView totalCostTextView;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser cUser;
    CheckoutAdapter coutAdapter;
    ImageView btnBack;
    Button btncheckout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        // Initialize Firestore and Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        cUser = mAuth.getCurrentUser();
        btnBack = view.findViewById(R.id.btnBack);

        // Initialize UI elements
        recyclerView = view.findViewById(R.id.recyclerViewCart);
        itemTotalTextView = view.findViewById(R.id.itemTotal);
        totalCostTextView = view.findViewById(R.id.totalCost);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Retrieve selected items from arguments if provided
        selectedItems = getArguments() != null ? getArguments().<CartItemModel>getParcelableArrayList("selectedItems") : new ArrayList<>();

        // Log the received selected items for debugging
        if (selectedItems != null && !selectedItems.isEmpty()) {
            for (CartItemModel item : selectedItems) {
                Log.d("CheckoutFragment", "Received Item: " + item.getProductName() + ", Quantity: " + item.getQuantity());
            }
        } else {
            Log.d("CheckoutFragment", "No selected items received from CartFragment.");
        }

        // Set up adapter with selected items list
        coutAdapter = new CheckoutAdapter(selectedItems);
        recyclerView.setAdapter(coutAdapter);

        btnBack.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_checkoutFragment_to_cartFragment);
        });

        // Initialize the Checkout button
        Button btnCheckout = view.findViewById(R.id.btncheckout);
        btnCheckout.setOnClickListener(v -> {
            saveOrderToDatabase();
            showSuccessDialog();
        });

        // Update totals based on selectedItems
        updateTotals();

        return view;
    }

    private void showSuccessDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.success_dialog, null);
        TextView btnContinue = dialogView.findViewById(R.id.btnContinue);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        btnContinue.setOnClickListener(v -> {
            alertDialog.dismiss();
            Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();

            // Navigate to HomeFragment
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_checkoutFragment_to_homePageFragment); // Adjust to your actual action ID
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
        orderData.put("status", "placed"); // Default status

        // Get the current user's email
        if (cUser != null) {
            orderData.put("userEmail", cUser.getEmail()); // Add user email to order data
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
            itemData.put("totalPrice", item.getTotalPrice()); // Ensure this method returns the price as a String
            itemData.put("imageUrl", item.getImageUrl()); // Assuming you have this method
            itemData.put("cakeSize", item.getCakeSize()); // Assuming you have this method

            itemsList.add(itemData);
        }

        orderData.put("items", itemsList); // Add items to the order data

        // Find the document containing the admin email
        String adminEmail = "sweetkatrinabiancaignacio@gmail.com";
        db.collection("Users")
                .whereEqualTo("email", adminEmail) // Adjust based on your field name for email
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Assuming there's only one document with this email
                        String userDocId = task.getResult().getDocuments().get(0).getId();

                        // Save order under the found document's ID
                        db.collection("Users")
                                .document(userDocId)
                                .collection("Orders")
                                .add(orderData)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(getActivity(), "Order placed successfully!", Toast.LENGTH_SHORT).show();
                                    Log.d("CheckoutFragment", "Order successfully written with ID: " + documentReference.getId());

                                    // Now delete the checked-out items from the current user's cart
                                    deleteCheckedOutItems();

                                })
                                .addOnFailureListener(e -> {
                                    Log.w("CheckoutFragment", "Error adding document", e);
                                });
                    } else {
                        Log.w("CheckoutFragment", "No user found with the email: " + adminEmail);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w("CheckoutFragment", "Error fetching user document", e);
                });
    }


    private void deleteCheckedOutItems() {
        // Get the current user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Get the current user's document ID
            String currentUserId = currentUser.getUid();

            // Reference to the cart items collection for the current user
            db.collection("Users")
                    .document(currentUserId)
                    .collection("cartItems")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            // Loop through the cart items and delete the ones that match the checked-out product IDs
                            for (CartItemModel item : selectedItems) {
                                String productIdToDelete = item.getProductId();

                                // Check if the product ID matches any in the cart
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    CartItemModel cartItem = document.toObject(CartItemModel.class);
                                    if (cartItem.getProductId().equals(productIdToDelete)) {
                                        // Delete the matching cart item
                                        document.getReference().delete()
                                                .addOnSuccessListener(aVoid -> Log.d("CheckoutFragment", "Cart item with ID " + productIdToDelete + " deleted successfully"))
                                                .addOnFailureListener(e -> Log.w("CheckoutFragment", "Error deleting cart item", e));
                                    }
                                }
                            }
                        } else {
                            Log.w("CheckoutFragment", "No cart items found for the current user.");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.w("CheckoutFragment", "Error fetching cart items", e);
                    });
        } else {
            Log.w("CheckoutFragment", "No current user is logged in.");
        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
