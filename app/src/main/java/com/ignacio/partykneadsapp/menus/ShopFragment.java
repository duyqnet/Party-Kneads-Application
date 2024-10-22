package com.ignacio.partykneadsapp.menus;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ignacio.partykneadsapp.R;
import com.ignacio.partykneadsapp.adapters.ProductShopAdapter;
import com.ignacio.partykneadsapp.model.ProductShopModel;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {

    private List<ProductShopModel> productList; // Declare productList here

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

//        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        productList = new ArrayList<>();
        productList.add(new ProductShopModel("Vanilla Bean Cake", "P 567.00", "5.0", 78, R.drawable.strawberry));
        productList.add(new ProductShopModel("Chocolate Cake", "P 650.00", "4.5", 50, R.drawable.strawberry));
        productList.add(new ProductShopModel("Red Velvet Cake", "P 700.00", "4.8", 65, R.drawable.strawberry));
        productList.add(new ProductShopModel("Cheesecake", "P 600.00", "4.9", 85, R.drawable.strawberry));
        productList.add(new ProductShopModel("Lemon Tart", "P 550.00", "4.6", 40, R.drawable.strawberry));

        ProductShopAdapter adapter = new ProductShopAdapter(getActivity(), productList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
