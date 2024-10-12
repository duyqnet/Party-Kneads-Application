package com.ignacio.partykneadsapp.onboarding;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private ArrayList<Fragment> fragmentList;

    public ViewPagerAdapter(ArrayList<Fragment> list, FragmentManager fm, Lifecycle lifecycle) {
        super(fm, lifecycle);
        this.fragmentList = list; // Assigning the passed list to the local variable
    }

    @Override
    public int getItemCount() {
        return fragmentList.size(); // Returns the size of the fragment list
    }

    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position); // Returns the fragment at the specified position
    }
}