package com.ignacio.partykneadsapp.menus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ignacio.partykneadsapp.R;
import com.ignacio.partykneadsapp.databinding.FragmentProfileBinding;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        binding.btnLogout.setOnClickListener(v -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            FirebaseAuth.getInstance().signOut();

            NavController navController = Navigation.findNavController(requireView());
            if (Objects.equals(currentUser.getEmail(), "sweetkatrinabiancaignacio@gmail.com")) {
                navController.navigate(R.id.action_profileFragment_to_loginFragment);
            } else {
                navController.navigate(R.id.action_profileFragment_to_loginFragment);
            }
        });

        binding.btnBack.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            if (Objects.equals(currentUser.getEmail(), "sweetkatrinabiancaignacio@gmail.com")) {
                navController.navigate(R.id.action_profileFragment_to_seller_HomePageFragment);
            } else {
                navController.navigate(R.id.action_profileFragment_to_homePageFragment);
            }
        });
    }
}