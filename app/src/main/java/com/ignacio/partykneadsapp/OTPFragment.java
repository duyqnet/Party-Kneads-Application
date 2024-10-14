package com.ignacio.partykneadsapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.View;
import android.widget.Button;


public class OTPFragment extends Fragment {

    Button submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        submit= view.findViewById(R.id.btnsubmitOTP);

    }

    public void otpSubmit(View view) {
        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.action_OTPFragment_to_homePageFragment);
    }
}