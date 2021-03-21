package com.rct.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rct.R;
import com.rct.models.CountryModel;
import com.rct.models.RoleModel;

import java.util.List;

public class RoleAdapter extends ArrayAdapter<String> {
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<RoleModel> items;
    private final int mResource;

    public RoleAdapter(@NonNull Context context, @LayoutRes int resource,
                                 @NonNull List objects) {
        super(context, resource, 0, objects);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull
    View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @SuppressLint("DefaultLocale")
    private View createItemView(int position, View convertView, ViewGroup parent) {
        final View view = mInflater.inflate(mResource, parent, false);
        TextView name = view.findViewById(R.id.name);
        RoleModel model = items.get(position);
        if (model.getRole().equals("regular")){
            name.setText("Buyer");
        }else{
            name.setText("Seller");
        }

        return view;
    }
}
