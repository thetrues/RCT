package com.rctapp.chat;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rctapp.R;
import com.rctapp.adapter.FirebaseMessageAdapter;
import com.rctapp.database.UserPreference;
import com.rctapp.databinding.ActivityFirebaseChatBinding;
import com.rctapp.models.ChatFireModel;
import com.rctapp.models.ChatModel;
import com.rctapp.models.QuoteModel;
import com.rctapp.utils.Api;
import com.rctapp.utils.DataApi;
import com.rctapp.utils.Tools;
import com.rctapp.utils.TrustAllSSL;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class FirebaseChat extends AppCompatActivity {
    ChatModel chatModel;
    OkHttpClient client;
    List<ChatFireModel> chatFireModels;
    FirebaseMessageAdapter adapter;
    ActivityFirebaseChatBinding binding;
    UserPreference userPreference;
    String user;
    AlertDialog alertDialog;
    DatabaseReference mData, mData2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFirebaseChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userPreference = new UserPreference(this);
        binding.backArrow.setOnClickListener(view -> finish());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));

        client = new OkHttpClient.Builder()
                .connectTimeout (15, TimeUnit.MINUTES) // in seconds
                .readTimeout(15, TimeUnit.MINUTES)
                .sslSocketFactory(TrustAllSSL.createSSLSocketFactory())
                .hostnameVerifier(new TrustAllSSL.TrustAllHostnameVerifier())
                .build();
        chatModel = (ChatModel) getIntent().getExtras().getSerializable("chatModel");
        if (!chatModel.getSeller_id().equals(userPreference.getUserId())) {
            if (!chatModel.getBuyer_image_path().isEmpty()){
                String imgURL = Api.main_plain + "/rctimages/rct-upload-encoded/" + chatModel.getSeller_image_path();
            Tools.displayImageOriginal(getApplication(), binding.image, imgURL);
            binding.names.setText(Tools.toCamelCase(chatModel.getSeller()));
        }
        }else if(!chatModel.getSeller_image_path().isEmpty()){
            String imgURL = Api.main_plain+"/rctimages/rct-upload-encoded/" + chatModel.getBuyer_image_path();
            Tools.displayImageOriginal(getApplication(), binding.image, imgURL);
            binding.names.setText(Tools.toCamelCase(chatModel.getBuyer()));
        }
        try {
            QuoteModel quoteModel = DataApi.getQuote(chatModel.getQuote_id(), userPreference.getToken());
            Log.d("onGetQuote: ", quoteModel.getId());
            binding.fabQuote.setOnClickListener(view -> viewQuote(quoteModel));
        }catch (Exception e){
            e.printStackTrace();
            Log.d( "onGetQUOTE ", e.getMessage());
        }

        if (!chatModel.isExpiration_status()){
            binding.status.setText("Active");
            binding.status.setTextColor(Color.GREEN);
        }else {
            binding.status.setText("Expired");
            binding.status.setTextColor(Color.RED);
        }

        binding.btnSend.setOnClickListener(view -> {
            String sms = binding.textContent.getText().toString().trim();
            if (!sms.isEmpty()){
                if (userPreference.getSeller()){
                    try {
                        String resp = sendSMS(chatModel.getQuote_id(), userPreference.getUserId(), chatModel.getBuyer_id(), sms);
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

        mData = FirebaseDatabase.getInstance().getReference().child("chats").child(userPreference.getUserId()).child(chatModel.getQuote_id());
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatFireModels = new ArrayList<>();
                for (DataSnapshot d: snapshot.getChildren()){
                    ChatFireModel model = d.getValue(ChatFireModel.class);
                    chatFireModels.add(model);
                }
                Log.d("onDataChange: ", chatFireModels.toString());
                Collections.reverse(chatFireModels);
                adapter = new FirebaseMessageAdapter(getApplicationContext(), chatFireModels, FirebaseChat.this, userPreference);
                binding.recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener((view, obj, position) -> {

                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public String sendSMS(String quote_id, String senderId, String receiverId, String message) throws JSONException, IOException {
        mData = FirebaseDatabase.getInstance().getReference().child("chats").child(senderId).child(quote_id).push();
//        mData.child("quote_id").setValue(quote_id);
//        mData.child("sender_id").setValue(senderId);
//        mData.child("receiver_id").setValue(receiverId);
//        mData.child("message").setValue(message);
//        mData.child("readStatus").setValue(true);
//        mData.child("time").setValue(System.currentTimeMillis());
        Map<String, Object> one = new HashMap<>();
        one.put("quote_id", quote_id);
        one.put("sender_id", senderId);
        one.put("receiver_id", receiverId);
        one.put("message", message);
        one.put("readStatus", true);
        one.put("time", System.currentTimeMillis());
        mData.setValue(one);

        mData2 = FirebaseDatabase.getInstance().getReference().child("chats").child(receiverId).child(quote_id).push();
        Map<String, Object> two = new HashMap<>();
        two.put("quote_id", quote_id);
        two.put("sender_id", senderId);
        two.put("receiver_id", receiverId);
        two.put("message", message);
        two.put("readStatus", false);
        two.put("time", System.currentTimeMillis());
        mData2.setValue(two);
//        JSONObject object = new JSONObject();
//        object.put("quote_id", quote_id);
//        object.put("sender_id", senderId);
//        object.put("receiver_id", receiverId);
//        object.put("message", message);
//        Log.d("sendSMS: ", object.toString());
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = RequestBody.create(JSON, String.valueOf(object));
//        Request request = new Request.Builder()
//                .url(Api.main_url+"/api/v1/message")
//                .post(body)
//                .build();
//        Response response = client.newCall(request).execute();
//        return response.body().string();
        return "sent";
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