package com.ignacio.partykneadsapp;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.Objects;

public class LoginFragment extends Fragment {

    private TextInputEditText etEmail, etPass;
    private Button btnContinue, btnFacebook, btnGoogle;
    private FirebaseAuth mAuth;
    private TextView btnSignup;
    private ConstraintLayout cl;
    private CallbackManager callbackManager;
    private GoogleSignInClient googleSignInClient;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            if (Objects.equals(currentUser.getEmail(), "sweetkatrinabiancaignacio@gmail.com")) {
                navigateToSellerHomePage();
            } else {
                Log.d("LoginFragment", "User is already signed in: " + currentUser.getEmail());
                navigateToUserHomePage();
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();

        etEmail = view.findViewById(R.id.etEmail);
        etPass = view.findViewById(R.id.etPassword);
        btnContinue = view.findViewById(R.id.btnContinue);
        btnSignup = view.findViewById(R.id.btnSignUp);
        btnFacebook = view.findViewById(R.id.btnFacebook);
        btnGoogle = view.findViewById(R.id.btnGoogle);

        btnSignup.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_loginFragment_to_termsFragment2);
        });

        //Login with email
        btnContinue.setOnClickListener(v -> loginWithEmail());

        cl = view.findViewById(R.id.clayout);
        cl.setOnClickListener(v -> hideKeyboard(v));

        setupFacebookLogin();
        setupGoogleSignIn();
    }

    private void loginWithEmail() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Login Successfully", Toast.LENGTH_SHORT).show();
                        if (Objects.equals(email, "sweetkatrinabiancaignacio@gmail.com")) {
                            navigateToSellerHomePage();
                        } else {
                            navigateToUserHomePage();
                        }
                    } else {
                        Log.e("LoginFragment", "Email login failed", task.getException());
                        Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setupFacebookLogin() {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("LoginFragment", "Facebook login successful");
                        AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                        mAuth.signInWithCredential(credential)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Log.d("LoginFragment", "Firebase authentication successful");
                                        Toast.makeText(getActivity(), "Login Successfully", Toast.LENGTH_SHORT).show();
                                        navigateToUserHomePage();
                                    } else {
                                        Log.e("LoginFragment", "Firebase authentication failed", task.getException());
                                        Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onCancel() {
                        Log.d("LoginFragment", "Facebook login canceled");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("LoginFragment", "Facebook login error", exception);
                    }
                });

        btnFacebook.setOnClickListener(v -> {
            LoginManager.getInstance().logOut();
            LoginManager.getInstance().logInWithReadPermissions(LoginFragment.this, Arrays.asList("public_profile"));
        });
    }

    private void setupGoogleSignIn() {
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id)) // Update to your actual client ID
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), options);

        btnGoogle.setOnClickListener(v -> {
            Intent intent = googleSignInClient.getSignInIntent();
            startActivityForResult(intent, 100); // Request code can be anything
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            handleGoogleSignInResult(data);
        }
    }

    private void handleGoogleSignInResult(Intent data) {
        try {
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
            AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
            mAuth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("LoginFragment", "Google sign-in successful");
                    Toast.makeText(getActivity(), "Login Successfully", Toast.LENGTH_SHORT).show();
                    navigateToUserHomePage();
                } else {
                    Log.e("LoginFragment", "Google sign-in failed", task.getException());
                    Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (ApiException e) {
            Log.e("LoginFragment", "Google sign-in failed", e);
        }
    }

    private void navigateToUserHomePage() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_loginFragment_to_homePageFragment);
    }

    private void navigateToSellerHomePage() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_loginFragment_to_seller_HomePageFragment);
    }
}