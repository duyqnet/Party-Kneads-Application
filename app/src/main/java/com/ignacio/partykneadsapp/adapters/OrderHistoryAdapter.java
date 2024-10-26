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
import com.ignacio.partykneadsapp.model.OrderHistoryModel;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private Context context;
    private List<OrderHistoryModel> orderHistoryList;

    public OrderHistoryAdapter(Context context, List<OrderHistoryModel> orderHistoryList) {
        this.context = context;
        this.orderHistoryList = orderHistoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderHistoryModel order = orderHistoryList.get(position);
        holder.productName.setText(order.getName());
        holder.itemRating.setText(order.getRate());
        holder.itemPrice.setText(order.getPrice());
        holder.productImage.setImageResource(order.getImageResource());
        holder.itemSold.setText(order.getSold());
    }

    @Override
    public int getItemCount() {
        return orderHistoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView itemRating;
        TextView itemPrice;
        TextView itemSold;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            itemRating = itemView.findViewById(R.id.itemRating);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            productImage = itemView.findViewById(R.id.productImage);
            itemSold = itemView.findViewById(R.id.itemSold);
        }
    }
}
