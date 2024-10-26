package com.ignacio.partykneadsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ignacio.partykneadsapp.R;
import com.ignacio.partykneadsapp.model.CakeSizeModel;

import java.util.List;

public class CakeSizeAdapter extends RecyclerView.Adapter<CakeSizeAdapter.CakeSizeViewHolder> {

    private List<CakeSizeModel> cakeSizes;
    private OnItemClickListener listener;

    // Define an interface for item click
    public interface OnItemClickListener {
        void onItemClick(CakeSizeModel cakeSize);
    }

    public CakeSizeAdapter(List<CakeSizeModel> cakeSizes, OnItemClickListener listener) {
        this.cakeSizes = cakeSizes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CakeSizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cake_sizes, parent, false); // Use your XML layout file name
        return new CakeSizeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CakeSizeViewHolder holder, int position) {
        CakeSizeModel cakeSize = cakeSizes.get(position);
        holder.cakeSizesTextView.setText(cakeSize.getSize() + ": " + cakeSize.getServings()); // Changed to getServings()

        // Set click listener on the item view
        holder.itemView.setOnClickListener(v -> listener.onItemClick(cakeSize));
    }

    @Override
    public int getItemCount() {
        return cakeSizes.size();
    }

    static class CakeSizeViewHolder extends RecyclerView.ViewHolder {
        TextView cakeSizesTextView;

        CakeSizeViewHolder(@NonNull View itemView) {
            super(itemView);
            cakeSizesTextView = itemView.findViewById(R.id.cakeSizes);
        }
    }
}
