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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class TermsFragment extends Fragment {

    Switch switchTerms;
    Button btnCont;
    TextView btnBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_terms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switchTerms = view.findViewById(R.id.switchTerms);
        btnCont = view.findViewById(R.id.btnCont);
        btnBack = view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_termsFragment2_to_loginFragment);
            }
        });

        btnCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchTerms.isChecked()) {
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_termsFragment2_to_personaldetailsFragment);
                } else {
                    Toast.makeText(getActivity(), "Read pls", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}