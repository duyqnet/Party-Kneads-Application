package com.ignacio.partykneadsapp.onboarding.screens;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ignacio.partykneadsapp.R;
import androidx.core.app.ActivityCompat;

public class FourthScreen extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fourth_screen, container, false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        view.findViewById(R.id.btnFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBoardingFinished();
                requestLocationAccess();
            }
        });

        return view;
    }

    private void requestLocationAccess() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLastLocationAndProceed();
        }
    }

    private void getLastLocationAndProceed() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireActivity(), "Location permission not granted. Proceeding without location.", Toast.LENGTH_SHORT).show();
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Toast.makeText(requireActivity(), "Location: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(requireActivity(), "No location found", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
        NavHostFragment.findNavController(FourthScreen.this).navigate(R.id.action_viewPagerFragment_to_loginFragment);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocationAndProceed();
            } else {
                Toast.makeText(requireActivity(), "Location permission denied. Proceeding without location.", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(FourthScreen.this).navigate(R.id.action_viewPagerFragment_to_loginFragment);
            }
        }
    }

    private void onBoardingFinished() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Finished", true);
        editor.apply();
    }
}