package com.ignacio.partykneadsapp;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ignacio.partykneadsapp.adapters.CarouselAdapter;
import com.ignacio.partykneadsapp.adapters.CategoriesAdapter;
import com.ignacio.partykneadsapp.adapters.OrderHistoryAdapter;
import com.ignacio.partykneadsapp.adapters.PopularAdapter;
import com.ignacio.partykneadsapp.databinding.FragmentHomeBinding;
import com.ignacio.partykneadsapp.model.CategoriesModel;
import com.ignacio.partykneadsapp.model.OrderHistoryModel;
import com.ignacio.partykneadsapp.model.PopularModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements NavigationBarView.OnItemSelectedListener {

    private CarouselAdapter adapter;
    private FragmentHomeBinding binding;
    private FirebaseAuth mAuth;
    private TextView txtUser;
    private Button btnLogout;
    private FirebaseUser user;
    private DrawerLayout drawerLayout;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Initialize ViewPager and Adapter first
        int[] images = {
                R.drawable.homepage_slider,
                R.drawable.image2,
                R.drawable.image3,
        };

        adapter = new CarouselAdapter(images);
        binding.viewPager.setAdapter(adapter);

        // Initialize dotsCount after setting up the adapter
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

        // Initialize category, popular, and order history
        setupCategories();
        setupPopularProducts();
        setupOrderHistory();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        btnLogout = view.findViewById(R.id.btnLogout);
        txtUser = view.findViewById(R.id.txtUserFname);
        user = mAuth.getCurrentUser();

        if (user == null) {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_loginFragment_to_homePageFragment);
        } else {
            String email = user.getEmail();
            if (email != null && !email.isEmpty()) {
                String firstName = email.split("@")[0];
                firstName = capitalizeFirstLetter(firstName.split("\\.")[0]);
                txtUser.setText("Hi, " + firstName + "!");
            } else {
                txtUser.setText("Hi, User!");
            }
        }

        btnLogout.setOnClickListener(v -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            FirebaseAuth.getInstance().signOut();
            NavController navController = Navigation.findNavController(requireView());
            if (Objects.equals(currentUser.getEmail(), "sweetkatrinabiancaignacio@gmail.com")) {
                navController.navigate(R.id.action_seller_HomePageFragment_to_loginFragment);
            } else {
                navController.navigate(R.id.action_homePageFragment_to_loginFragment);
            }
        });

        cl = view.findViewById(R.id.clayout);
        cl.setOnClickListener(v -> {
            InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });
    }

    // Set up categories
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
        popularProductList.add(new PopularModel("Product 1", "Description 1", "₱700.00", R.drawable.cake_sample));
        popularProductList.add(new PopularModel("Product 2", "Description 2", "₱800.00", R.drawable.cake_sample));
        popularProductList.add(new PopularModel("Product 3", "Description 3", "₱900.00", R.drawable.cake_sample));

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
        orderHistoryList.add(new OrderHistoryModel("Vanilla Bean Cake", "₱700.00", R.drawable.cake_sample));
        orderHistoryList.add(new OrderHistoryModel("Chocolate Cake", "₱750.00", R.drawable.cake_sample));
        orderHistoryList.add(new OrderHistoryModel("Red Velvet Cake", "₱800.00", R.drawable.cake_sample));

        orderHistoryAdapter = new OrderHistoryAdapter(getActivity(), orderHistoryList);
        orderHistory.setAdapter(orderHistoryAdapter);
        orderHistory.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        orderHistory.setHasFixedSize(true);
        orderHistory.setNestedScrollingEnabled(false);
    }

    // Helper method to capitalize the first letter of the first name
    private String capitalizeFirstLetter(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
