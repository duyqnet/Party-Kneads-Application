package com.ignacio.partykneadsapp;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateAccountFragment extends Fragment {

    TextInputEditText etPassCA, etEmailCA;
    Button btnCont;
    FirebaseAuth mAuth;
    TextView btnBack;
    ConstraintLayout cl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        checkIfUserIsVerified();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etEmailCA = view.findViewById(R.id.etEmailCA);
        etPassCA = view.findViewById(R.id.etPassCA);
        btnCont = view.findViewById(R.id.btnCont);
        btnBack = view.findViewById(R.id.btnBack);
        cl = view.findViewById(R.id.clayout);

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

                // Check if password is strong
                if (!isStrongPassword(password)) {
                    Toast.makeText(getActivity(), "Weak password! Must include:\n- At least one uppercase letter\n- At least one lowercase letter\n- At least one number\n- At least one special character", Toast.LENGTH_LONG).show();
                    return;
                }

                // Create user with email and password using Firebase Auth
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        sendVerificationLink(user);
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Account creation failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }

    private void sendVerificationLink(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Verification link sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Failed to send verification link.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkIfUserIsVerified() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.reload().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (user.isEmailVerified()) {
                        NavController navController = Navigation.findNavController(requireView());
                        navController.navigate(R.id.action_createAccountFragment4_to_homePageFragment);
                    }
                }
            });
        }
    }

    private boolean isStrongPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        Pattern lowerCasePattern = Pattern.compile("[a-z]");
        Pattern numberPattern = Pattern.compile("[0-9]");
        Pattern specialCharPattern = Pattern.compile("[^a-zA-Z0-9 ]");

        Matcher hasUpperCase = upperCasePattern.matcher(password);
        Matcher hasLowerCase = lowerCasePattern.matcher(password);
        Matcher hasNumber = numberPattern.matcher(password);
        Matcher hasSpecialChar = specialCharPattern.matcher(password);

        return hasUpperCase.find() && hasLowerCase.find() && hasNumber.find() && hasSpecialChar.find();
    }
}