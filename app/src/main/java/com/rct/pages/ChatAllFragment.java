package com.rct.pages;

import android.app.ProgressDialog;
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

import com.rct.adapter.ChatMessageAdapter;
import com.rct.chat.ChatV2;
import com.rct.database.UserPreference;
import com.rct.databinding.FragmentChatAllBinding;
import com.rct.models.ChatModel;
import com.rct.utils.Api;
import com.rct.utils.Tools;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatAllFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatAllFragment extends Fragment {


    public ChatAllFragment() {
        // Required empty public constructor
    }


    public static ChatAllFragment newInstance() {
        ChatAllFragment fragment = new ChatAllFragment();
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
    FragmentChatAllBinding binding;
    OkHttpClient client;
    UserPreference userPreference;
    List<ChatModel> chatModelList;
    ChatMessageAdapter adapter;
    ProgressDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatAllBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Tools.NetPolicy();
        client = new OkHttpClient();
        userPreference = new UserPreference(getContext());
        chatModelList = new ArrayList<>();
        binding.chatRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        if (userPreference.getLoggedIn()) {
            getMessages();
        }

    }

    public void getMessages(){
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading messages ...");
        dialog.show();
        Request request = new Request.Builder()
                .get()
                .url(Api.main_url+Api.get_all_messages)
                .addHeader("Authorization", "Bearer "+userPreference.getToken())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                requireActivity().runOnUiThread(() -> {
                    Log.d("runChat: ", e.getMessage());
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Failed to get sms", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                requireActivity().runOnUiThread(() -> {
                    try {
                        dialog.dismiss();
                        String data = response.body().string();
                        Log.d( "onResponse: ", data);
                        JSONObject object = new JSONObject(data);
                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            chatModelList.add(new ChatModel(o.getString("messengerId"), o.getString("messenger_id"),
                                    o.getString("seller_id"), o.getString("quote_id"), o.getString("buyer"), o.getString("seller"), o.getBoolean("chat_status"),
                                    o.getBoolean("expiration_status"), o.getInt("message_count"), Long.parseLong( o.getString("updated_time"))));
                        }
                Collections.reverse(chatModelList);
                adapter = new ChatMessageAdapter(getActivity(), chatModelList, userPreference);
                binding.chatRecycler.setAdapter(adapter);
                adapter.setOnChatMessageClick(chatModel -> {
                    Intent i = new Intent(getContext(), ChatV2.class);
                    i.putExtra("chatModel", chatModel);
                    startActivity(i);
                });
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }
}