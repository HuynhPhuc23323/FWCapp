package com.fwc.cosmetic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fwc.cosmetic.R;
import com.fwc.cosmetic.model.Cart;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private Context context;
    private List<Cart> cartList = new ArrayList();

    private CartClickListener cartClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, quantity, price;
        public ImageView thumbnail, remove;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tvTitle);
            quantity = view.findViewById(R.id.tvQuantity);
            price = view.findViewById(R.id.tvPrice);
            thumbnail = view.findViewById(R.id.ivThumbnail);
            remove = view.findViewById(R.id.ivRemove);
        }
    }


    public CartAdapter(Context context, CartClickListener cartClickListener) {
        this.context = context;
        this.cartClickListener = cartClickListener;
    }

    public void setData(List<Cart> cartList) {
        this.cartList = cartList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Cart cart = cartList.get(position);
        holder.name.setText(cart.getProductName());
        holder.quantity.setText("Quantity: " + cart.getQuantity());
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        holder.price.setText(currencyVN.format(cart.getProductPrice()));

        Glide.with(context)
                .load(cart.getProductImage())
                .into(holder.thumbnail);
        holder.remove.setOnClickListener(view ->
                cartClickListener.onRemoveClick(cart));
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
}


