package com.ignacio.partykneadsapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ignacio.partykneadsapp.adapters.CartAdapter;
import com.ignacio.partykneadsapp.model.CartItemModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CheckoutFragment extends Fragment {
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItemModel> selectedItems;
    private TextView itemTotalTextView;
    private TextView totalCostTextView;
    private FirebaseFirestore db;
    private ListenerRegistration registration;
    private FirebaseAuth mAuth;
    private FirebaseUser cUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        // Initialize Firestore and Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        cUser = mAuth.getCurrentUser();

        // Initialize UI elements
        recyclerView = view.findViewById(R.id.recyclerViewCart);
        itemTotalTextView = view.findViewById(R.id.itemTotal);
        totalCostTextView = view.findViewById(R.id.totalCost);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Retrieve selected items from arguments if provided
        selectedItems = getArguments() != null ? getArguments().<CartItemModel>getParcelableArrayList("selectedItems") : new ArrayList<>();

        // Set up adapter with selected items list
        cartAdapter = new CartAdapter(selectedItems);
        recyclerView.setAdapter(cartAdapter);

        // Fetch data from Firestore
        fetchDataFromFirestore();

        return view;
    }

    private void fetchDataFromFirestore() {
        if (cUser == null) {
            Log.e("CheckoutFragment", "User not logged in.");
            return;
        }

        String userId = cUser.getUid();

        db.collection("Users").document(userId).collection("cartItems")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<CartItemModel> items = new ArrayList<>();
                        double itemTotal = 0;
                        double discount = 0; // Set discount value if applicable

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CartItemModel item = document.toObject(CartItemModel.class);
                            // Only add selected items
                            if (item.isSelected()) {
                                items.add(item);
                                itemTotal += item.getTotalPriceAsDouble(); // Assuming getTotalPriceAsDouble() is implemented correctly
                            }
                        }

                        double totalCost = itemTotal - discount;

                        // Update selected items and totals in the adapter
                        selectedItems.clear();
                        selectedItems.addAll(items);
                        cartAdapter.notifyDataSetChanged();

                        // Update TextViews and set visibility
                        updateTotals(itemTotal, totalCost);
                        toggleTextViewVisibility(!items.isEmpty());
                    } else {
                        Log.e("CheckoutFragment", "Error fetching data: ", task.getException());
                    }
                });
    }

    private void updateTotals(double itemTotal, double totalCost) {
        itemTotalTextView.setText("P" + String.format("%.2f", itemTotal)); // Display item total
        totalCostTextView.setText("P" + String.format("%.2f", totalCost)); // Display total cost
    }

    private void toggleTextViewVisibility(boolean hasItems) {
        if (hasItems) {
            itemTotalTextView.setVisibility(View.VISIBLE);
            totalCostTextView.setVisibility(View.VISIBLE);
        } else {
            itemTotalTextView.setVisibility(View.GONE);
            totalCostTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (registration != null) {
            registration.remove();
        }
    }
}
