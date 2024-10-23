package com.ignacio.partykneadsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ignacio.partykneadsapp.R;
import com.ignacio.partykneadsapp.model.ProductShopModel;

import java.util.List;

public class ProductShopAdapter extends RecyclerView.Adapter<ProductShopAdapter.ProductViewHolder> {

    private List<ProductShopModel> productList;

    public ProductShopAdapter(List<ProductShopModel> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_shop, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductShopModel currentProduct = productList.get(position);

        // Bind data to the views
        holder.productName.setText(currentProduct.getName());
        holder.itemPrice.setText("P " + currentProduct.getPrice());

        // Load product image using Glide
        Glide.with(holder.itemView.getContext())
                .load(currentProduct.getImageUrl())
                 // Add a placeholder image
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, itemPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
        }
    }

    public void updateData(List<ProductShopModel> newProductsList) {
        this.productList.clear();
        this.productList.addAll(newProductsList);
        notifyDataSetChanged();
    }

}
