package com.rctapp.pages;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rctapp.adapter.PricesRecyclerAdapter;
import com.rctapp.databinding.FragmentPricesBinding;
import com.rctapp.models.PricesModel;
import com.rctapp.utils.Api;
import com.rctapp.utils.TrustAllSSL;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PricesFragment extends Fragment {

    public PricesFragment() {
        // Required empty public constructor
    }


    public static PricesFragment newInstance(String param1, String param2) {
        PricesFragment fragment = new PricesFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }
    FragmentPricesBinding binding;
    OkHttpClient client;
    List<PricesModel> pricesModels;
    PricesRecyclerAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPricesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.pricesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        client =new OkHttpClient.Builder()
                .connectTimeout (15, TimeUnit.MINUTES) // in seconds
                .readTimeout(15, TimeUnit.MINUTES)
                .sslSocketFactory(TrustAllSSL.createSSLSocketFactory())
                .hostnameVerifier(new TrustAllSSL.TrustAllHostnameVerifier())
                .build();
        pricesModels = new ArrayList<>();
        getPrices();
    }

    public void getPrices(){
        Request request = new Request.Builder()
                .url(Api.main_url+"/api/v1/price-rate")
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                requireActivity().runOnUiThread(() -> Log.d("runPrices: ", e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                requireActivity().runOnUiThread(() -> {
                    try {
                        String data = response.body().string();
                        JSONObject object = new JSONObject(data);
                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            pricesModels.add(new PricesModel(o.getString("id"), o.getString("region"), "", o.getString("variety"),
                                    o.getInt("grade"), o.getString("date"), o.getInt("active"), o.getInt("price_rate")));
                        }
                        adapter = new PricesRecyclerAdapter(getContext(), pricesModels);
                        binding.pricesRecycler.setAdapter(adapter);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                });

            }
        });
    }
}