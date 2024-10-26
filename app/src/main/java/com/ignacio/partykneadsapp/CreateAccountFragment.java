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
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateAccountFragment extends Fragment {

    private TextInputEditText etPassCA, etEmailCA;
    private Button btnCont;
    private FirebaseAuth mAuth;
    private TextView btnBack;
    private ConstraintLayout cl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
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
        String fname, lname;

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_createAccountFragment4_to_personaldetailsFragment);
            }
        });

        btnCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmailCA.getText().toString().trim();
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
                } else {
                    // Proceed to OTP Fragment
                    Bundle bundle = new Bundle();
                    bundle.putString("email", email);
                    bundle.putString("password", password);


                    NavController navController = Navigation.findNavController(requireView());
                    navController.navigate(R.id.action_createAccountFragment4_to_OTPFragment, bundle);
                }
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
