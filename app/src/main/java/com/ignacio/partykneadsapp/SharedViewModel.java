package com.ignacio.partykneadsapp;

import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private String fname;
    private String lname;

    // Set the first and last names
    public void setNames(String fname, String lname) {
        this.fname = fname;
        this.lname = lname;
    }

    // Retrieve the first name
    public String getFname() {
        return fname;
    }

    // Retrieve the last name
    public String getLname() {
        return lname;
    }
}
