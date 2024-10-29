package com.ignacio.partykneadsapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ignacio.partykneadsapp.databinding.FragmentHomePageBinding;
import com.ignacio.partykneadsapp.menus.LikesFragment;
import com.ignacio.partykneadsapp.menus.NotificationFragment;
import com.ignacio.partykneadsapp.menus.ProfileFragment;
import com.ignacio.partykneadsapp.menus.ShopFragment;

public class HomePageFragment extends Fragment {

    private FragmentHomePageBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean loadShopFragment = getArguments() != null && getArguments().getBoolean("loadShop", false);
        Fragment fragmentToLoad = loadShopFragment ? new ShopFragment() : new HomeFragment();

        if (savedInstanceState == null) {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_cont, fragmentToLoad)
                    .commit();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomePageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setActiveIcon(R.id.home); // Default active icon to Home on launch

        binding.home.setOnClickListener(v -> {
            loadFragment(new HomeFragment());
            setActiveIcon(R.id.home);
        });

        binding.likes.setOnClickListener(v -> {
            loadFragment(new LikesFragment());
            setActiveIcon(R.id.likes);
        });

        binding.shop.setOnClickListener(v -> {
            loadFragment(new ShopFragment());
            setActiveIcon(R.id.shop);
        });

        binding.notif.setOnClickListener(v -> {
            loadFragment(new NotificationFragment());
            setActiveIcon(R.id.notif);
        });

        binding.profile.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_homePageFragment_to_profileFragment);
            setActiveIcon(R.id.profile);
        });
    }

    // Helper method to update icons based on selected menu
    private void setActiveIcon(int selectedId) {
        binding.homeMenu.setImageResource(selectedId == R.id.home ? R.drawable.home_filled : R.drawable.home_outline);
        binding.heartMenu.setImageResource(selectedId == R.id.likes ? R.drawable.favorite_filled : R.drawable.favorite_outline);
        binding.shopMenu.setImageResource(selectedId == R.id.shop ? R.drawable.shop : R.drawable.shop);
        binding.notifMenu.setImageResource(selectedId == R.id.notif ? R.drawable.notif_filled : R.drawable.notif_outline);
        binding.profileMenu.setImageResource(selectedId == R.id.profile ? R.drawable.person_filled : R.drawable.person_outline);
    }

    // Utility method to load fragments in `fragment_cont`
    private void loadFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_cont, fragment)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
