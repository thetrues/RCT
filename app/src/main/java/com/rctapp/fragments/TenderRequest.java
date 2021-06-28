package com.rctapp.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
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
import com.rctapp.R;
import com.rctapp.database.UserPreference;
import com.rctapp.databinding.FragmentTenderRequestDialogBinding;
import com.rctapp.models.BuyerModel;
import com.rctapp.utils.DataApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class TenderRequest extends BottomSheetDialogFragment {
    private BottomSheetBehavior mBehavior;
    private AppBarLayout app_bar_layout;
    BuyerModel buyerModel;
    UserPreference userPreference;
    String variety;
    int Cert = 0;
    int Graded = 0;
    int Grade;
    Uri resultUri;
    private final int PICK_FILE_REQUEST = 1;

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

           binding.addBatchCertification.setOnClickListener(view13 -> {
               if (binding.isCertified.isChecked()) {
               Intent intent = new Intent();
               intent.setType("image/*");
               intent.setAction(Intent.ACTION_GET_CONTENT);
               startActivityForResult(Intent.createChooser(intent, getString(R.string.pick_image)), PICK_FILE_REQUEST);
               }else{
                   Toast.makeText(getContext(), "You need to be certified", Toast.LENGTH_SHORT).show();
               }
           });

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

        binding.spinnerGrade.setVisibility(View.GONE);
        binding.isGraded.setOnClickListener(view2 -> {
            Graded = 1;
            binding.spinnerGrade.setVisibility(View.VISIBLE);
        });
        binding.isNotGraded.setOnClickListener(view22 -> {
            Graded = 0;
            binding.spinnerGrade.setVisibility(View.GONE);
        });
        binding.addBatchCertification.setVisibility(View.GONE);
        binding.isCertified.setOnClickListener(view23 -> {
            Cert = 1;
            binding.addBatchCertification.setVisibility(View.VISIBLE);
        });
        binding.notCertified.setOnClickListener(view24 -> {
            Cert = 0;
            binding.addBatchCertification.setVisibility(View.GONE);
        });

        binding.sendRequest.setOnClickListener(view1 -> {
            try {
                int quantity = Integer.parseInt(binding.quantity.getText().toString().trim());
                int price = Integer.parseInt(binding.sellingPrice.getText().toString().trim());
                String details = binding.details.getText().toString().trim();


                String location = Objects.requireNonNull(binding.location.getText()).toString().trim();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            resultUri = data.getData();
        }
    }

    
}