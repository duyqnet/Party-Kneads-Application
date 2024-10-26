package com.ignacio.partykneadsapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
    TextView btnTerms;
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
        btnTerms = view.findViewById(R.id.btnTermsandConditions);

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

        btnTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_termsFragment2_to_tandCFragment);  // Ensure you have this action
            }
        });

        TextView textView = view.findViewById(R.id.tvAgree);
        String text = getString(R.string.readTerm);

// Create a SpannableString
        SpannableString spannableString = new SpannableString(text);

// Set the color only for the asterisk (*)
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

// Apply the SpannableString to the TextView
        textView.setText(spannableString);


    }
}