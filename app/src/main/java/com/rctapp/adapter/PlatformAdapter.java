package com.rctapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rctapp.R;
import com.rctapp.models.PlatformModel;
import com.rctapp.utils.Api;
import com.rctapp.utils.Tools;

import java.util.List;

public class PlatformAdapter extends RecyclerView.Adapter<PlatformAdapter.ViewHolder> {

    private Context mContext;
    private List<PlatformModel> seller;
    private String currentUserID;
    private OnPlatformClick onPlatformClick;

    public PlatformAdapter(Context mContext, List<PlatformModel> seller){
        this.mContext = mContext;
        this.seller = seller;
    }

    @NonNull
    @Override
    public PlatformAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewproject) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.platform_row, parent, false);
        return new PlatformAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PlatformAdapter.ViewHolder holder, int position) {
        final PlatformModel model = seller.get(position);
        holder.tv_name.setText(model.getPlatform_name());
        holder.tv_member.setText(String.format("%s Member", model.getNumber_of_member()));
        if (!model.getImage_path().isEmpty()) {
            String imgURL = Api.main_plain+"/rctimages/rct-upload-encoded/" + model.getImage_path();
            Log.d("onBindViewHolder: ", imgURL);
            Tools.displayImageOriginal(mContext, holder.iv_image, imgURL);
        }
        View.OnClickListener listener = view -> onPlatformClick.onClick(model);
        holder.cv.setOnClickListener(listener);
    }
    @Override
    public int getItemCount() {
        return seller.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        protected TextView tv_name, tv_member;
        protected ImageView iv_image;
        protected View cv;

        public ViewHolder(View view) {
            super(view);
            this.tv_name = view.findViewById(R.id.name);
            this.tv_member = view.findViewById(R.id.members);
            this.iv_image = view.findViewById(R.id.phatform_img);
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

    public void setOnPlatformClick(OnPlatformClick onPlatformClick) {
        this.onPlatformClick = onPlatformClick;
    }

    public OnPlatformClick getOnPlatformClick() {
        return onPlatformClick;
    }
}
