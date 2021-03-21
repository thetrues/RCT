package com.rct.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rct.R;
import com.rct.models.SellerModel;

import java.util.List;

public class SellersAdapter extends RecyclerView.Adapter<SellersAdapter.ViewHolder> {

    private Context mContext;
    private List<SellerModel> seller;
    private String currentUserID;
    private OnSellerClick onsellerClick;


    public SellersAdapter(Context mContext, List<SellerModel> seller){
        this.mContext = mContext;
        this.seller = seller;
    }

    @NonNull
    @Override
    public SellersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewproject) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.seller_row, parent, false);
        return new SellersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SellersAdapter.ViewHolder holder, int position) {
        final SellerModel model = seller.get(position);
        holder.tv_name.setText(model.getFull_name());
        holder.tv_type.setText(model.getApplication_type());
        View.OnClickListener listener = view -> onsellerClick.onClick(model);
        holder.cv.setOnClickListener(listener);

    }
    @Override
    public int getItemCount() {
        return seller.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        protected TextView tv_name, tv_type;
        protected ImageView  iv_image;
        protected View cv;

        public ViewHolder(View view) {
            super(view);
            this.tv_name = view.findViewById(R.id.name);
            this.tv_type = view.findViewById(R.id.type);
            this.cv = view.findViewById(R.id.card);

        }
    }


    public void removeAt(int position) {
        try {
            seller.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, seller.size());
        }catch (Exception e){

        }
    }

    public void setOnSellerClick(OnSellerClick onsellerClick) {
        this.onsellerClick = onsellerClick;
    }

    public OnSellerClick getOnSellerClick() {
        return onsellerClick;
    }
}
