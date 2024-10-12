package com.ignacio.partykneadsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SplashFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        new Handler().postDelayed(() -> {
            if (onBoardingFinished())
            {
                NavHostFragment.findNavController(this).navigate(R.id.action_splashFragment_to_loginFragment);
            } else {
                NavHostFragment.findNavController(this).navigate(R.id.action_splashFragment_to_viewPagerFragment);
            }
        }, 1500);

        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    private boolean onBoardingFinished()
    {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE);
        return sharedPref.getBoolean("Finished", false);
    }

}