package com.fwc.cosmetic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fwc.cosmetic.R;
import com.fwc.cosmetic.model.Product;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder> {
    private Context context;
    private List<Product> productList = new ArrayList();

    private ProductClickListener productClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price;
        public ImageView thumbnail;
        public Button btnBuyNow;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tvTitle);
            price = view.findViewById(R.id.tvPrice);
            thumbnail = view.findViewById(R.id.ivThumbnail);
            btnBuyNow = view.findViewById(R.id.btnBuy);
        }
    }


    public StoreAdapter(Context context, ProductClickListener productClickListener) {
        this.context = context;
        this.productClickListener = productClickListener;
    }

    public void setData(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_store, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Product product = productList.get(position);
        holder.name.setText(product.getName());
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        holder.price.setText(currencyVN.format(product.getPrice()));

        Glide.with(context)
                .load(product.getImage())
                .into(holder.thumbnail);
        holder.itemView.setOnClickListener(view ->
                productClickListener.onClick(product));
        holder.btnBuyNow.setOnClickListener(view ->
                productClickListener.onClick(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}


