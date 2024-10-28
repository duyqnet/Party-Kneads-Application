package com.ignacio.partykneadsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ignacio.partykneadsapp.R;
import com.ignacio.partykneadsapp.model.CartItemModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItemModel> cartItems;
    private HashMap<Integer, Boolean> selectedItems = new HashMap<>();
    private OnItemSelectedListener onItemSelectedListener;

    // Interface for item selection change
    public interface OnItemSelectedListener {
        void onItemSelected();
    }

    public CartAdapter(List<CartItemModel> cartItems, OnItemSelectedListener listener) {
        this.cartItems = cartItems;
        this.onItemSelectedListener = listener;
        for (int i = 0; i < cartItems.size(); i++) {
            selectedItems.put(i, false); // Initialize all items as unchecked
        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItemModel item = cartItems.get(position);
        holder.bind(item, position);

        // Set checkbox state with a fallback to false if not present
        holder.checkBox.setChecked(selectedItems.getOrDefault(position, false));

        // Checkbox listener
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selectedItems.put(position, isChecked); // Update selection state
            onItemSelectedListener.onItemSelected(); // Notify the listener
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void selectAll(boolean isSelected) {
        for (int i = 0; i < cartItems.size(); i++) {
            selectedItems.put(i, isSelected); // Update selection state for all items
        }
        notifyDataSetChanged(); // Notify adapter of changes
    }

    public List<CartItemModel> getSelectedItems() {
        List<CartItemModel> selected = new ArrayList<>();
        for (int i = 0; i < cartItems.size(); i++) {
            if (selectedItems.getOrDefault(i, false)) {
                selected.add(cartItems.get(i));
            }
        }
        return selected;
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private TextView productName, quantity, totalPrice, ratePercent, numReviews;
        private ImageView cakeImage;
        private CheckBox checkBox;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            quantity = itemView.findViewById(R.id.quantity);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            ratePercent = itemView.findViewById(R.id.ratePercent);
            numReviews = itemView.findViewById(R.id.numReviews);
            cakeImage = itemView.findViewById(R.id.cakeImage);
            checkBox = itemView.findViewById(R.id.checkbox);
        }

        public void bind(CartItemModel item, int position) {
            productName.setText(item.getProductName());
            quantity.setText(String.valueOf(item.getQuantity()));
            totalPrice.setText(item.getTotalPrice()); // Updated method call
            ratePercent.setText(item.getRate() != null ? item.getRate() : "test");
            numReviews.setText(item.getNumReviews() != null ? item.getNumReviews() : "test");

            Glide.with(itemView.getContext())
                    .load(item.getImageUrl())
                    .into(cakeImage);
        }
    }
}
