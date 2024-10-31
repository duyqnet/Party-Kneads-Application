package com.ignacio.partykneadsapp;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;

public class CustomGridLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = false;

    public CustomGridLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}