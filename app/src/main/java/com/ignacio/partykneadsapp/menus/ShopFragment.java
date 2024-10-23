package com.ignacio.partykneadsapp.menus;

import static android.content.Context.INPUT_METHOD_SERVICE;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.ignacio.partykneadsapp.R;
import com.ignacio.partykneadsapp.adapters.CategoriesAdapter;
import com.ignacio.partykneadsapp.adapters.ProductShopAdapter;
import com.ignacio.partykneadsapp.databinding.FragmentShopBinding;
import com.ignacio.partykneadsapp.model.CategoriesModel;
import com.ignacio.partykneadsapp.model.ProductShopModel;

import java.util.ArrayList;
import java.util.List;



public class ShopFragment extends Fragment {

    private LinearLayout cl;
    private List<ProductShopModel> productList; // Declare productList here
    private RecyclerView categories;
    private List<CategoriesModel> categoriesModelList;
    private CategoriesAdapter categoriesAdapter;
    FragmentShopBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentShopBinding.inflate(getLayoutInflater());
//        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        productList = new ArrayList<>();
        productList.add(new ProductShopModel("Vanilla Bean Cake", "P 567.00", "5.0", 78, R.drawable.strawberry));
        productList.add(new ProductShopModel("Chocolate Cake", "P 650.00", "4.5", 50, R.drawable.strawberry));
        productList.add(new ProductShopModel("Red Velvet Cake", "P 700.00", "4.8", 65, R.drawable.strawberry));
        productList.add(new ProductShopModel("Cheesecake", "P 600.00", "4.9", 85, R.drawable.strawberry));
        productList.add(new ProductShopModel("Lemon Tart", "P 550.00", "4.6", 40, R.drawable.strawberry));

        ProductShopAdapter adapter = new ProductShopAdapter(getActivity(), productList);
        recyclerView.setAdapter(adapter);

        setupCategories();

        return binding.getRoot();



    }


    private void setupCategories() {
        categories = binding.categories;
        categoriesModelList = new ArrayList<>();
        categoriesModelList.add(new CategoriesModel(R.drawable.cake, "Cakes"));
        categoriesModelList.add(new CategoriesModel(R.drawable.breads, "Breads"));
        categoriesModelList.add(new CategoriesModel(R.drawable.desserts, "Desserts"));
        categoriesModelList.add(new CategoriesModel(R.drawable.customized, "Customize"));
        categoriesModelList.add(new CategoriesModel(R.drawable.balloons, "Balloons"));
        categoriesModelList.add(new CategoriesModel(R.drawable.candles, "Candles"));
        categoriesModelList.add(new CategoriesModel(R.drawable.party_hats, "Party Hats"));
        categoriesModelList.add(new CategoriesModel(R.drawable.banners, "Banners"));
        categoriesModelList.add(new CategoriesModel(R.drawable.confetti, "Confetti"));

        categoriesAdapter = new CategoriesAdapter(getActivity(), categoriesModelList);
        categories.setAdapter(categoriesAdapter);
        categories.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        categories.setHasFixedSize(true);
        categories.setNestedScrollingEnabled(false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cl = view.findViewById(R.id.clayout);
        cl.setOnClickListener(v -> {
            InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

    }
}
