package com.rct.pages.notificattion;

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

import com.rct.adapter.NotificationRecyclerAdapter;
import com.rct.chat.ChatActivity;
import com.rct.database.UserPreference;
import com.rct.databinding.FragmentRequestNotificationBinding;
import com.rct.models.NotificationModel;
import com.rct.utils.Api;
import com.rct.utils.DataApi;
import com.rct.utils.Tools;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestNotification#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestNotification extends Fragment {

    public RequestNotification() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RequestNotification newInstance(String param1, String param2) {
        RequestNotification fragment = new RequestNotification();
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
    FragmentRequestNotificationBinding binding;
    private NotificationRecyclerAdapter adapter;
    private List<NotificationModel> model;
    private OkHttpClient client;
    private UserPreference userPreference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRequestNotificationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        client = new OkHttpClient();
        userPreference = new UserPreference(getContext());
        Tools.NetPolicy();
        binding.requestRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        getRequest();
    }

    public void getRequest(){
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + userPreference.getToken())
                .url(Api.main_url+ Api.get_seller_offer_tender)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                requireActivity().runOnUiThread(() -> {
                    e.printStackTrace();
                    Log.d("run: ", e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                requireActivity().runOnUiThread(() -> {
                    try {
                        String data = response.body().string();
                        Log.d("onResponse: ", data);
                        model = new ArrayList<>();
                        JSONObject object = new JSONObject(data);
                        JSONObject json = object.getJSONObject("data");
                        JSONArray array = json.getJSONArray("requestTenderList");
                        for (int i=0; i<array.length(); i++){
                            JSONObject t = array.getJSONObject(i);
                            model.add(new NotificationModel(t.getString("id"), t.getString("sellerId"), t.getString("variety"), t.getString("pickup_location"), t.getString("extra_details"),
                                    "", "", "", t.getString("request_document"), t.getString("request_card"), "", t.getLong("quantity"), t.getLong("grade"),
                                    t.getLong("active"), t.getLong("selling_price"), t.getBoolean("graded")));
                        }
                        adapter = new NotificationRecyclerAdapter(getContext(), model);
                        binding.requestRecycler.setAdapter(adapter);
                        adapter.setOnNotificationClick(model -> {
                            if (model.getActive() == 2){
                                GotToChat(model);
                            }else {
                                if (userPreference.getBuyer() && userPreference.getRole().toLowerCase().equals("buyer")) {
                                    JSONObject ob = new JSONObject();
                                    try {
                                        ob.put("seller_id", model.getSeller_id());
                                        ob.put("buyer_id", userPreference.getUserId());
                                        ob.put("quote_id", model.getId());
                                        String resp = DataApi.initChat(ob, userPreference.getToken());
                                        Log.d("onResponse: ", resp);
                                        Toast.makeText(getContext(), "Chat initiated", Toast.LENGTH_SHORT).show();
                                    } catch (JSONException | IOException e) {
                                        e.printStackTrace();
                                        Log.d("onClick: ", e.getMessage());
                                    }
                                } else {
                                    Toast.makeText(getContext(), "Wait for buyer to accept your request", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public void GotToChat(NotificationModel model){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Quotation Accepted");
        builder.setMessage(model.getExtra_details());
        builder.setPositiveButton("Go to chat", (dialogInterface, i) -> {
            Intent s = new Intent(getContext(), ChatActivity.class);
            s.putExtra("id", model.getId());
            s.putExtra("name", model.getExtra_details());
            startActivity(s);
        }).setNegativeButton("Not now", (dialogInterface, i) -> {

        });
        builder.create().show();
    }
}