package com.ignacio.partykneadsapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.ignacio.partykneadsapp.databinding.FragmentOTPBinding;

import java.util.Random;

public class OTPFragment extends Fragment {

    private FragmentOTPBinding binding;
    private FirebaseAuth auth;
    private String email = "";
    private String pass = "";
    private int random;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOTPBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            email = getArguments().getString("email");
            pass = getArguments().getString("password");
        }
        auth = FirebaseAuth.getInstance();

        random();

        // Add text watchers for OTP fields
        setupOtpInputFields();
        binding.tvEmail.setText(email);
        binding.btnsubmitOTP.setOnClickListener(v -> verifyOtp());
    }

    private void setupOtpInputFields() {
        binding.otp1.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.otp2.requestFocus();
                }
            }
        });

        binding.otp2.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.otp3.requestFocus();
                } else {
                    binding.otp1.requestFocus();
                }
            }
        });

        binding.otp3.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.otp4.requestFocus();
                } else {
                    binding.otp2.requestFocus();
                }
            }
        });

        binding.otp4.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.otp5.requestFocus();
                } else {
                    binding.otp3.requestFocus();
                }
            }
        });

        binding.otp5.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.otp6.requestFocus();
                } else {
                    binding.otp4.requestFocus();
                }
            }
        });

        binding.otp6.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    binding.otp5.requestFocus();
                }
            }
        });
    }

    private void verifyOtp() {
        String otp1 = binding.otp1.getText().toString();
        String otp2 = binding.otp2.getText().toString();
        String otp3 = binding.otp3.getText().toString();
        String otp4 = binding.otp4.getText().toString();
        String otp5 = binding.otp5.getText().toString();
        String otp6 = binding.otp6.getText().toString();

        String otp = otp1 + otp2 + otp3 + otp4 + otp5 + otp6;

        if (otp.isEmpty() || otp.length() < 6) {
            Toast.makeText(getActivity(), "Enter OTP", Toast.LENGTH_SHORT).show();
        } else if (!otp.equals(String.valueOf(random))) {
            Toast.makeText(getActivity(), "Wrong OTP", Toast.LENGTH_SHORT).show();
        } else {
            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    NavController navController = Navigation.findNavController(requireView());
                    navController.navigate(R.id.action_OTPFragment_to_homePageFragment);
                } else {
                    Toast.makeText(getActivity(), "error occured", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void random() {
        random = new Random().nextInt(900000) + 100000; // Generate 6-digit OTP

        // Create and send the email
        String subject = "Login Signup app's OTP";
        String message = "Your OTP is -> " + random;

        new JavaMailSender(email, subject, message).execute(); // Send email

    }

    private abstract class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {}
    }
}
