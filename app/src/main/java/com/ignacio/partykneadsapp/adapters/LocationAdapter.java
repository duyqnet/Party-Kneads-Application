package com.ignacio.partykneadsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ignacio.partykneadsapp.R;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    private List<String> locationList;
    private String userName; // Store userName as a member variable

    public LocationAdapter(List<String> locationList, String userName) {
        this.locationList = locationList;
        this.userName = userName;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adress_list, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        String location = locationList.get(position);
        holder.locationTextView.setText(location);

        // Optionally set user name if you want to display it alongside locations
        holder.userNameTextView.setText(userName);
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public void setUserName(String userName) {
        this.userName = userName; // Update userName
        notifyDataSetChanged(); // Notify adapter of data change
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView locationTextView;
        TextView userNameTextView; // If displaying user name

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.location);
            userNameTextView = itemView.findViewById(R.id.txtUserName); // Assuming you have a TextView for user name
        }
    }
}
