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
        holder.productName.setText(product.getName());
        holder.itemRating.setText(product.getRate());
        holder.itemPrice.setText(product.getPrice());
        holder.productImage.setImageResource(product.getImageResource());
        holder.itemSold.setText(product.getSold());


    }

    @Override
    public int getItemCount() {
        return products.size();
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
