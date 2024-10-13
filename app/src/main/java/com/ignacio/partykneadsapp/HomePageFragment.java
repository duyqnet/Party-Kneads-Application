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
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePageFragment extends Fragment {

    FirebaseAuth mAuth;
    TextView txtUser;
    Button btnLogout;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        btnLogout = view.findViewById(R.id.btnLogout);
        txtUser = view.findViewById(R.id.txtUser);
        user = mAuth.getCurrentUser();

        if(user == null) {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_loginFragment_to_homePageFragment);
        } else {
            txtUser.setText(user.getEmail());
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_homePageFragment_to_loginFragment);
            }
        });
    }

}