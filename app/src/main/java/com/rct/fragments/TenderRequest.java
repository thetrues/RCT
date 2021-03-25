package com.rct.fragments;

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
import com.rct.database.UserPreference;
import com.rct.databinding.FragmentTenderRequestDialogBinding;
import com.rct.models.BuyerModel;
import com.rct.utils.DataApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class TenderRequest extends BottomSheetDialogFragment {
    private BottomSheetBehavior mBehavior;
    private AppBarLayout app_bar_layout;
    BuyerModel buyerModel;
    UserPreference userPreference;
    String variety;
    int Grade, Cert;

    public void setBuyerModel(BuyerModel buyerModel){
        this.buyerModel = buyerModel;
    }

    public void setUserPreference(UserPreference userPreference){
        this.userPreference = userPreference;
    }

    public static TenderRequest newInstance() {
        final TenderRequest fragment = new TenderRequest();
        final Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    FragmentTenderRequestDialogBinding binding;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = FragmentTenderRequestDialogBinding.inflate(getLayoutInflater());
         View view = binding.getRoot();
        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
        binding.buyerName.setText(buyerModel.getName());
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

        String[] grades = {"grade 1", "grade 2", "grade 3"};
        String[] cert = {"certified", "not certified"};
        String[] veri = {"Supper Kyelar", "Kilosa One"};
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
        ArrayAdapter<String> g = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, grades);
        ArrayAdapter<String> cet = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, cert);
        binding.spinnerGrade.setAdapter(g);
        binding.spinnerGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Grade = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerCertified.setAdapter(cet);
        binding.spinnerCertified.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cert = i + 1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int quantity = Integer.parseInt(binding.quantity.getText().toString().trim());
                    int price = Integer.parseInt(binding.sellingPrice.getText().toString().trim());
                    String details = binding.details.getText().toString().trim();

                    String location = binding.location.getText().toString().trim();
                    JSONObject ob = new JSONObject();
                    ob.put("buyer_id", buyerModel.getId());
                    JSONArray array = new JSONArray();
                    array.put(ob);
                    JSONObject obj = new JSONObject();
                    obj.put("ids", array);

                    int response = DataApi.RequestTender(userPreference.getToken(), quantity, price, 1, Grade, Cert, location, details, variety, obj);
                    Log.d( "onClick: ", String.valueOf(response));
                    if (response == 200)
                        Toast.makeText(getContext(), "Request sent", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), "Failed try again", Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    Log.d("onClick: ", e.getMessage());
                }
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