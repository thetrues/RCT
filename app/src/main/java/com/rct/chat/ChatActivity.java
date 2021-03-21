package com.rct.chat;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rct.adapter.MessageRecyclerAdapter;
import com.rct.database.UserPreference;
import com.rct.databinding.ActivityChatBinding;
import com.rct.models.MessageModel;
import com.rct.models.RecentChatMessage;
import com.rct.utils.Api;
import com.rct.utils.Vars;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
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

public class ChatActivity extends AppCompatActivity implements MqttCallback {

    ActivityChatBinding binding;
    OkHttpClient client;
    MessageRecyclerAdapter adapter;
    List<MessageModel> messageModelList;
    String messengerId, sender_id, receiverId, quote_id, chat_status, expiration_status;
    String messenger_id = "";
    UserPreference userPreference;
    MqttAndroidClient androidClient;
    List<RecentChatMessage> recentChatMessages;
    List<MessageModel> ms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        client = new OkHttpClient();
        ms = new ArrayList<>();
        Log.d("onChat: ", ms.toString());
         binding.backArrow.setOnClickListener(view -> finish());

        recentChatMessages = new ArrayList<>();
        binding.recentRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        messageModelList = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
        userPreference = new UserPreference(this);
        String clientId = userPreference.getUserId();
        quote_id = getIntent().getExtras().getString("id");
        try {
           Gt();
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("onCreate: ", quote_id);
        try {
            getMessage(quote_id);
            String mm = post(quote_id);
            Log.d( "run: ", mm);
            JSONObject object = new JSONObject(mm);
            JSONObject object1 = object.getJSONObject("data");
            messengerId = object1.getString("messengerId");
            if (userPreference.getRole().toLowerCase().equals("buyer")){
                messenger_id = object1.getString("seller_id");
            }else {
                messenger_id = object1.getString("messenger_id");
            }
           String user = getUser(messenger_id);
            Log.d("onCreate: ", user);
           JSONObject u = new JSONObject(user);
           JSONObject ui = u.getJSONObject("data");
           JSONObject uui = ui.getJSONObject("user");
           binding.names.setText(uui.getString("name"));
           // MqttUtil.getInstance().subscribeMessage(quote_id);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.d("onChat: ", e.getMessage());
        }

        binding.btnSend.setOnClickListener(view -> {
            String mess = binding.textContent.getText().toString().trim();
            String revei = messenger_id;
            if (mess.isEmpty()){
                Toast.makeText(ChatActivity.this, "Add message first", Toast.LENGTH_SHORT).show();
            }else{
                try {
                    if (revei.length() == 0) {
                        Toast.makeText(getApplicationContext(), "You can not send sms in this quote", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.d("sendSms: ", quote_id + " " + messenger_id);
                        String resp = sendSMS(quote_id, messenger_id, mess);
                        binding.textContent.setText("");
                        Log.d("onCreate: ", resp);
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public String post(String quoteId) throws IOException {
        Request request = new Request.Builder()
                .addHeader("Authorization", userPreference.getToken())
                .url(Api.main_url+"/api/v1/messenger/"+quoteId)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
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
                      /*  Log.d("onResponse: ", messageModelList.toString());
                        Set<MessageModel> set = new HashSet<>(messageModelList);
                        messageModelList.clear();
                        messageModelList.addAll(set);

*/
                        Log.d("onResponse: ", messageModelList.toString());
                     /*  Collections.sort(messageModelList, (messageModel, t1) -> {
                            String one = String.valueOf(messageModel.getTime());
                            String two = String.valueOf(t1.getTime());
                            return one.compareTo(two);
                        });*/
                        Collections.reverse(messageModelList);

                        Log.d("onResponse: ", removeDuplicates(messageModelList).toString());
                        adapter = new MessageRecyclerAdapter(ChatActivity.this, messageModelList, ChatActivity.this, userPreference);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static List<MessageModel> removeDuplicates(List<MessageModel> origArray) {
        List<MessageModel> messageMode = new ArrayList<>();
        for (int indexI = 0; indexI < origArray.size(); indexI++)
        {
            MessageModel me = origArray.get(indexI);
            if (!messageMode.contains(me)) {
                messageMode.add(me);
                Log.d("removeDuplicates: ", me.getId());
            }
        }
        messageMode.stream().distinct().toArray();
        return messageMode;
    }

    public String sendSMS(String quote_id, String messenger_id, String message) throws JSONException, IOException {
        JSONObject object = new JSONObject();
        object.put("quote_id", quote_id);
        object.put("sender_id", userPreference.getUserId());
        object.put("receiver_id", messenger_id);
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

    public void Gt() {
                try {

                MqttClient client = new MqttClient(Vars.MQTT_BROKER_ADDR, MqttClient.generateClientId(), new MemoryPersistence());
                client.connect();
                client.setCallback(ChatActivity.this);
                client.subscribe(quote_id);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("Gt: ", e.getMessage());
                }

    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.d("connectionLost: ", cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.d("messageArriveddd: ", message.toString());
            messageModelList.clear();
            getMessage(quote_id);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d("deliveryComplete: ", token.toString());
    }
/*
    public void newMessage(){
        Log.d("newAdapter: ", ms.toString());
        adapter = new MessageRecyclerAdapter(this,  ms, this, userPreference);
        binding.recentRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }*/

}