package com.rctapp.pages.notificattion;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rctapp.adapter.QuotationRecyclerAdapter;
import com.rctapp.chat.FirebaseChat;
import com.rctapp.database.UserPreference;
import com.rctapp.databinding.FragmentQouteNotificationBinding;
import com.rctapp.models.ChatModel;
import com.rctapp.models.QuoteModel;
import com.rctapp.utils.Api;
import com.rctapp.utils.DataApi;
import com.rctapp.utils.TrustAllSSL;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class QouteNotification extends Fragment {

    public QouteNotification() {
        // Required empty public constructor
    }


    public static QouteNotification newInstance(String param1, String param2) {
        QouteNotification fragment = new QouteNotification();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    FragmentQouteNotificationBinding binding;
    OkHttpClient client;
    UserPreference userPreference;
    private List<QuoteModel> modelList;
    private QuotationRecyclerAdapter adapter;
    DatabaseReference mData, mData2;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQouteNotificationBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.quotationRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        client = new OkHttpClient.Builder()
                .connectTimeout (15, TimeUnit.MINUTES) // in seconds
                .readTimeout(15, TimeUnit.MINUTES)
                .sslSocketFactory(TrustAllSSL.createSSLSocketFactory())
                .hostnameVerifier(new TrustAllSSL.TrustAllHostnameVerifier())
                .build();
        userPreference = new UserPreference(getContext());
        if (userPreference.getSeller() && userPreference.getRole().toLowerCase().equals("seller")) {
            getQuoteSeller();
        }else{
            getQuotesBuyer();
        }
    }

    public void getQuotesBuyer(){
        Request request = new Request.Builder()
                .url(Api.main_url+Api.get_all_quotes_buyer)
                .addHeader("Authorization", "Bearer "+userPreference.getToken())
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                requireActivity().runOnUiThread(() -> Log.d("run: ", e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                requireActivity().runOnUiThread(() -> {
                    try {
                        modelList = new ArrayList<>();
                        modelList.clear();
                        String data = response.body().string();
                        JSONObject object = new JSONObject(data);
                        JSONArray array = object.getJSONArray("data");
                        for (int i=0; i<array.length(); i++){
                            JSONObject o = array.getJSONObject(i);
                            modelList.add(new QuoteModel(o.getString("id"), o.getString("tender_id"), o.getString("seller_id"), o.getString("supply_details"),
                                    o.getInt("supply_quantity"), o.getInt("supply_price"), o.getInt("active")));
                        }
                        Collections.reverse(modelList);
                        adapter = new QuotationRecyclerAdapter(getActivity(), modelList);
                        binding.quotationRecycler.setAdapter(adapter);
                        adapter.setOnQuoteClick(model -> {

                            if(model.getActive() == 2) {
                                GotToChat(model);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Initiate Chat?");
                                builder.setPositiveButton("Initiate", (dialogInterface, i) -> {
                                    String sellerData = null;
                                    try {
                                        sellerData = DataApi.getUserData(model.getSeller_id(), userPreference);
                                    JSONObject u = new JSONObject(sellerData);
                                    JSONObject ui = u.getJSONObject("data");
                                    JSONObject uui = ui.getJSONObject("user");
                                    Map<String, Object> chat = new HashMap<>();
                                    chat.put("seller_id", model.getSeller_id());
                                    chat.put("seller",  uui.getString("name"));
                                    chat.put("seller_image_path",  ui.getString("profile_image_path"));
                                    chat.put("buyer_id", userPreference.getUserId());
                                    chat.put("buyer", userPreference.getName());
                                    chat.put("buyer_image_path", userPreference.getImage());
                                    chat.put("message_count", 0);
                                    chat.put("quote_id", model.getId());
                                    chat.put("supply_quantity", model.getSupply_quantity());
                                    chat.put("supply_price", model.getSupply_price());
                                    chat.put("supply_details", model.getSupply_details());
                                    chat.put("chat_status", true);
                                    chat.put("expiration_status", true);
                                    chat.put("update_time", System.currentTimeMillis());
                                    mData = FirebaseDatabase.getInstance().getReference().child("messenger").child(userPreference.getUserId()).child(model.getId());
                                    mData2 = FirebaseDatabase.getInstance().getReference().child("messenger").child(model.getSeller_id()).child(model.getId());
                                    mData.setValue(chat);
                                    mData2.setValue(chat);
                                    } catch (IOException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                    JSONObject ob = new JSONObject();
                                    try {
                                        ob.put("seller_id", model.getSeller_id());
                                        ob.put("buyer_id", userPreference.getUserId());
                                        ob.put("quote_id", model.getId());
                                        String resp = DataApi.initChat(ob, userPreference.getToken());
                                        Log.d("onResponse: ", resp);
                                        JSONObject app = new JSONObject();
                                        app.put("supply_quantity", model.getSupply_quantity());
                                        app.put("supply_price", model.getSupply_price());
                                        app.put("supply_pickup_location", model.getSupply_details());
                                        app.put("supply_details", model.getSupply_details());

                                        String approve = DataApi.approveQuote(app, userPreference.getToken(), model.getId());
                                        Log.d("onResponse: ", approve);
                                        Toast.makeText(getContext(), "Chat initiated", Toast.LENGTH_SHORT).show();
                                        getQuotesBuyer();
                                    } catch (JSONException | IOException e) {
                                        e.printStackTrace();
                                        Log.d("onClick: ", e.getMessage());
                                    }
                                }).setNegativeButton("Cancel", (dialogInterface, i) -> {

                                });
                                builder.create().show();
                            }


                        });
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public void getQuoteSeller(){
        Request request = new Request.Builder()
                .url(Api.main_url+"/api/v1/quote/seller")
                .addHeader("Authorization", "Bearer "+userPreference.getToken())
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                requireActivity().runOnUiThread(() -> Log.d("run: ", e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                requireActivity().runOnUiThread(() -> {
                    try {
                        modelList = new ArrayList<>();
                        String data = response.body().string();
                        Log.d("onResponseQuotes: ", data);
                        JSONObject object = new JSONObject(data);
                        JSONArray array = object.getJSONArray("data");
                        for (int i=0; i<array.length(); i++){
                            JSONObject o = array.getJSONObject(i);
                            modelList.add(new QuoteModel(o.getString("id"), o.getString("tender_id"), o.getString("seller_id"), o.getString("supply_details"),
                                    o.getInt("supply_quantity"), o.getInt("supply_price"), o.getInt("active")));
                        }
                        Collections.reverse(modelList);
                        adapter = new QuotationRecyclerAdapter(getActivity(), modelList);
                        binding.quotationRecycler.setAdapter(adapter);
                        adapter.setOnQuoteClick(model -> {
                            if (model.getActive() == 2){
                                GotToChat(model);
                            }else{
                                Toast.makeText(getContext(), "Please wait for buyer to accept your quote", Toast.LENGTH_SHORT).show();
                            }

                        });
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        Log.d("onResponseQuote: ", e.getMessage());
                    }
                });
            }
        });
    }

    public void GotToChat(QuoteModel model){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Quotation Accepted");
        builder.setMessage(model.getSupply_details());
            mData = FirebaseDatabase.getInstance().getReference().child("messenger").child(userPreference.getUserId()).child(model.getId());
            mData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    builder.setPositiveButton("Go to chat", (dialogInterface, i) -> {
                    ChatModel chatModel = snapshot.getValue(ChatModel.class);
                        Intent s = new Intent(getContext(), FirebaseChat.class);
                        s.putExtra("chatModel", chatModel);
                        startActivity(s);
                    }).setNegativeButton("Not now", (dialogInterface, i) -> {

                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        builder.create().show();
    }
}