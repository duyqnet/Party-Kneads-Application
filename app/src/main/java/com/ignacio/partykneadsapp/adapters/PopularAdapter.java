package com.ignacio.partykneadsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ignacio.partykneadsapp.R;
import com.ignacio.partykneadsapp.model.PopularModel;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {

    private Context context;
    private List<PopularModel> products;

    public PopularAdapter(Context context, List<PopularModel> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PopularModel product = products.get(position);
        holder.productText.setText(product.getName());
        holder.itemDescription.setText(product.getDescription());
        holder.itemPrice.setText(product.getPrice());
        holder.productImage.setImageResource(product.getImageResource());

        // Handle button click
        holder.btnOrderNow.setOnClickListener(v -> {
            // Handle order now action, e.g., open product details
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productText;
        TextView itemDescription;
        TextView itemPrice;
        ImageView productImage;
        Button btnOrderNow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productText = itemView.findViewById(R.id.productText);
            itemDescription = itemView.findViewById(R.id.itemDescription);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            productImage = itemView.findViewById(R.id.productImage);
            btnOrderNow = itemView.findViewById(R.id.btnOrderNow);
        }
    }
}
