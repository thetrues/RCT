package com.rctapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rctapp.R;
import com.rctapp.models.TenderModel;

import java.util.List;

public class TenderRecyclerAdapter extends RecyclerView.Adapter<TenderRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<TenderModel> tender;
    private String currentUserID;
    private OnTenderClick onTenderClick;

    public TenderRecyclerAdapter(Context mContext, List<TenderModel> tender){
        this.mContext = mContext;
        this.tender = tender;
    }

    @NonNull
    @Override
    public TenderRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewproject) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tender_row, parent, false);
        return new TenderRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TenderRecyclerAdapter.ViewHolder holder, int position) {
        final TenderModel model = tender.get(position);
        holder.tv_variety.setText(model.getVariety());
        holder.tv_quantity.setText(String.format("%s kgs", model.getQuantity()));
        holder.dateCreated.setText(model.getDateCreated());

        View.OnClickListener listener = view -> onTenderClick.OnTenderClick(model);
        holder.cv.setOnClickListener(listener);
        holder.dateCreated.setOnClickListener(listener);

    }
    @Override
    public int getItemCount() {
        return tender.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        protected TextView tv_variety, tv_quantity, dateCreated;
        protected View cv;

        public ViewHolder(View view) {
            super(view);
            this.tv_quantity= view.findViewById(R.id.quantity);
            this.tv_variety = view.findViewById(R.id.variety);
            this.dateCreated = view.findViewById(R.id.dateSent);
            this.cv = view.findViewById(R.id.cv_card);
        }
    }


    public void removeAt(int position) {
        try {
            tender.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, tender.size());
        }catch (Exception e){

        }
    }

    public OnTenderClick getOnTenderClick() {
        return onTenderClick;
    }

    public void setOnTenderClick(OnTenderClick onTenderClick) {
        this.onTenderClick = onTenderClick;
    }
}
