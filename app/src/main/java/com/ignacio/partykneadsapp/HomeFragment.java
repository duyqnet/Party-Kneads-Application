package com.ignacio.partykneadsapp;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.ignacio.partykneadsapp.adapters.CarouselAdapter;
import com.ignacio.partykneadsapp.adapters.CategoriesAdapter;
import com.ignacio.partykneadsapp.adapters.OrderHistoryAdapter;
import com.ignacio.partykneadsapp.adapters.PopularAdapter;
import com.ignacio.partykneadsapp.databinding.FragmentHomeBinding;
import com.ignacio.partykneadsapp.model.CategoriesModel;
import com.ignacio.partykneadsapp.model.OrderHistoryModel;
import com.ignacio.partykneadsapp.model.PopularModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment implements NavigationBarView.OnItemSelectedListener {

    private CarouselAdapter adapter;
    private FragmentHomeBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private TextView txtUser;
    private Button btnLogout;
    private FirebaseUser user;
    private RecyclerView categories;
    private RecyclerView popular;
    private RecyclerView orderHistory;

    private List<CategoriesModel> categoriesModelList;
    private CategoriesAdapter categoriesAdapter;
    private List<PopularModel> popularProductList;
    private PopularAdapter popularAdapter;
    private List<OrderHistoryModel> orderHistoryList;
    private OrderHistoryAdapter orderHistoryAdapter;

    private int dotsCount;
    private ImageView[] dots;
    private LinearLayout cl;

    // Location-related variables
    private FusedLocationProviderClient fusedLocationClient;
    private TextView locationTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance(); // Initialize Firestore
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false; // Implement navigation item selection if needed
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Initialize ViewPager and Adapter
        int[] images = {
                R.drawable.homepage_slider,
                R.drawable.image2,
                R.drawable.image3,
        };

        adapter = new CarouselAdapter(images);
        binding.viewPager.setAdapter(adapter);
        setupDotsIndicator();

        // Initialize category, popular, and order history
        setupCategories();
        setupPopularProducts();
        setupOrderHistory();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnLogout = view.findViewById(R.id.btnLogout);
        txtUser = view.findViewById(R.id.txtUserFname);
        user = mAuth.getCurrentUser();

        if (user == null) {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_loginFragment_to_homePageFragment);
        } else {
            fetchUserFirstName(user.getUid());
        }

        binding.btnCart.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_cont);
            navController.navigate(R.id.action_homePageFragment_to_cartFragment);
        });

        cl = view.findViewById(R.id.clayout);
        cl.setOnClickListener(v -> hideKeyboard(view));

        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        locationTextView = view.findViewById(R.id.location); // Ensure this ID matches your XML
        requestLocationPermissions();
    }

    private void setupDotsIndicator() {
        dotsCount = adapter.getItemCount();
        dots = new ImageView[dotsCount];

        // Add dots to the LinearLayout
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.non_active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);
            binding.dotsIndicator.addView(dots[i], params);
        }

        // Set the first dot as active
        if (dots.length > 0) {
            dots[0].setImageDrawable(getResources().getDrawable(R.drawable.active_dot));
        }

        // Set up a page change listener to update the dots
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.non_active_dot));
                }
                dots[position].setImageDrawable(getResources().getDrawable(R.drawable.active_dot));
            }
        });
    }

    private void fetchUserFirstName(String userId) {
        DocumentReference userDocRef = firestore.collection("Users").document(userId);
        userDocRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String firstName = documentSnapshot.getString("First Name");
                        txtUser.setText("Hi, " + capitalizeFirstLetter(firstName) + "!");
                    } else {
                        txtUser.setText("Hi, No Document!");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("HomeFragment", "Error fetching user data", e);
                    txtUser.setText("Hi, Error Fetching!");
                });
    }

    private void requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            locationTextView.setText("Location permission denied.");
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissions();
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        displayLocation(location);
                    } else {
                        locationTextView.setText("Unable to get location");
                    }
                })
                .addOnFailureListener(requireActivity(), e -> {
                    locationTextView.setText("Error getting location: " + e.getMessage());
                });
    }

    private void displayLocation(Location location) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder addressDetails = new StringBuilder();

                // Append address details
                if (address.getLocality() != null) {
                    addressDetails.append(address.getLocality()).append(", ");
                }
                if (address.getSubAdminArea() != null) {
                    addressDetails.append(address.getSubAdminArea()).append(", ");
                }

                // Update UI and save location
                locationTextView.setText(addressDetails.toString());
                saveLocationToFirestore(addressDetails.toString());
            } else {
                locationTextView.setText("Unable to get location");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveLocationToFirestore(String location) {
        user = mAuth.getCurrentUser();
        if (user != null) {
            DocumentReference userDocRef = firestore.collection("Users").document(user.getUid());

            // Create a reference to the "Locations" sub-collection
            CollectionReference locationsCollection = userDocRef.collection("Locations");

            // Check if the Locations sub-collection is empty
            locationsCollection.get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.isEmpty()) {
                            // Add a new document with a unique ID for the location
                            Map<String, Object> locationData = new HashMap<>();
                            locationData.put("location", location);
                            locationData.put("status", "Active"); // Optional: Add a timestamp

                            locationsCollection.add(locationData)
                                    .addOnSuccessListener(aVoid -> Log.d("HomeFragment", "Location saved successfully."))
                                    .addOnFailureListener(e -> Log.e("HomeFragment", "Error saving location", e));
                        } else {
                            Log.d("HomeFragment", "Location not saved; Locations collection is not empty.");
                        }
                    })
                    .addOnFailureListener(e -> Log.e("HomeFragment", "Error checking existing locations", e));
        }
    }




    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setupCategories() {
        categories = binding.categories;
        categoriesModelList = new ArrayList<>();
        categoriesModelList.add(new CategoriesModel(R.drawable.cake, "Cakes"));
        categoriesModelList.add(new CategoriesModel(R.drawable.breads, "Breads"));
        categoriesModelList.add(new CategoriesModel(R.drawable.desserts, "Desserts"));
        categoriesModelList.add(new CategoriesModel(R.drawable.customized, "Customize"));
        categoriesModelList.add(new CategoriesModel(R.drawable.balloons, "Balloons"));
        categoriesModelList.add(new CategoriesModel(R.drawable.candles, "Candles"));
        categoriesModelList.add(new CategoriesModel(R.drawable.party_hats, "Party Hats"));
        categoriesModelList.add(new CategoriesModel(R.drawable.banners, "Banners"));
        categoriesModelList.add(new CategoriesModel(R.drawable.confetti, "Confetti"));

        categoriesAdapter = new CategoriesAdapter(getActivity(), categoriesModelList);
        categories.setAdapter(categoriesAdapter);
        categories.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        categories.setHasFixedSize(true);
        categories.setNestedScrollingEnabled(false);
    }

    // Set up popular products
    private void setupPopularProducts() {
        popular = binding.popular;
        popularProductList = new ArrayList<>();
        popularProductList.add(new PopularModel("Strawberry Bean", "5.0 (100)", "₱700.00",  R.drawable.strawberry, "20 sold"));
        popularProductList.add(new PopularModel("Matcha", "5.0 (100)", "₱800.00", R.drawable.matcha, "23 sold"));
        popularProductList.add(new PopularModel("Strawberry Shortcake 3", "5.0 (100)", "₱900.00", R.drawable.shortcake, "15 sold"));

        popularAdapter = new PopularAdapter(getActivity(), popularProductList);
        popular.setAdapter(popularAdapter);
        popular.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        popular.setHasFixedSize(true);
        popular.setNestedScrollingEnabled(false);
    }

    // Set up order history
    private void setupOrderHistory() {
        orderHistory = binding.orderHistory;
        orderHistoryList = new ArrayList<>();
        orderHistoryList.add(new OrderHistoryModel("Strawberry Bean", "5.0 (100)", "₱700.00",  R.drawable.strawberry, "20 sold"));
        orderHistoryList.add(new OrderHistoryModel("Matcha", "5.0 (100)", "₱800.00", R.drawable.matcha, "23 sold"));
        orderHistoryList.add(new OrderHistoryModel("Strawberry Shortcake 3", "5.0 (100)", "₱900.00", R.drawable.shortcake, "15 sold"));

        orderHistoryAdapter = new OrderHistoryAdapter(getActivity(), orderHistoryList);
        orderHistory.setAdapter(orderHistoryAdapter);
        orderHistory.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        orderHistory.setHasFixedSize(true);
        orderHistory.setNestedScrollingEnabled(false);
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
