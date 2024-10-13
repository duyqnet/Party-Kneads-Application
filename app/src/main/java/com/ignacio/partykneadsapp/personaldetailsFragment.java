package com.ignacio.partykneadsapp;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;


public class personaldetailsFragment extends Fragment {

    TextInputEditText etfname;
    TextInputEditText etlname;
    Button btnCont;
    TextView btnBack;

    ConstraintLayout cl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personaldetails, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etfname = view.findViewById(R.id.etfName);
        etlname = view.findViewById(R.id.etPassCA);
        btnCont = view.findViewById(R.id.btnCont);
        btnBack = view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_personaldetailsFragment_to_termsFragment2);
            }
        });

        btnCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fname = etfname.getText().toString().trim();
                String lname = etlname.getText().toString().trim();

                if(TextUtils.isEmpty(fname) || TextUtils.isEmpty(lname)) {
                    Toast.makeText(getActivity(), "Please fill up all field", Toast.LENGTH_SHORT).show();
                } else {
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_personaldetailsFragment_to_createAccountFragment4);
                }
            }
        });

        cl = view.findViewById(R.id.clayout);

// Then just use the following:
        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }
}