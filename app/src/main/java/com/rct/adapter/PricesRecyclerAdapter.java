package com.rct.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rct.R;
import com.rct.models.PricesModel;

import java.util.List;

public class PricesRecyclerAdapter extends RecyclerView.Adapter<PricesRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<PricesModel> price;
    private String currentUserID;



    public PricesRecyclerAdapter(Context mContext, List<PricesModel> price) {
        this.mContext = mContext;
        this.price = price;
    }

    @NonNull
    @Override
    public PricesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewproject) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.price_row, parent, false);
        return new PricesRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PricesRecyclerAdapter.ViewHolder holder, int position) {
        final PricesModel model = price.get(position);
        holder.tv_title.setText(model.getVariety());
        holder.tv_region.setText(model.getRegion());
        holder.tv_dateTime.setText(model.getDate());
        holder.tv_price.setText(String.format("Price: %s", String.valueOf(model.getPrice_rate())));


    }

    @Override
    public int getItemCount() {
        return price.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView tv_title, tv_price, tv_active, tv_dateTime, tv_region;
        protected View cv;

        public ViewHolder(View view) {
            super(view);
            this.tv_title = view.findViewById(R.id.title);
            this.tv_price = view.findViewById(R.id.price);
            this.tv_active = view.findViewById(R.id.active);
            this.tv_dateTime = view.findViewById(R.id.dateTime);
            this.tv_region = view.findViewById(R.id.region);
            this.cv = view.findViewById(R.id.cv_card);

        }
    }


    public void removeAt(int position) {
        try {
            price.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, price.size());
        } catch (Exception e) {

        }
    }

}