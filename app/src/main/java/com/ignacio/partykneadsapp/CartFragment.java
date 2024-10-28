package com.ignacio.partykneadsapp;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ignacio.partykneadsapp.adapters.CartAdapter;
import com.ignacio.partykneadsapp.model.CartItemModel;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.OnItemSelectedListener {
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItemModel> cartItemList;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private TextView totalPriceTextView; // TextView to display total price
    private CheckBox selectAllCheckBox; // Checkbox to select all items
    ImageView btnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        btnBack = view.findViewById(R.id.btnBack);
        // Initialize Firestore and Auth
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartItemList = new ArrayList<>();

        // Initialize the adapter with listener
        cartAdapter = new CartAdapter(cartItemList, this);
        recyclerView.setAdapter(cartAdapter);

        // Initialize the total price TextView
        totalPriceTextView = view.findViewById(R.id.total);

        // Initialize the Select All checkbox
        selectAllCheckBox = view.findViewById(R.id.selectAllcheckbox);

        selectAllCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cartAdapter.selectAll(isChecked); // Select or deselect all items
            updateTotalPrice(); // Update total price based on selection
        });

        btnBack.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_cartFragment_to_homePageFragment);
        });

        // Initialize the Checkout button
        Button btnCheckOut = view.findViewById(R.id.btncheckOut);
        btnCheckOut.setOnClickListener(v -> proceedToCheckout());

        loadCartItems();

        return view;
    }

    private void loadCartItems() {
        String userId = auth.getCurrentUser().getUid();

        firestore.collection("Users").document(userId).collection("cartItems")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            CartItemModel item = document.toObject(CartItemModel.class);
                            cartItemList.add(item);
                        }

                        cartAdapter.notifyDataSetChanged();
                        updateTotalPrice(); // Update total price based on initial selection state
                    } else {
                        // Handle the error
                        Toast.makeText(getContext(), "Error loading cart items", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onItemSelected() {
        updateTotalPrice(); // Update total price whenever an item's checkbox is checked/unchecked
    }

    private void updateTotalPrice() {
        double totalPrice = 0; // Initialize total price
        List<CartItemModel> selectedItems = cartAdapter.getSelectedItems(); // Get selected items

        for (CartItemModel item : selectedItems) {
            totalPrice += item.getTotalPriceAsDouble(); // Accumulate total price of selected items
        }

        totalPriceTextView.setText("Total: P" + String.format("%.2f", totalPrice)); // Display formatted total price
    }

    private void proceedToCheckout() {
        List<CartItemModel> selectedItems = cartAdapter.getSelectedItems();

        if (selectedItems.isEmpty()) {
            Toast.makeText(getContext(), "Please select at least one item to proceed.", Toast.LENGTH_SHORT).show();
        } else {
            // Log selected items to check
            for (CartItemModel item : selectedItems) {
                Log.d("SelectedItem", "Item: " + item.getProductName() + ", Quantity: " + item.getQuantity());
            }

            // Create a Bundle to pass the selected items to the CheckoutFragment
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("selectedItems", (ArrayList<? extends Parcelable>) selectedItems);
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_cartFragment_to_checkoutFragment, bundle);
        }
    }
}
