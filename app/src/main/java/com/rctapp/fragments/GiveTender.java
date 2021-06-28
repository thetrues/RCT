package com.rctapp.fragments;

import android.app.Dialog;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rctapp.database.UserPreference;
import com.rctapp.databinding.GiveTenderBinding;
import com.rctapp.models.SellerModel;
import com.rctapp.utils.DataApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GiveTender extends BottomSheetDialogFragment {
    private BottomSheetBehavior mBehavior;
    private AppBarLayout app_bar_layout;
    SellerModel sellerModel;
    UserPreference userPreference;
    GiveTenderBinding binding;
    String gradeSelected, variety;

    public void setSellerModel(SellerModel sellerModel){
        this.sellerModel = sellerModel;
    }

    public void setUserPreference(UserPreference userPreference){
        this.userPreference = userPreference;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = GiveTenderBinding.inflate(getLayoutInflater());
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

        String[] grade = {"1", "2", "3"};


       binding.sellerName.setText(sellerModel.getFull_name());

        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, grade);
        binding.spinnerGrade.setAdapter(gradeAdapter);
        binding.spinnerGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gradeSelected = grade[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> veriAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, DataApi.getVariety(getContext()));
        binding.spinnerVariety.setAdapter(veriAdapter);
        binding.spinnerVariety.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                variety = DataApi.getVariety(getContext()).get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.giveTender2.setOnClickListener(v -> {
            String quantity = binding.quantity.getText().toString().trim();
            String pickup = binding.location.getText().toString().trim();
            Log.d("onClick: ", gradeSelected);
            JSONObject object = new JSONObject();
            try {
                JSONObject usd = new JSONObject();
                usd.put("id", sellerModel.getId());
                JSONArray array = new JSONArray();
                array.put(usd);
                JSONObject seller_selection = new JSONObject();
                seller_selection.put("seller_id", array);
                object.put("quantity", quantity);
                object.put("grade", gradeSelected);
                object.put("location", pickup);
                object.put("variety", variety);
                object.put("seller_selection", seller_selection);
                Log.d("onClick1: ", object.toString());
                if (!quantity.isEmpty() && !variety.isEmpty() && !pickup.isEmpty()){
                    DataApi.GiveTender(object, getContext(), userPreference.getToken());
                }else {
                    Toast.makeText(getContext(), "Fill all inputs", Toast.LENGTH_SHORT).show();
                    binding.quantity.setError("Please add quantity");
                }


                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });
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
