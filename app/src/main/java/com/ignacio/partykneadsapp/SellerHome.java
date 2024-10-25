package com.ignacio.partykneadsapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ignacio.partykneadsapp.databinding.FragmentSellerHomeBinding;

public class SellerHome extends Fragment {

    FragmentSellerHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSellerHomeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Check if binding is correctly initialized
        if (binding == null) {
            Log.e("SellerHome", "Binding is null");
            return;
        }

        // Test the button click and Toast first
        binding.btnmyProduct1.setOnClickListener(v -> {

                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView2);
                navController.navigate(R.id.action_seller_HomePageFragment_to_myProductFragment);
            
        });
    }
}
