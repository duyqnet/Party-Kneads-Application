package com.ignacio.partykneadsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ignacio.partykneadsapp.R;
import com.ignacio.partykneadsapp.model.CartItemModel;

import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder> {
    private List<CartItemModel> selectedItems;

    public CheckoutAdapter(List<CartItemModel> selectedItems) {
        this.selectedItems = selectedItems;
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartcheckout_items, parent, false);
        return new CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {
        CartItemModel item = selectedItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return selectedItems.size();
    }

    class CheckoutViewHolder extends RecyclerView.ViewHolder {
        private TextView productName, quantity, totalPrice, cakeSize;
        private ImageView cakeImage;

        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);


            productName = itemView.findViewById(R.id.productName);
            quantity = itemView.findViewById(R.id.quantity);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            cakeImage = itemView.findViewById(R.id.cakeImage);
            cakeSize = itemView.findViewById(R.id.cakeSize);
        }

        public void bind(CartItemModel item) {
            productName.setText(item.getProductName());
            quantity.setText(String.valueOf(item.getQuantity()));
            totalPrice.setText(item.getTotalPrice());
            cakeSize.setText(item.getCakeSize());


            Glide.with(itemView.getContext())
                    .load(item.getImageUrl())
                    .into(cakeImage);
        }
    }
}
