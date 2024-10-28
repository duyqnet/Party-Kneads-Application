package com.ignacio.partykneadsapp;

import android.os.Bundle;
import android.os.Parcelable;
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

public class CartFragment extends Fragment {
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
        cartAdapter = new CartAdapter(cartItemList);
        recyclerView.setAdapter(cartAdapter);

        // Initialize the total price TextView
        totalPriceTextView = view.findViewById(R.id.total);

        // Initialize the Select All checkbox
        selectAllCheckBox = view.findViewById(R.id.selectAllcheckbox);

        selectAllCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cartAdapter.selectAll(isChecked); // Select or deselect all items
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
                        double totalPrice = 0; // Initialize total price

                        for (DocumentSnapshot document : task.getResult()) {
                            CartItemModel item = document.toObject(CartItemModel.class);
                            cartItemList.add(item);
                            totalPrice += item.getTotalPriceAsDouble(); // Accumulate total price
                        }

                        cartAdapter.notifyDataSetChanged();
                        updateTotalPrice(totalPrice); // Update the total price TextView
                    } else {
                        // Handle the error
                        Toast.makeText(getContext(), "Error loading cart items", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateTotalPrice(double totalPrice) {
        totalPriceTextView.setText("Total: P" + String.format("%.2f", totalPrice)); // Display formatted total price
    }

    private void proceedToCheckout() {
        List<CartItemModel> selectedItems = cartAdapter.getSelectedItems();

        if (selectedItems.isEmpty()) {
            Toast.makeText(getContext(), "Please select at least one item to proceed.", Toast.LENGTH_SHORT).show();
        } else {
            // Create a Bundle to pass the selected items
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("selectedItems", new ArrayList<Parcelable>(selectedItems));

            // Navigate to CheckoutFragment with selected items
            CheckoutFragment checkoutFragment = new CheckoutFragment();
            checkoutFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, checkoutFragment) // Adjust the container ID
                    .addToBackStack(null)
                    .commit();
        }
    }
}
