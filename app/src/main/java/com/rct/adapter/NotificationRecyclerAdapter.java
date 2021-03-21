package com.rct.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.rct.R;
import com.rct.models.NotificationModel;
import java.util.List;

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<NotificationModel> notificate;
    private String currentUserID;
    private OnNotificationClick onNotificationClick;

    public NotificationRecyclerAdapter(Context mContext, List<NotificationModel> notificate){
        this.mContext = mContext;
        this.notificate = notificate;
    }

    @Override
    public NotificationRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewproject) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_row, parent, false);
        return new NotificationRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotificationRecyclerAdapter.ViewHolder holder, int position) {
        final NotificationModel model = notificate.get(position);
        holder.variety.setText(model.getVariety().toUpperCase());
        holder.quantity.setText(String.format("Quantity: %s", model.getQuantity()));
        holder.price.setText(String.format("Price: %s", model.getSelling_price()));
        holder.message.setText(model.getExtra_details());
        holder.location.setText(model.getPickup_location());


        View.OnClickListener listener = view -> onNotificationClick.onNotificationClick(model);
        holder.cv.setOnClickListener(listener);
    }


    @Override
    public int getItemCount() {
        return notificate.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        protected View cv;
        protected TextView variety, quantity, price, message, location;
        public ViewHolder(View view) {
            super(view);
            this.variety = view.findViewById(R.id.variety);
            this.quantity = view.findViewById(R.id.quantity);
            this.price = view.findViewById(R.id.price);
            this.message = view.findViewById(R.id.message);
            this.location = view.findViewById(R.id.location);
            this.cv = view.findViewById(R.id.cv_card);
        }
    }
    public void removeAt(int position) {
        try {
            notificate.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, notificate.size());
        }catch (Exception e){

        }
    }

    public void setOnNotificationClick(OnNotificationClick onNotificationClick) {
        this.onNotificationClick = onNotificationClick;
    }

    public OnNotificationClick getOnNotificationClick() {
        return onNotificationClick;
    }
}