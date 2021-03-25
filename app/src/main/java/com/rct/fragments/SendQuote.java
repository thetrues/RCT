package com.rct.fragments;

import android.app.Dialog;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rct.database.UserPreference;
import com.rct.databinding.SendQuoteBinding;
import com.rct.models.TenderModel;
import com.rct.utils.DataApi;

import org.json.JSONException;

import java.io.IOException;

public class SendQuote extends BottomSheetDialogFragment {
    private BottomSheetBehavior mBehavior;
    private AppBarLayout app_bar_layout;
    TenderModel tenderModel;
    UserPreference userPreference;
    String variety;
    int Grade, Cert;

    public void setTenderModel(TenderModel tenderModel){
        this.tenderModel = tenderModel;
    }

    public void setUserPreference(UserPreference userPreference){
        this.userPreference = userPreference;
    }


    SendQuoteBinding binding;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = SendQuoteBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        binding.btClose2.setOnClickListener(view12 -> dialog.dismiss());

        binding.lytSpacer.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels / 2);

        hideView(binding.appBarLayout);

        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    showView(binding.appBarLayout, getActionBarSize());

                }
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    hideView(binding.appBarLayout);

                }

                if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                    dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        binding.quantity.setText(tenderModel.getQuantity());
        binding.variety.setText(tenderModel.getVariety());
        binding.grade.setText(tenderModel.getGrade());
        binding.dateSent.setText(tenderModel.getDateCreated());
        binding.sendQuote.setOnClickListener(v -> {

            String quantity = binding.quantitySupply.getText().toString().trim();
            String price = binding.price.getText().toString().trim();
            String loca = binding.pickupLocation.getText().toString().trim();
            boolean da = true;
            if (quantity.isEmpty()){
                binding.quantitySupply.setError("Add quantity");
                da = false;
            }
            if (price.isEmpty()){
                binding.price.setError("Add Price");
                da = false;
            }
            if (da){
                try {
                    int Code = DataApi.SendQuote(userPreference.getToken(), tenderModel.getId(), quantity, price, binding.supplyDetails.getText().toString().trim(), loca, getActivity());
                    if (Code == 200){
                        Toast.makeText(getContext(), "Quote sent", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(), "Try again", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();

            }

        });

        binding.cancel.setOnClickListener(view1 -> dialog.dismiss());

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = 0;
        view.setLayoutParams(params);
    }

    private void showView(View view, int size) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = size;
        view.setLayoutParams(params);
    }

    private int getActionBarSize() {
        final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int size = (int) styledAttributes.getDimension(0, 0);
        return size;
    }

}
