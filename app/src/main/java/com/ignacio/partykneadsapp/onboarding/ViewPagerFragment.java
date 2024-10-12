package com.ignacio.partykneadsapp.onboarding;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ignacio.partykneadsapp.R;
import com.ignacio.partykneadsapp.onboarding.screens.FirstScreen;
import com.ignacio.partykneadsapp.onboarding.screens.SecondScreen;
import com.ignacio.partykneadsapp.onboarding.screens.ThirdScreen;
import com.ignacio.partykneadsapp.onboarding.screens.FourthScreen;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        // Create a list of fragments
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new FirstScreen());
        fragmentList.add(new SecondScreen());
        fragmentList.add(new ThirdScreen());
        fragmentList.add(new FourthScreen());

        // Set up the adapter for ViewPager2
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, fragmentList);

        // Find the ViewPager2 in the inflated layout
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        return view;
    }

    // Adapter for ViewPager2
    private static class ViewPagerAdapter extends FragmentStateAdapter {

        private final List<Fragment> fragmentList;

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> fragments) {
            super(fragmentActivity);
            this.fragmentList = fragments;
        }

        public ViewPagerAdapter(@NonNull Fragment fragment, List<Fragment> fragments) {
            super(fragment);
            this.fragmentList = fragments;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }
}