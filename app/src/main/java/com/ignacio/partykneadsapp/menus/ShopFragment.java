package com.ignacio.partykneadsapp.menus;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.ignacio.partykneadsapp.R;
import com.ignacio.partykneadsapp.adapters.CategoriesAdapter;
import com.ignacio.partykneadsapp.adapters.ProductShopAdapter;
import com.ignacio.partykneadsapp.model.CategoriesModel;
import com.ignacio.partykneadsapp.model.ProductShopModel;
import com.ignacio.partykneadsapp.databinding.FragmentShopBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {

    private LinearLayout cl;
    private RecyclerView categories;
    private List<CategoriesModel> categoriesModelList;
    private CategoriesAdapter categoriesAdapter;
    private RecyclerView recyclerView;
    private ProductShopAdapter productShopAdapter;
    private List<ProductShopModel> productList;
    private FirebaseFirestore db;
    private FragmentShopBinding binding;
    CollectionReference productsRef;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShopBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        productsRef = db.collection("products");

        // Use GridLayoutManager for 2 items in a row
        RecyclerView productsRecyclerView = binding.recyclerView;
        productsRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2)); // 2 columns

        // Initialize the adapter with an empty list and the fragment's context
        productShopAdapter = new ProductShopAdapter(requireActivity(), new ArrayList<>());
        productsRecyclerView.setAdapter(productShopAdapter);

        // Setup categories
        setupCategories();

        // Fetch products
        fetchProducts();

        // Set up click listener to hide the keyboard when tapping outside
        cl = view.findViewById(R.id.clayout);
        cl.setOnClickListener(v -> {
            InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });
    }

    private void setupCategories() {
        categories = binding.categories;
        categoriesModelList = new ArrayList<>();

        // Add categories to the list (Images should exist in drawable folder)
        categoriesModelList.add(new CategoriesModel(R.drawable.cake, "Cakes"));
        categoriesModelList.add(new CategoriesModel(R.drawable.breads, "Breads"));
        categoriesModelList.add(new CategoriesModel(R.drawable.desserts, "Desserts"));
        categoriesModelList.add(new CategoriesModel(R.drawable.customized, "Customize"));
        categoriesModelList.add(new CategoriesModel(R.drawable.balloons, "Balloons"));
        categoriesModelList.add(new CategoriesModel(R.drawable.candles, "Candles"));
        categoriesModelList.add(new CategoriesModel(R.drawable.party_hats, "Party Hats"));
        categoriesModelList.add(new CategoriesModel(R.drawable.banners, "Banners"));
        categoriesModelList.add(new CategoriesModel(R.drawable.confetti, "Confetti"));

        // Initialize adapter and layout manager for categories
        categoriesAdapter = new CategoriesAdapter(requireActivity(), categoriesModelList);
        categories.setAdapter(categoriesAdapter);
        categories.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false));
        categories.setHasFixedSize(true);
        categories.setNestedScrollingEnabled(false);
    }

    private void fetchProducts() {
        productsRef
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<ProductShopModel> productsList = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Retrieve the document ID
                            String id = document.getId(); // Get the document ID

                            // Retrieve the necessary fields from the document
                            String imageUrl = document.getString("imageUrl");
                            String name = document.getString("name");
                            String price = document.getString("price"); // Price as String
                            String description = document.getString("description"); // Get description
                            String rate = document.getString("rate"); // Get rate
                            String numreviews = document.getString("numreviews"); // Get number of reviews

                            // Create a ProductShopModel
                            productsList.add(new ProductShopModel(id, imageUrl, name, price, description, rate, numreviews));
                        }

                        // Update the adapter with the fetched data
                        productShopAdapter.updateData(productsList);
                    } else {
                        Log.d("Firestore", "Error getting products: ", task.getException());
                    }
                });
    }
}