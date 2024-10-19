package com.ignacio.partykneadsapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ignacio.partykneadsapp.R;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ignacio.partykneadsapp.adapters.CategoriesAdapter;
import com.ignacio.partykneadsapp.databinding.FragmentHomePageBinding;
import com.ignacio.partykneadsapp.menus.LikesFragment;
import com.ignacio.partykneadsapp.menus.NotificationFragment;
import com.ignacio.partykneadsapp.menus.ProfileFragment;
import com.ignacio.partykneadsapp.menus.ShopFragment;
import com.ignacio.partykneadsapp.model.CategoriesModel;

import java.util.ArrayList;
import java.util.List;

public class HomePageFragment extends Fragment implements NavigationBarView.OnItemSelectedListener {

    private FragmentHomePageBinding binding;
    private BottomNavigationView bottomNavigationView;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        bottomNavigationView = binding.bottomNav;
        bottomNavigationView.setOnItemSelectedListener(this);
        loadFragment(new HomeFragment());



    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.homePage:
                fragment = new HomeFragment();
                break;

            case R.id.favorites:
                fragment = new LikesFragment();
                break;

            case R.id.shop:
                fragment = new ShopFragment();
                break;

            case R.id.notification:
                fragment = new NotificationFragment();
                break;

            case R.id.profile:
                fragment = new ProfileFragment();
                break;
        }
        return loadFragment(fragment);
    }

    boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_cont, fragment).commit();
            return true;
        }
        return false;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment using View Binding
        binding = FragmentHomePageBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
