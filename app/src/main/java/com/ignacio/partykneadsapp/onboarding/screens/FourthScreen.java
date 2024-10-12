package com.ignacio.partykneadsapp.onboarding.screens;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ignacio.partykneadsapp.R;

public class FourthScreen extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fourth_screen, container, false);

        view.findViewById(R.id.btnFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FourthScreen.this).navigate(R.id.action_viewPagerFragment_to_loginFragment);

                onBoardingFinished();
            }
        });

        return view;
    }

    private void onBoardingFinished() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Finished", true);
        editor.apply();
    }
}