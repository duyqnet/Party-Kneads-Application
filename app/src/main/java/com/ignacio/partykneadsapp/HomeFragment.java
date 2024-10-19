package com.ignacio.partykneadsapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.ignacio.partykneadsapp.model.OrderHistoryModel; // Import the OrderHistoryModel class
import com.ignacio.partykneadsapp.model.PopularModel;

import java.util.ArrayList;
import java.util.List;

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

    private List<OrderHistoryModel> orderHistoryList; // Change to List<OrderHistoryModel>
    private OrderHistoryAdapter orderHistoryAdapter;

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

        categories = binding.categories;
        popular = binding.popular;
        orderHistory = binding.orderHistory;

        // Set up categories
        categoriesModelList = new ArrayList<>();
        categoriesModelList.add(new CategoriesModel(R.drawable.cake, "Cake"));
        categoriesModelList.add(new CategoriesModel(R.drawable.bread, "Bread"));
        categoriesModelList.add(new CategoriesModel(R.drawable.balloons, "Balloons"));
        categoriesModelList.add(new CategoriesModel(R.drawable.candles, "Candles"));
        categoriesModelList.add(new CategoriesModel(R.drawable.confetti, "Confetti"));

        categoriesAdapter = new CategoriesAdapter(getActivity(), categoriesModelList);
        categories.setAdapter(categoriesAdapter);
        categories.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        categories.setHasFixedSize(true);
        categories.setNestedScrollingEnabled(false);

        // Set up popular products
        popularProductList = new ArrayList<>();
        popularProductList.add(new PopularModel("Product 1", "Description 1", "₱700.00", R.drawable.cake));
        popularProductList.add(new PopularModel("Product 2", "Description 2", "₱800.00", R.drawable.cake));
        popularProductList.add(new PopularModel("Product 3", "Description 3", "₱900.00", R.drawable.cake_sample));
        popularProductList.add(new PopularModel("Product 3", "Description 3", "₱900.00", R.drawable.cake_sample));
        popularProductList.add(new PopularModel("Product 3", "Description 3", "₱900.00", R.drawable.cake_sample));

        popularAdapter = new PopularAdapter(getActivity(), popularProductList);
        popular.setAdapter(popularAdapter);
        popular.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        popular.setHasFixedSize(true);
        popular.setNestedScrollingEnabled(false);


        // Set up order history
        orderHistoryList = new ArrayList<>();
        orderHistoryList.add(new OrderHistoryModel("Vanilla Bean Cake", "₱700.00", R.drawable.cake_sample));
        orderHistoryList.add(new OrderHistoryModel("Chocolate Cake", "₱750.00", R.drawable.cake_sample));
        orderHistoryList.add(new OrderHistoryModel("Red Velvet Cake", "₱800.00", R.drawable.cake_sample));

        orderHistoryAdapter = new OrderHistoryAdapter(getActivity(), orderHistoryList);
        orderHistory.setAdapter(orderHistoryAdapter);
        orderHistory.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        orderHistory.setHasFixedSize(true);
        orderHistory.setNestedScrollingEnabled(false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        drawerLayout = view.findViewById(R.id.drawerLayout);

        mAuth = FirebaseAuth.getInstance();
        btnLogout = view.findViewById(R.id.btnLogout);
        txtUser = view.findViewById(R.id.txtUser);
        user = mAuth.getCurrentUser();

        if (user == null) {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_loginFragment_to_homePageFragment);
        } else {
            txtUser.setText(user.getEmail());
        }

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_homePageFragment_to_loginFragment);
        });

        binding.menu.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_homePageFragment_to_drawer);
        });

        int[] images = {
                R.drawable.homepage_slider,
                R.drawable.image2,
                R.drawable.image3,
        };

        adapter = new CarouselAdapter(images);
        binding.viewPager.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
