package com.rctapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.rctapp.adapter.BuyerMultSelectAdapter;
import com.rctapp.database.UserPreference;
import com.rctapp.databinding.ActivityBuyersBinding;
import com.rctapp.fragments.TenderRequest;
import com.rctapp.fragments.TenderRequestMult;
import com.rctapp.models.BuyerModel;
import com.rctapp.utils.Api;
import com.rctapp.utils.Tools;
import com.rctapp.utils.TrustAllSSL;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.rctapp.R.string.fail_to_load_data;

public class BuyersActivity extends AppCompatActivity {
    OkHttpClient client;
    ProgressDialog progressDialog;
    UserPreference userPreference;
    private List<BuyerModel> buyerModels;
    private BuyerMultSelectAdapter buyerRecyclerAdapter;
    ActivityBuyersBinding binding;
    List<String> selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBuyersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buyersRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        client = new OkHttpClient.Builder()
                .connectTimeout (15, TimeUnit.MINUTES) // in seconds
                .readTimeout(15, TimeUnit.MINUTES)
                .sslSocketFactory(TrustAllSSL.createSSLSocketFactory())
                .hostnameVerifier(new TrustAllSSL.TrustAllHostnameVerifier())
                .build();
        selected = new ArrayList<>();
        Tools.NetPolicy();
        userPreference = new UserPreference(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        binding.giveAllTender.setVisibility(View.GONE);

        getBuyers();

        binding.giveAllTender.setOnClickListener(view -> {
            TenderRequestMult fragment = new TenderRequestMult();
            fragment.setBuyers(selected);
            fragment.setUserPreference(userPreference);
            fragment.show(getSupportFragmentManager(), fragment.getTag());
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                getBuyers(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getBuyers(s);
                return false;
            }
        });
    }

    public void getBuyers(String words){
        progressDialog.show();
        Request request = new Request.Builder()
                .url(Api.main_url+"/api/v1/buyer/all-buyer")
                .addHeader("Authorization", "Bearer " + userPreference.getToken())
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), fail_to_load_data, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Log.d("run: ", e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
               runOnUiThread(() -> {
                    try {
                        progressDialog.dismiss();
                        String data = response.body().string();
                        buyerModels = new ArrayList<>();
                        buyerModels.clear();
                        Log.d("run: ", data);
                        JSONObject ob = new JSONObject(data);
                        JSONArray array = ob.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            if (o.getString("name").toLowerCase().contains(words.toLowerCase())) {
                                buyerModels.add(new BuyerModel(o.getString("id"), o.getString("name"), o.getString("countryName"), o.getString("phone_number"), o.getString("image"), o.getBoolean("active_status")));
                            }
                        }

                        buyerRecyclerAdapter = new BuyerMultSelectAdapter(getApplicationContext(), buyerModels);
                        binding.buyersRecycler.setAdapter(buyerRecyclerAdapter);
                        buyerRecyclerAdapter.setOnBuyerClick(model -> {
                            TenderRequest fragment = new TenderRequest();
                            fragment.setBuyerModel(model);
                            fragment.setUserPreference(userPreference);
                            fragment.show(getSupportFragmentManager(), fragment.getTag());
                        });
                        buyerRecyclerAdapter.setOnBuyerSelectClick(model -> {
                            if (!selected.contains(model.getId())) {
                                selected.add(model.getId());
                            }else{
                                selected.remove(model.getId());
                            }

                            if (selected.size() != 0){
                                binding.giveAllTender.setVisibility(View.VISIBLE);
                            }else{
                                binding.giveAllTender.setVisibility(View.GONE);
                            }
                        });
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public void getBuyers(){
        progressDialog.show();
        Request request = new Request.Builder()
                .url(Api.main_url+"/api/v1/buyer/all-buyer")
                .addHeader("Authorization", "Bearer " + userPreference.getToken())
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), fail_to_load_data, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Log.d("run: ", e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                runOnUiThread(() -> {
                    try {
                        progressDialog.dismiss();
                        String data = response.body().string();
                        buyerModels = new ArrayList<>();
                        Log.d("run: ", data);
                        JSONObject ob = new JSONObject(data);
                        JSONArray array = ob.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            buyerModels.add(new BuyerModel(o.getString("id"), o.getString("name"), o.getString("countryName"), o.getString("phone_number"), o.getString("image"), o.getBoolean("active_status")));
                        }

                        buyerRecyclerAdapter = new BuyerMultSelectAdapter(getApplicationContext(), buyerModels);
                        binding.buyersRecycler.setAdapter(buyerRecyclerAdapter);
                        buyerRecyclerAdapter.setOnBuyerClick(model -> {
                            TenderRequest fragment = new TenderRequest();
                            fragment.setBuyerModel(model);
                            fragment.setUserPreference(userPreference);
                            fragment.show(getSupportFragmentManager(), fragment.getTag());
                        });
                        buyerRecyclerAdapter.setOnBuyerSelectClick(model -> {
                            if (!selected.contains(model.getId())) {
                                selected.add(model.getId());
                            }else{
                                selected.remove(model.getId());
                            }

                            if (selected.size() != 0){
                                binding.giveAllTender.setVisibility(View.VISIBLE);
                            }else{
                                binding.giveAllTender.setVisibility(View.GONE);
                            }
                        });
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

}