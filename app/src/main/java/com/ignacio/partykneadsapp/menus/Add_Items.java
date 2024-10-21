package com.ignacio.partykneadsapp.menus;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ignacio.partykneadsapp.R;
import com.ignacio.partykneadsapp.databinding.FragmentAddItemsBinding;

import java.util.HashMap;
import java.util.Map;

public class Add_Items extends Fragment {

    FragmentAddItemsBinding binding;
    ConstraintLayout cl;
    final int GALLERY_REQ_CODE = 1000;
    Uri selectedImageUri;

    // Counter for unique product IDs
    private int productCounter = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddItemsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cl = view.findViewById(R.id.clayout);
        cl.setOnClickListener(v -> hideKeyboard(v));

        binding.btnUpload.setOnClickListener(v -> { uploadImage(); });
        binding.btnAddItem.setOnClickListener(v -> {
            String productId = "product" + productCounter++; // Generate unique ID
            if (selectedImageUri != null) {
                uploadImageToFirebase(selectedImageUri, productId);
            } else {
                // If no image selected, directly save product info with the unique ID
                saveProductDataToFirebase(null, productId);
            }
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void uploadImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQ_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQ_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();

            // Display the selected image in an ImageView
            binding.itemImg.setImageURI(selectedImageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri, String productId) {
        // Create a reference to Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance("gs://party-kneads-firebase.appspot.com").getReference()
                .child("product_images").child(System.currentTimeMillis() + ".jpg");

        // Upload the file
        storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Get the download URL of the image
                String imageUrl = uri.toString();

                // Save product info and image URL to Firestore with the unique product ID
                saveProductDataToFirebase(imageUrl, productId);
            });
        }).addOnFailureListener(e -> {
            // Handle any errors
            Toast.makeText(getContext(), "Error uploading image", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveProductDataToFirebase(@Nullable String imageUrl, String productId) {
        // Retrieve the entered text from input fields
        String productName = binding.productName.getText().toString();
        String productPrice = binding.productPrice.getText().toString();
        String productDescription = binding.description.getText().toString();

        // Prepare the data to be saved
        Map<String, Object> product = new HashMap<>();
        product.put("id", productId); // Add the unique product ID
        product.put("name", productName);
        product.put("price", productPrice);
        product.put("description", productDescription);
        if (imageUrl != null) {
            product.put("imageUrl", imageUrl);
        }

        // Save to Firestore with custom document ID
        FirebaseFirestore db = FirebaseFirestore.getInstance(); // No arguments needed
        db.collection("products").document(productId).set(product)
                .addOnSuccessListener(aVoid -> {
                    // Successfully added the product
                    Toast.makeText(getActivity(), "Successfully added item", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore Error", "Error adding item", e);
                    Toast.makeText(getContext(), "Error adding item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}
