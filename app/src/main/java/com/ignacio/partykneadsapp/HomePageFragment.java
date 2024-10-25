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

public class HomePageFragment extends Fragment{

    private FragmentHomePageBinding binding;
    private BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_cont, new HomeFragment()) // assuming Frag1 is FragmentOne
                    .commit();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            binding.home.setOnClickListener(v -> {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_cont, new HomeFragment())
                        .commit();
            });
            binding.likes.setOnClickListener(v -> {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_cont, new LikesFragment())
                        .commit();
            });

            binding.shop.setOnClickListener(v -> {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_cont, new ShopFragment())
                        .commit();
            });
            binding.notif.setOnClickListener(v -> {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_cont, new NotificationFragment())
                        .commit();
            });
            binding.profile.setOnClickListener(v -> {
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_homePageFragment_to_profileFragment);
            });

    }





    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment using View Binding
        binding = FragmentHomePageBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
