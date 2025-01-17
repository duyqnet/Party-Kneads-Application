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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
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

        binding.btnUpload.setOnClickListener(v -> uploadImage());

        // Set up the AutoCompleteTextView for categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.Categories, android.R.layout.simple_dropdown_item_1line);
        binding.categgories.setAdapter(adapter);

        binding.btnAddItem.setOnClickListener(v -> {
            String productName = binding.productName.getText().toString().trim();
            String productPrice = binding.productPrice.getText().toString().trim();
            String productDescription = binding.description.getText().toString().trim();
            String productCategory = binding.categgories.getText().toString().trim();

            // Check if any of the fields are empty
            if (productName.isEmpty() || productPrice.isEmpty() || productDescription.isEmpty() || productCategory.isEmpty()) {
                Toast.makeText(getContext(), "All fields must be filled up!", Toast.LENGTH_SHORT).show();
            } else if (selectedImageUri == null) {
                // If no image is selected, show a warning
                Toast.makeText(getContext(), "An image must be uploaded!", Toast.LENGTH_SHORT).show();
            } else {
                String productId = String.valueOf(System.currentTimeMillis()); // Use current time as unique ID
                uploadImageToFirebase(selectedImageUri, productId);
            }
        });

        binding.btnBack.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_add_Items_to_seller_HomePageFragment);
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

                // Save product info and image URL to Firestore
                saveProductDataToFirebase(imageUrl);
            });
        }).addOnFailureListener(e -> {
            // Handle any errors
            Toast.makeText(getContext(), "Error uploading image", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveProductDataToFirebase(@Nullable String imageUrl) {
        // Retrieve the entered text from input fields
        String productName = binding.productName.getText().toString();
        String productPrice = binding.productPrice.getText().toString();
        String productDescription = binding.description.getText().toString();
        String productCategory = binding.categgories.getText().toString();

        // Prepare the data to be saved
        Map<String, Object> product = new HashMap<>();
        product.put("name", productName);
        product.put("price", productPrice);
        product.put("description", productDescription);
        product.put("categories", productCategory);
        if (imageUrl != null) {
            product.put("imageUrl", imageUrl);
        }

        // Save to Firestore with a new document ID
        FirebaseFirestore db = FirebaseFirestore.getInstance(); // No arguments needed
        db.collection("products").add(product) // Using add() instead of document(productId)
                .addOnSuccessListener(documentReference -> {
                    // Successfully added the product
                    Toast.makeText(getActivity(), "Successfully added item", Toast.LENGTH_SHORT).show();
                    // Reset the form fields
                    resetForm();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore Error", "Error adding item", e);
                    Toast.makeText(getContext(), "Error adding item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void resetForm() {
        binding.productName.setText("");
        binding.productPrice.setText("");
        binding.description.setText("");
        binding.categgories.setText("");
        binding.itemImg.setImageURI(null);
        selectedImageUri = null;
    }
}