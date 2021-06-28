package com.rctapp.chat;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rctapp.R;
import com.rctapp.adapter.MessageRecyclerAdapter;
import com.rctapp.database.UserPreference;
import com.rctapp.databinding.ActivityChatV2Binding;
import com.rctapp.models.ChatModel;
import com.rctapp.models.MessageModel;
import com.rctapp.models.QuoteModel;
import com.rctapp.utils.Api;
import com.rctapp.utils.DataApi;
import com.rctapp.utils.Tools;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatV2 extends AppCompatActivity implements MqttCallback {
    ChatModel chatModel;
    OkHttpClient client;
    List<MessageModel> messageModelList;
    MessageRecyclerAdapter adapter;
    ActivityChatV2Binding binding;
    UserPreference userPreference;
    String user;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatV2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
        userPreference = new UserPreference(this);
        binding.backArrow.setOnClickListener(view -> finish());
        chatModel = (ChatModel) getIntent().getExtras().getSerializable("chatModel");
        if (chatModel.getSeller().equals(userPreference.getUserId()) && !chatModel.getBuyer_image_path().isEmpty()){
            String imgURL = "http://rctapp.co.tz/rctimages/rct-upload-encoded/" + chatModel.getSeller_image_path();
            Tools.displayImageOriginal(getApplication(), binding.image, imgURL);
        }else{
            String imgURL = "http://rctapp.co.tz/rctimages/rct-upload-encoded/" + chatModel.getBuyer_image_path();
            Tools.displayImageOriginal(getApplication(), binding.image, imgURL);
        }
        try {
            QuoteModel quoteModel = DataApi.getQuote(chatModel.getQuote_id(), userPreference.getToken());
            Log.d("onGetQuote: ", quoteModel.getId());
            binding.fabQuote.setOnClickListener(view -> viewQuote(quoteModel));
        }catch (Exception e){
            e.printStackTrace();
            Log.d( "onGetQUOTE ", e.getMessage());
        }
        Log.d( "onChatV2: ", chatModel.getMessenger_id());
        if (chatModel.isExpiration_status()){
            binding.status.setText("Active");
            binding.status.setTextColor(Color.GREEN);
        }else {
            binding.status.setText("Expired");
            binding.status.setTextColor(Color.RED);
        }
        client = new OkHttpClient();
        try {
            if (userPreference.getSeller()) {
                user = getUser(chatModel.getMessenger_id());
            }else{
                user = getUser(chatModel.getSeller_id());
            }
            Log.d("onCreate: ", user);
            JSONObject u = new JSONObject(user);
            JSONObject ui = u.getJSONObject("data");
            JSONObject uui = ui.getJSONObject("user");
            binding.names.setText(uui.getString("name"));
        }catch (Exception e){
            e.printStackTrace();
        }
        messageModelList = new ArrayList<>();
        getMessage(chatModel.getQuote_id());
        try {
            DataApi.MqttSub(this, chatModel.getQuote_id());
        }catch (Exception e){
            e.printStackTrace();
            Log.d( "onMqttFail: ", e.getMessage());
        }
        binding.btnSend.setOnClickListener(view -> {
            String sms = binding.textContent.getText().toString().trim();
            if (!sms.isEmpty()){
                if (userPreference.getSeller()){
                    try {
                      String resp = sendSMS(chatModel.getQuote_id(), userPreference.getUserId(), chatModel.getMessenger_id(), sms);
                        binding.textContent.setText("");
                        Log.d("onCreate: ", resp);
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                      String resp = sendSMS(chatModel.getQuote_id(), userPreference.getUserId(), chatModel.getSeller_id(), sms);
                        binding.textContent.setText("");
                        Log.d("onCreate: ", resp);
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                binding.textContent.setError("Add message");
                Toast.makeText(getApplicationContext(), "Please add message", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getMessage(String quoteId) {
        Request request = new Request.Builder()
                .url(Api.main_url+"/api/v1/message/"+quoteId)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> e.printStackTrace());
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                runOnUiThread(() -> {
                    try {
                        String data = response.body().string();
                        Log.d("onResponseChat: ", data);
                        JSONObject msg = new JSONObject(data);
                        JSONArray array = msg.getJSONArray("data");
                        messageModelList.clear();
                        for (int i=0; i<array.length(); i++){
                            JSONObject ms = array.getJSONObject(i);
                            Log.d( "onResponse: ", ms.getString("id"));
                            messageModelList.add(new MessageModel(ms.getString("id"), ms.getString("messengerId"), ms.getString("senderId"),
                                    ms.getString("receiverId"), ms.getString("message"), ms.getLong("time"), ms.getBoolean("readStatus")));
                        }

                        Log.d("onResponse: ", messageModelList.toString());
                       Collections.sort(messageModelList, (messageModel, t1) -> {
                            String one = String.valueOf(messageModel.getTime());
                            String two = String.valueOf(t1.getTime());
                            return one.compareTo(two);
                        });
                        Collections.reverse(messageModelList);
                        adapter = new MessageRecyclerAdapter(ChatV2.this, messageModelList, ChatV2.this, userPreference);
                        binding.recyclerView.setAdapter(adapter);
                        adapter.setOnItemClickListener((view, obj, position) -> {

                        });

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public String getUser(String userId) throws IOException {
        Request request = new Request.Builder()
                .url(Api.main_url+"/api/v1/user/information/"+userId)
                .addHeader("Authorization", "Bearer " + userPreference.getToken())
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String sendSMS(String quote_id, String senderId, String receiverId, String message) throws JSONException, IOException {
        JSONObject object = new JSONObject();
        object.put("quote_id", quote_id);
        object.put("sender_id", senderId);
        object.put("receiver_id", receiverId);
        object.put("message", message);
        Log.d("sendSMS: ", object.toString());
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, String.valueOf(object));
        Request request = new Request.Builder()
                .url(Api.main_url+"/api/v1/message")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.d("connectionLost: ", cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.d("messageArriveddd: ", message.toString());
        messageModelList.clear();
        getMessage(chatModel.getQuote_id());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    public void viewQuote(QuoteModel model){
        AlertDialog.Builder qu = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.quote_view, null);
        qu.setView(v);
        ((TextView) v.findViewById(R.id.price)).setText(String.valueOf(model.getSupply_price()));
        ((TextView) v.findViewById(R.id.quantity)).setText(String.valueOf(model.getSupply_quantity()));
        ((TextView) v.findViewById(R.id.description)).setText(model.getSupply_details());
        v.findViewById(R.id.bt_close).setOnClickListener(view -> alertDialog.dismiss());
        alertDialog = qu.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }
}