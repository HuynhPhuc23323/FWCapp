package com.fwc.cosmetic.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fwc.cosmetic.ProductDetailActivity;
import com.fwc.cosmetic.R;
import com.fwc.cosmetic.adapter.HeaderAdapter;
import com.fwc.cosmetic.adapter.ProductClickListener;
import com.fwc.cosmetic.adapter.StoreAdapter;
import com.fwc.cosmetic.model.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator2;

public class HomeFragment extends Fragment implements ProductClickListener {

    private RecyclerView rvHeader, rvItem;
    private SwipeRefreshLayout swRefresh;

    private List<Product> productList = new ArrayList();
    private List<Integer> bannerIdList;
    private StoreAdapter storeAdapter;
    private HeaderAdapter headerAdapter;
    private TimerTask timerTask;
    private Timer timer;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rvItem = view.findViewById(R.id.rvItem);
        rvHeader = view.findViewById(R.id.rvHeader);
        swRefresh = view.findViewById(R.id.swRefresh);

        // them data cho banner
        bannerIdList = new ArrayList<>();
        bannerIdList.add(R.drawable.banner1);
        bannerIdList.add(R.drawable.banner2);
        bannerIdList.add(R.drawable.banner3);
        bannerIdList.add(R.drawable.banner4);
        bannerIdList.add(R.drawable.banner5);

        // setup adapter cho banner
        headerAdapter = new HeaderAdapter(getActivity(), bannerIdList);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        // setup 1 item = 1 man hinh
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvHeader);
        rvHeader.setLayoutManager(mLayoutManager2);
        rvHeader.setAdapter(headerAdapter);

        // set indicator cho banner
        CircleIndicator2 indicator = view.findViewById(R.id.indicator);
        indicator.attachToRecyclerView(rvHeader, snapHelper);
        headerAdapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());

        // set adapter cho list product
        storeAdapter = new StoreAdapter(getActivity(), this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvItem.setLayoutManager(mLayoutManager);
        rvItem.setAdapter(storeAdapter);

        getProducts();

        swRefresh.setOnRefreshListener(() -> {
            getProducts();
            swRefresh.setRefreshing(false);
        });

        return view;
    }

    private void setAutoScrollHeader() {
        final int speedScroll = 5000;
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    int position = ((LinearLayoutManager) rvHeader.getLayoutManager()).findFirstVisibleItemPosition();
                    int count = headerAdapter.getItemCount();
                    if (position < count - 1) {
                        rvHeader.smoothScrollToPosition(position + 1);
                    } else {
                        rvHeader.smoothScrollToPosition(0);
                    }
                }
            };
        }
        if (timer == null) timer = new Timer();
        timer.schedule(timerTask, speedScroll, speedScroll);
    }

    @Override
    public void onStart() {
        super.onStart();
        // set auto scroll cho banner
        setAutoScrollHeader();
    }

    @Override
    public void onStop() {
        super.onStop();
        timerTask.cancel();
        timerTask = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer.purge();
        timer = null;
        timerTask = null;
    }

    private void getProducts() {
        db.collection("Products")
                .get()
                .addOnCompleteListener(task -> {
                    swRefresh.setRefreshing(false);
                    if (task.isSuccessful()) {
                        productList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            productList.add(document.toObject(Product.class)); // parse data from Firestore thanh Object Product
                        }
                        storeAdapter.setData(productList);
                    }
                });
    }

    @Override
    public void onClick(Product product) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class).putExtra("product", product);
        startActivity(intent);
    }
}