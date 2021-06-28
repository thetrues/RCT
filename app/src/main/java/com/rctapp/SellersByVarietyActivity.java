package com.rctapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.rctapp.adapter.SellerMultipleSelectAdapter;
import com.rctapp.database.UserPreference;
import com.rctapp.databinding.ActivitySellersByVarietyBinding;
import com.rctapp.fragments.GiveTender;
import com.rctapp.models.SellerModel;
import com.rctapp.utils.Api;
import com.rctapp.utils.DataApi;
import com.rctapp.utils.TrustAllSSL;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.rctapp.R.string.fail_to_load_data;

public class SellersByVarietyActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    OkHttpClient client;
    SellerMultipleSelectAdapter sellersAdapter;
    private List<SellerModel> sellerModelList;
    ActivitySellersByVarietyBinding binding;
    UserPreference userPreference;
    List<String> sellerIds;
    String gradeSelected = "1";
    String variety;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySellersByVarietyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.sellersRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        userPreference = new UserPreference(this);
        client = new OkHttpClient.Builder()
                .connectTimeout (15, TimeUnit.MINUTES) // in seconds
                .readTimeout(15, TimeUnit.MINUTES)
                .sslSocketFactory(TrustAllSSL.createSSLSocketFactory())
                .hostnameVerifier(new TrustAllSSL.TrustAllHostnameVerifier())
                .build();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        sellerIds = new ArrayList<>();
        binding.varietyName.setText(getIntent().getExtras().getString("variety"));
        getSellers();
        binding.giveAllTender.setVisibility(View.GONE);
        binding.giveAllTender.setOnClickListener(view -> GiveTenderMany(sellerIds));
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                getSellers(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getSellers(s);
                return false;
            }
        });
    }


    public void getSellers(String words) {
        progressDialog.show();
        Request request = new Request.Builder()
                .cacheControl(new CacheControl.Builder()
                        .maxStale(60, TimeUnit.MINUTES)
                        .build())
                .url(Api.main_url + Api.sellers)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                try {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), fail_to_load_data, Toast.LENGTH_SHORT).show();
                }catch (Exception ex){
                    Log.d("onFailure: " , ex.getMessage());
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                runOnUiThread(() -> {
                    try {
                        progressDialog.dismiss();
                        String data = response.body().string();
                        JSONObject object = new JSONObject(data);
                        sellerModelList = new ArrayList<>();
                        JSONObject object1 = object.getJSONObject("data");
                        JSONArray array = object1.getJSONArray("sellerInformations");
                        Log.d("onResponse: ", data);
                        sellerModelList.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject s = array.getJSONObject(i);
                            if (s.getString("full_name").toLowerCase().contains(words.toLowerCase())) {
                                sellerModelList.add(new SellerModel(s.getString("id"), s.getString("category"), s.getString("full_name"),
                                        s.getString("dial_code"), s.getString("phone_number"), s.getString("platform_name"), s.getString("platform_leader"),
                                        s.getString("location"), s.getString("card_type"), s.getString("application_type"),
                                        s.getString("is_tbs_certified"), s.getString("image_path"), s.getString("certificate_path"),
                                        s.getString("card_path"), s.getInt("is_graded")));
                            }
                        }
                        sellersAdapter = new SellerMultipleSelectAdapter(getApplicationContext(), sellerModelList);
                        binding.sellersRecycler.setAdapter(sellersAdapter);
                        sellersAdapter.setOnCheckBoxClick(model -> {
                            if (!sellerIds.contains(model.getId())) {
                                sellerIds.add(model.getId());
                            }else {
                                sellerIds.remove(model.getId());
                            }
                            Log.d("OnCheckClick: ", sellerIds.toString());
                            if (sellerIds.size() !=0){
                                binding.giveAllTender.setVisibility(View.VISIBLE);
                            }else {
                                binding.giveAllTender.setVisibility(View.GONE);
                            }
                        });
                        sellersAdapter.setOnSellerClick(model -> SellerDialog(model));

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        Log.d("onResponse: ", e.getMessage());
                    }

                });
            }
        });
    }


    public void getSellers() {
        progressDialog.show();
        Request request = new Request.Builder()
                .cacheControl(new CacheControl.Builder()
                        .maxStale(60, TimeUnit.MINUTES)
                        .build())
                .url(Api.main_url + Api.sellers)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                try {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), fail_to_load_data, Toast.LENGTH_SHORT).show();
                }catch (Exception ex){
                    Log.d("onFailure: " , ex.getMessage());
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                runOnUiThread(() -> {
                    try {
                        progressDialog.dismiss();
                        String data = response.body().string();
                        JSONObject object = new JSONObject(data);
                        sellerModelList = new ArrayList<>();
                        JSONObject object1 = object.getJSONObject("data");
                        JSONArray array = object1.getJSONArray("sellerInformations");
                        Log.d("onResponse: ", data);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject s = array.getJSONObject(i);
                            sellerModelList.add(new SellerModel(s.getString("id"), s.getString("category"), s.getString("full_name"),
                                    s.getString("dial_code"), s.getString("phone_number"), s.getString("platform_name"), s.getString("platform_leader"),
                                    s.getString("location"), s.getString("card_type"), s.getString("application_type"),
                                    s.getString("is_tbs_certified"), s.getString("image_path"), s.getString("certificate_path"),
                                    s.getString("card_path"), s.getInt("is_graded")));
                        }
                        sellersAdapter = new SellerMultipleSelectAdapter(getApplicationContext(), sellerModelList);
                        binding.sellersRecycler.setAdapter(sellersAdapter);
                        sellersAdapter.setOnCheckBoxClick(model -> {
                            if (!sellerIds.contains(model.getId())) {
                                sellerIds.add(model.getId());
                            }else {
                                sellerIds.remove(model.getId());
                            }
                            Log.d("OnCheckClick: ", sellerIds.toString());
                            if (sellerIds.size() !=0){
                                binding.giveAllTender.setVisibility(View.VISIBLE);
                            }else {
                                binding.giveAllTender.setVisibility(View.GONE);
                            }
                        });
                        sellersAdapter.setOnSellerClick(model -> SellerDialog(model));

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        Log.d("onResponse: ", e.getMessage());
                    }

                });
            }
        });
    }


    public void SellerDialog(SellerModel model) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View mView = getLayoutInflater().inflate(R.layout.seller_details, null);
        dialog.setContentView(mView);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ((TextView) dialog.findViewById(R.id.names)).setText(model.getFull_name());
        ((TextView) dialog.findViewById(R.id.phone)).setText(model.getPhone_number());
        ((TextView) dialog.findViewById(R.id.platformName)).setText(model.getPlatform_name());
        ((TextView) dialog.findViewById(R.id.location)).setText(model.getLocation());
        ((TextView) dialog.findViewById(R.id.category)).setText(model.getApplication_type());
        ((TextView) dialog.findViewById(R.id.size)).setText(model.getCategory());
        dialog.findViewById(R.id.bt_close).setOnClickListener(view -> dialog.dismiss());
        dialog.findViewById(R.id.giveTender).setOnClickListener((View.OnClickListener) view -> {
            if (userPreference.getLoggedIn() && userPreference.getBuyer()) {
                GiveTender giveTender = new GiveTender();
                giveTender.setSellerModel(model);
                giveTender.setUserPreference(userPreference);
                giveTender.show(getSupportFragmentManager(), giveTender.getTag());
                dialog.dismiss();
            } else {
                Login();
            }

        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void GiveTenderMany(List<String> ids) {
        final Dialog tenderDialog = new Dialog(this);
        tenderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        tenderDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View mView = getLayoutInflater().inflate(R.layout.give_tender_dialog, null);
        tenderDialog.setContentView(mView);
        tenderDialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(tenderDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Spinner gr = tenderDialog.findViewById(R.id.spinnerGrade);
        Spinner vr = tenderDialog.findViewById(R.id.spinnerVariety);
        vr.setVisibility(View.GONE);
        String[] grade = {"1", "2", "3"};
        ((TextView) tenderDialog.findViewById(R.id.names)).setText(String.format("Give %s Sellers Tender", ids.size()));

        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, grade);
        gr.setAdapter(gradeAdapter);
        gr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gradeSelected = grade[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> varietyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DataApi.getVariety(this));
        vr.setAdapter(varietyAdapter);
        vr.setSelection(getIntent().getExtras().getInt("variety_id"));
        tenderDialog.findViewById(R.id.giveTender).setOnClickListener(view -> {
            String quantity = ((EditText) tenderDialog.findViewById(R.id.quantity)).getText().toString().trim();
            String pickup = ((EditText) tenderDialog.findViewById(R.id.location)).getText().toString().trim();
            Log.d("onClick: ", gradeSelected);
            JSONObject object = new JSONObject();
            try {
                JSONArray array = new JSONArray();
                for (int i = 0; i < ids.size(); i++) {
                    JSONObject usd = new JSONObject();
                    usd.put("id", ids.get(i));
                    array.put(usd);
                }
                variety = DataApi.getVariety(getApplicationContext()).get(getIntent().getExtras().getInt("variety_id"));
                JSONObject seller_selection = new JSONObject();
                seller_selection.put("seller_id", array);
                object.put("quantity", quantity);
                object.put("grade", gradeSelected);
                object.put("location", pickup);
                object.put("variety", variety);
                object.put("seller_selection", seller_selection);
                Log.d("onClick1: ", object.toString());
                if (!quantity.isEmpty() && !pickup.isEmpty()) {
                    if (userPreference.getLoggedIn()) {
                        DataApi.GiveTender(object, this, userPreference.getToken());
                        Log.d("GiveTenderMany: ", object.toString());
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Login();
                    }
                    tenderDialog.dismiss();
                } else {
                    Toast.makeText(this, "Fill all inputs", Toast.LENGTH_SHORT).show();
                    ((EditText) tenderDialog.findViewById(R.id.quantity)).setError("Please add quantity");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        tenderDialog.create();
        tenderDialog.show();
    }


    public void Login(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(R.string.you_must_login_to_continue);
        alert.setPositiveButton(R.string.login, (dialogInterface, i) -> startActivity(new Intent(this, Auth.class))).setNegativeButton("Cancel", (dialogInterface, i) -> { });
        alert.create().show();
    }
}