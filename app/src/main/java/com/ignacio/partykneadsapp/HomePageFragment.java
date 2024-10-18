package com.ignacio.partykneadsapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ignacio.partykneadsapp.databinding.FragmentHomePageBinding;

public class HomePageFragment extends Fragment {

    private FragmentHomePageBinding binding;
    private CarouselAdapter adapter;

    private FirebaseAuth mAuth;
    private TextView txtUser;
    private Button btnLogout;
    private FirebaseUser user;
    private DrawerLayout drawerLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment using View Binding
        binding = FragmentHomePageBinding.inflate(inflater, container, false);
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
                R.drawable.image1,
                R.drawable.image2,
                R.drawable.image3,
                // Add more images as needed
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
