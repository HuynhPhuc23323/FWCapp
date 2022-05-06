package com.fwc.cosmetic.adapter;

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

import java.util.List;

public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.MyViewHolder> {
    private Context context;
    private List<Integer> imageIdList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tvTitle);
            price = view.findViewById(R.id.tvPrice);
            thumbnail = view.findViewById(R.id.ivThumbnail);
        }
    }


    public HeaderAdapter(Context context, List<Integer> imageIdList) {
        this.context = context;
        this.imageIdList = imageIdList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View StringView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_header, parent, false);

        return new MyViewHolder(StringView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Integer imageId = imageIdList.get(position);
        Glide.with(context)
                .load(imageId)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return imageIdList.size();
    }
}