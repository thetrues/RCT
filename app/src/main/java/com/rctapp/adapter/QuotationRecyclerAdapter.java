package com.rctapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rctapp.R;
import com.rctapp.models.QuoteModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class QuotationRecyclerAdapter extends RecyclerView.Adapter<QuotationRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<QuoteModel> quote;
    private String currentUserID;
    private OnQuoteClick onQuoteClick;

    public QuotationRecyclerAdapter(Context mContext, List<QuoteModel> quote){
        this.mContext = mContext;
        this.quote = quote;
    }

    @NonNull
    @Override
    public QuotationRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewproject) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.quote_row, parent, false);
        return new QuotationRecyclerAdapter.ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final QuotationRecyclerAdapter.ViewHolder holder, int position) {
        final QuoteModel model = quote.get(position);

        holder.message.setText(model.getSupply_details());
        holder.quantity.setText(String.format("Quantity: %s", model.getSupply_quantity()));
        holder.price.setText(String.format("Price: %s", model.getSupply_price()));
        if (model.getActive() == 2){
            holder.cv.setBackgroundColor(Color.parseColor("#70B77E"));
        }

        View.OnClickListener listener = view -> {
            try {
                onQuoteClick.OnQuoteClick(model);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        };
        holder.name.setOnClickListener(listener);
        holder.cv.setOnClickListener(listener);
    }


    @Override
    public int getItemCount() {
        return quote.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        protected View cv;
        protected TextView name, quantity, price, message, location;
        public ViewHolder(View view) {
            super(view);
            this.name = view.findViewById(R.id.names);
            this.quantity = view.findViewById(R.id.quantity);
            this.price = view.findViewById(R.id.price);
            this.message = view.findViewById(R.id.message);
            this.cv = view.findViewById(R.id.cv_card);
        }
    }
    public void removeAt(int position) {
        try {
            quote.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, quote.size());
        }catch (Exception e){

        }
    }

    public void setOnQuoteClick(OnQuoteClick onQuoteClick) {
        this.onQuoteClick = onQuoteClick;
    }

    public OnQuoteClick getOnQuoteClick() {
        return onQuoteClick;
    }
}