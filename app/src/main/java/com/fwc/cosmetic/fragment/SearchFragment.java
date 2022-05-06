package com.fwc.cosmetic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fwc.cosmetic.ProductDetailActivity;
import com.fwc.cosmetic.R;
import com.fwc.cosmetic.adapter.ProductClickListener;
import com.fwc.cosmetic.adapter.SearchAdapter;
import com.fwc.cosmetic.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment implements ProductClickListener {

    private TextView tvEmpty;
    private SearchView searchView;
    private RecyclerView rvItemProduct;
    private final List<Product> productList = new ArrayList();
    private SearchAdapter searchAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        tvEmpty = view.findViewById(R.id.tvEmpty);
        rvItemProduct = view.findViewById(R.id.rvItemProduct);
        searchView = view.findViewById(R.id.searchView);

        searchAdapter = new SearchAdapter(getActivity(), this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvItemProduct.setLayoutManager(mLayoutManager);
        rvItemProduct.setAdapter(searchAdapter);

        searchView.setOnClickListener(v -> searchView.setIconified(false));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryProduct(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryProduct(newText);
                return false;
            }
        });

        getProducts();

        return view;
    }

    private void queryProduct(String query) {
        List<Product> queryList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getName().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                queryList.add(product);
            }
        }
        if (queryList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvItemProduct.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvItemProduct.setVisibility(View.VISIBLE);
            searchAdapter.setData(queryList);
        }
    }

    private void getProducts() {
        db.collection("Products")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        productList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            productList.add(document.toObject(Product.class));
                        }
                        tvEmpty.setVisibility(View.GONE);
                        rvItemProduct.setVisibility(View.VISIBLE);
                        searchAdapter.setData(productList);
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        searchView.setQuery("",true);
    }

    // ham nay override cua ProductClickListener, bat su kien click cua item trong adapter
    @Override
    public void onClick(Product product) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class).putExtra("product", product);
        startActivity(intent);
    }
}