package com.rctapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rctapp.R;
import com.rctapp.models.SellerModel;
import com.rctapp.utils.Tools;

import java.util.List;

public class SellerMultipleSelectAdapter extends RecyclerView.Adapter<SellerMultipleSelectAdapter.ViewHolder> {

    private Context mContext;
    private List<SellerModel> seller;
    private String currentUserID;
    private OnSellerClick onsellerClick;
    private OnCheckBoxClick onCheckBoxClick;

    public SellerMultipleSelectAdapter(Context mContext, List<SellerModel> seller){
        this.mContext = mContext;
        this.seller = seller;
    }

    @NonNull
    @Override
    public SellerMultipleSelectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewproject) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.seller_row_select, parent, false);
        return new SellerMultipleSelectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SellerMultipleSelectAdapter.ViewHolder holder, int position) {
        final SellerModel model = seller.get(position);
        holder.tv_name.setText(model.getFull_name());
        holder.tv_type.setText(model.getApplication_type());
        if (!model.getImage_path().isEmpty()) {
            String imgURL = "https://rctapp.co.tz//rctimages/rct-upload-encoded/" + model.getImage_path();
            Log.d("onBindViewHolder: ", imgURL);
            Tools.displayImageOriginal(mContext, holder.iv_image, imgURL);
        }

        View.OnClickListener listener = view -> onsellerClick.onClick(model);
        View.OnClickListener checkListener = view -> onCheckBoxClick.OnCheckClick(model);
        holder.cv.setOnClickListener(listener);
        holder.checkBox.setOnClickListener(checkListener);

    }
    @Override
    public int getItemCount() {
        return seller.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        protected TextView tv_name, tv_type;
        protected ImageView iv_image;
        protected View cv;
        protected CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            this.tv_name = view.findViewById(R.id.name);
            this.tv_type = view.findViewById(R.id.type);
            this.cv = view.findViewById(R.id.card);
            this.checkBox = view.findViewById(R.id.checkBox);
            this.iv_image = view.findViewById(R.id.iv_profile);

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

    public void setOnCheckBoxClick(OnCheckBoxClick onCheckBoxClick) {
        this.onCheckBoxClick = onCheckBoxClick;
    }

    public OnCheckBoxClick getOnCheckBoxClick() {
        return onCheckBoxClick;
    }
}
