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
import com.rct.models.BuyerModel;
import com.rct.utils.Tools;

import java.util.List;

public class BuyerRecyclerAdapter extends RecyclerView.Adapter<BuyerRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<BuyerModel> buyer;
    private String currentUserID;
    private OnBuyerClick onBuyerClick;


    public BuyerRecyclerAdapter(Context mContext, List<BuyerModel> buyer){
        this.mContext = mContext;
        this.buyer = buyer;
    }

    @NonNull
    @Override
    public BuyerRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewproject) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.buyer_row, parent, false);
        return new BuyerRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BuyerRecyclerAdapter.ViewHolder holder, int position) {
        final BuyerModel model = buyer.get(position);
        holder.tv_name.setText(Tools.toCamelCase(model.getName()));
       View.OnClickListener listener = view -> onBuyerClick.onClick(model);
        holder.cv.setOnClickListener(listener);

    }
    @Override
    public int getItemCount() {
        return buyer.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        protected TextView tv_name, tv_type;
        protected ImageView iv_image;
        protected View cv;

        public ViewHolder(View view) {
            super(view);
            this.tv_name = view.findViewById(R.id.name);
            this.tv_type = view.findViewById(R.id.active);
            this.cv = view.findViewById(R.id.card);

        }
    }


    public void removeAt(int position) {
        try {
            buyer.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, buyer.size());
        }catch (Exception e){

        }
    }

    public void setOnBuyerClick(OnBuyerClick onBuyerClick) {
        this.onBuyerClick = onBuyerClick;
    }

    public OnBuyerClick getOnBuyerClick() {
        return onBuyerClick;
    }
}

