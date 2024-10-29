package com.ignacio.partykneadsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ignacio.partykneadsapp.R;
import com.ignacio.partykneadsapp.model.CakeSizeModel;

import java.util.List;

public class CakeSizeAdapter extends RecyclerView.Adapter<CakeSizeAdapter.CakeSizeViewHolder> {

    private List<CakeSizeModel> cakeSizes;
    private OnItemClickListener listener;
    private CakeSizeModel selectedCakeSize; // Track the selected cake size

    // Define an interface for item click
    public interface OnItemClickListener {
        void onItemClick(CakeSizeModel cakeSize);
    }

    public CakeSizeAdapter(List<CakeSizeModel> cakeSizes, OnItemClickListener listener) {
        this.cakeSizes = cakeSizes;
        this.listener = listener;
        this.selectedCakeSize = cakeSizes.get(0); // Default to the first item (Bento Cake)
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
        holder.cakeSizesTextView.setText(cakeSize.getSize() + ": " + cakeSize.getServings());

        // Set background and text color based on selection
        Context context = holder.itemView.getContext();
        CardView cardView = holder.itemView.findViewById(R.id.cakeCardView);

        if (cakeSize.equals(selectedCakeSize)) {
            // Set background color for selected item
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.pink));
            holder.cakeSizesTextView.setTextColor(ContextCompat.getColor(context, R.color.semiwhite));
        } else {
            // Set background color for unselected items
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.footerpink));
            holder.cakeSizesTextView.setTextColor(ContextCompat.getColor(context, R.color.semiblack));
        }

        // Set click listener on the item view
        holder.itemView.setOnClickListener(v -> {
            selectedCakeSize = cakeSize; // Update selected size
            notifyDataSetChanged(); // Notify adapter to refresh the views
            listener.onItemClick(cakeSize); // Trigger the item click listener
        });
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

    // Method to get the currently selected cake size
    public CakeSizeModel getSelectedCakeSize() {
        return selectedCakeSize;
    }
}
