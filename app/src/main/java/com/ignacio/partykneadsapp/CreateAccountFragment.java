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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class CreateAccountFragment extends Fragment {

    TextInputEditText etPassCA, etEmailCA;
    Button btnCont;
    FirebaseAuth mAuth;
    TextView btnBack;
    ConstraintLayout cl;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_createAccountFragment4_to_homePageFragment);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_account, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        etEmailCA = view.findViewById(R.id.etEmailCA);
        etPassCA = view.findViewById(R.id.etPassCA);
        btnCont = view.findViewById(R.id.btnCont);
        btnBack = view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_createAccountFragment4_to_personaldetailsFragment);
            }
        });

        btnCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmailCA.getText().toString();
                String password = etPassCA.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity(), "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity(), "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create user with email and password using Firebase Auth
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Account created successfully.", Toast.LENGTH_SHORT).show();
                                    // Navigate to next screen or perform other actions
                                    NavController navController = Navigation.findNavController(view);
                                    navController.navigate(R.id.action_createAccountFragment4_to_homePageFragment);
                                } else {
                                    Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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