package com.rctapp.pages;

import android.content.Intent;
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

import com.rctapp.adapter.ChatRecyclerAdapter;
import com.rctapp.chat.ChatActivity;
import com.rctapp.database.UserPreference;
import com.rctapp.databinding.FragmentChatBinding;
import com.rctapp.models.QuoteModel;
import com.rctapp.utils.Api;
import com.rctapp.utils.TrustAllSSL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChatFragment extends Fragment {


    public ChatFragment() {
        // Required empty public constructor
    }


    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    private ChatRecyclerAdapter adapter;
    private List<QuoteModel> models;
    FragmentChatBinding binding;
    UserPreference userPreference;
    OkHttpClient client;
    String quotes;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        client = new OkHttpClient.Builder()
                .connectTimeout (15, TimeUnit.MINUTES) // in seconds
                .readTimeout(15, TimeUnit.MINUTES)
                .sslSocketFactory(TrustAllSSL.createSSLSocketFactory())
                .hostnameVerifier(new TrustAllSSL.TrustAllHostnameVerifier())
                .build();
        binding.chatRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        models = new ArrayList<>();
        userPreference = new UserPreference(getContext());
        try {
            if (userPreference.getLoggedIn()) {
                init();
                binding.noMessages.setVisibility(View.GONE);
            } else {
                binding.noMessages.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            e.printStackTrace();
           // Log.d( "onChat: ", e.getMessage());
        }

    }

    public void init(){
        try {
            if (userPreference.getSeller()){
                quotes = getQuotes(userPreference.getToken());
            }else if (userPreference.getBuyer()){
                quotes = getQuotesBuyer(userPreference.getToken());

            }
            //quotes = getQuotes(userPreference.getToken());
            Log.d("init: ", quotes);
            JSONObject object = new JSONObject(quotes);
            JSONArray quoteArray = object.getJSONArray("data");
            Log.d("onChat: ",  quotes);
            if (quoteArray.length() != 0){
                binding.noMessages.setVisibility(View.VISIBLE);
            }else{
                binding.noMessages.setVisibility(View.GONE);
            }
            for (int i=0; i<quoteArray.length(); i++){
                JSONObject q = quoteArray.getJSONObject(i);
                if (q.getInt("active") == 2) {
                    models.add(new QuoteModel(q.getString("id"), q.getString("tender_id"), q.getString("seller_id"),
                            q.getString("supply_details"),q.getInt("supply_quantity"), q.getInt("supply_price"), q.getInt("active")));
                }

            }
            Collections.reverse(models);
            adapter = new ChatRecyclerAdapter(getContext(), models, userPreference);
            binding.chatRecycler.setAdapter(adapter);
            adapter.setChatClick(model -> {
                Intent i = new Intent(getContext(), ChatActivity.class);
                i.putExtra("id", model.getId());
                i.putExtra("name", model.getSupply_details());
                startActivity(i);
            });
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.d("onChat: ", e.getMessage());
        }
    }

    public String getQuotes(String token) throws IOException {

        Request request = new Request.Builder()
                .url(Api.main_url+ "/api/v1/quote/seller")
                .addHeader("Authorization", token)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String getQuotesBuyer(String token) throws IOException {

        Request request = new Request.Builder()
                .url(Api.main_url+Api.get_all_quotes_buyer)
                .addHeader("Authorization", token)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


}