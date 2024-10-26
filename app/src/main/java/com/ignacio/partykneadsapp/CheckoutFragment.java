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
import com.ignacio.partykneadsapp.adapters.CartAdapter;
import com.ignacio.partykneadsapp.model.CartItemModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CheckoutFragment extends Fragment {
    private RecyclerView recyclerView; // Declare RecyclerView
    private CartAdapter cartAdapter; // Declare CartAdapter
    private List<CartItemModel> selectedItems; // Declare the list of selected items
    private TextView itemTotalTextView; // TextView for item total
    private TextView totalCostTextView; // TextView for total cost
    private FirebaseFirestore db; // Firestore instance
    private ListenerRegistration registration; // Listener registration

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        recyclerView = view.findViewById(R.id.recyclerViewCart); // Use recyclerViewCart
        itemTotalTextView = view.findViewById(R.id.itemTotal); // TextView for item total
        totalCostTextView = view.findViewById(R.id.totalCost); // TextView for total cost
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Retrieve selected items from arguments
        selectedItems = getArguments().getParcelableArrayList("selectedItems");

        // Set up adapter
        cartAdapter = new CartAdapter(selectedItems);
        recyclerView.setAdapter(cartAdapter);

        // Fetch data from Firestore
        fetchDataFromFirestore();

        return view;
    }

    private void fetchDataFromFirestore() {
        String userId = "Users"; // Replace with actual user ID if dynamic

        db.collection("users").document(userId).collection("cartItems")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<CartItemModel> items = new ArrayList<>();
                        double itemTotal = 0;
                        double discount = 0; // Set your discount value here

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CartItemModel item = document.toObject(CartItemModel.class);
                            // Only add selected items
                            if (item.isSelected()) {
                                items.add(item);
                                itemTotal += item.getTotalPriceAsDouble(); // Assuming getTotalPriceAsDouble() is implemented correctly
                            }
                        }

                        // Log the number of items added
                        Log.d("CheckoutFragment", "Number of selected items: " + items.size());

                        // Calculate totalCost as itemTotal minus discount
                        double totalCost = itemTotal - discount;

                        // Update selected items and totals
                        selectedItems.clear();
                        selectedItems.addAll(items);
                        cartAdapter.notifyDataSetChanged();
                        updateTotals(itemTotal, totalCost);
                    } else {
                        Log.e("CheckoutFragment", "Error fetching data: ", task.getException());
                    }
                });
    }



    private void updateTotals(double itemTotal, double totalCost) {
        itemTotalTextView.setText("P" + String.format("%.2f", itemTotal)); // Display item total
        totalCostTextView.setText("P" + String.format("%.2f", totalCost)); // Display total cost
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (registration != null) {
            registration.remove();
        }
    }
}
