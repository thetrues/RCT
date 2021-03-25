package com.rct.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.rct.chat.ChatActivity;
import com.rct.database.UserPreference;
import com.rct.models.QuoteModel;
import com.rct.models.UserModel;
import com.rct.models.VarietyVar;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DataApi {
    public static boolean Sucess = false;

    public static void SavePreferenceData(String filename, String name, String s, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, s);
        editor.apply();
    }

    public static String ReadPreference(String filename, String name, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return sharedPreferences.getString(name, "guest");
    }

    public static void initAccount(Context context) throws IOException {
      String account =  ReadPreference("token", "token", context);
      if (!account.equals("guest")){
          getUserInformation(account, context);
      }
    }

    public static String getUserInformation(String token, Context context) throws IOException {
        OkHttpClient client = new OkHttpClient();
        UserPreference userPre = new UserPreference(context);
        Request request = new Request.Builder()
                .url(Api.main_url + Api.get_user_information_by_token)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
      /*  client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d("onFailure: ", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String data = response.body().string();
                Log.d( "onResponse: ", data);
                try {
                    JSONObject object = new JSONObject(data);
                    JSONObject d = object.getJSONObject("data");
                    JSONObject c = d.getJSONObject("user");
                    String id = c.getString("id");
                    String name = c.getString("name");
                    int active = c.getInt("active");
                    String deal_code = c.getString("dial_code");
                    String phone = c.getString("phone_number");
                    String img = d.getString("profile_image_path");
                    userPre.setName(name);
                    userPre.setUserId(id);
                    userPre.setToken(token);
                    userPre.setIsLoggedIn(true);
                    userPre.setDialCode(deal_code);
                    userPre.setPhone(phone);
                    userPre.setActive(String.valueOf(active));
                    List<RoleModel> list = new ArrayList<>();
                    JSONArray role = d.getJSONArray("roles");
                    String singleRole = "regular";
                    UserModel a = new UserModel();
                    a.setId(id);
                    a.setNames(name);
                    a.setActive(active);
                    a.setDial_code(deal_code);
                    a.setImg(img);
                    a.setPhone(phone);
                    a.setToken(token);
                    for (int i=0; i<role.length(); i++){
                        JSONObject o = role.getJSONObject(i);
                        singleRole = o.getString("role");
                        list.add(new RoleModel(o.getString("id"), o.getString("role"),
                                o.getString("user_id"), o.getInt("active")));
                        if (o.getString("role").equals("seller")){
                            a.setRole("seller");
                        }
                    }
                    if(role.length() == 2) {
                         userPre.setRole("both");
                        String[] users = {"Buyer", "Seller"};
                      *//*  AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setTitle("Choose Account");
                        dialog.setItems(users, (dialogInterface, i) -> {

                            String userRole;
                            if (i == 0){
                                userRole = "buyer";
                            }else{
                                userRole = "seller";
                            }
                            a.setRole(userRole);
                            SaveUserData(a, context);
                        });
                        dialog.create().show();*//*

                    }else{

                        if (singleRole.equals("regular")){
                            a.setRole("buyer");
                            userPre.setRole("buyer");
                            userPre.setIsBuyer(true);
                        }else {
                            userPre.setRole("seller");
                            userPre.setSeller(true);
                            a.setRole("seller");
                        }
                        SaveUserData(a, context);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("onResponse: ", e.getMessage());
                }
            }
        });
     */
    }

    public static void SaveUserData(UserModel userModel, Context context){
 /*       UserPreference ref = new UserPreference(context);
        ref.setName(userModel.getNames());
        ref.setPhone(userModel.getPhone());
        ref.setActive(String.valueOf(userModel.getActive()));
        ref.setIsLoggedIn(true);
        ref.setDialCode(userModel.getDial_code());
        ref.setToken(userModel.getToken());*/
        Log.d("SaveUserData: ", "Saved");

    }

    public static void ClearToken(String name, Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }


    public static boolean GiveTender(JSONObject object, Context context, String token){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, String.valueOf(object));
        Request request = new Request.Builder()
                .url(Api.main_url+Api.get_seller_tender)
                .addHeader("Authorization", "Bearer " + token)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("onFailure: ", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String data = response.body().string();
                Log.d("onGiveTender: ", data + " " +token);
                try {
                    JSONObject ob = new JSONObject(data);
                    Sucess = ob.getBoolean("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        return Sucess;
    }

    public static int SendQuote(String token, String tenderId, String quantity, String price, String details, String location,  Context context) throws JSONException, IOException {
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        object.put("supply_quantity", Integer.parseInt(quantity));
        object.put("supply_price", Integer.parseInt(price));
        object.put("supply_pickup_location", location);
        object.put("supply_details", details);
        Log.d("SendQuote: ", object.toString());
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, String.valueOf(object));
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + token)
                .post(body)
                .url(Api.main_url+Api.get_send_quote+tenderId)
                .build();
        /*client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String data = response.body().string();
                Log.d("onSendQuote: ", data);
            }
        });*/

        Response response = client.newCall(request).execute();
        Log.d("SendQuote: ", response.body().string());
        return response.code();
    }

    public static int RequestTender(String token, int quantity, int selling_perice, int grated, int grade, int certified, String location, String details,
                                    String variety, Object buyer) throws JSONException, IOException {
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        object.put("quantity", quantity);
        object.put("selling_price", selling_perice);
        object.put("is_graded", grated);
        object.put("grade", grade);
        object.put("is_batch_certified", certified);
        object.put("pickup_location", location);
        object.put("extra_details", details);
        object.put("variety", variety);
        object.put("buyer_sellection", buyer);
        Log.d( "RequestTender: ", object.toString());
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, String.valueOf(object));
        Request request = new Request.Builder()
                .post(body)
                .addHeader("Authorization", token)
                .url(Api.main_url+Api.seller_request_tender)
                .build();
        Response response = client.newCall(request).execute();
        Log.d( "RequestTender: ", response.body().string());
        return response.code();
    }

    public static String initChat(Object object, String token) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, String.valueOf(object));
        Request request = new Request.Builder()
                .url(Api.main_url+"/api/v1/messenger")
                .addHeader("Authorization", token)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    public static String approveQuote(Object object, String token, String quoteId) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, String.valueOf(object));
        Request request = new Request.Builder()
                .url(Api.main_url+"/api/v1/quote/approve/"+quoteId)
                .addHeader("Authorization", token)
                .put(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    public static String getUser(String userId, UserPreference userPreference) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Api.main_url+"/api/v1/user/information/"+userId)
                .addHeader("Authorization", "Bearer " + userPreference.getToken())
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static void GotToChat(QuoteModel model, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Quotation Accepted");
        builder.setMessage(model.getSupply_details());
        builder.setPositiveButton("Go to chat", (dialogInterface, i) -> {
            Intent s = new Intent(context, ChatActivity.class);
            s.putExtra("id", model.getId());
            s.putExtra("name", model.getSupply_details());
            context.startActivity(s);
        }).setNegativeButton("Not now", (dialogInterface, i) -> {

        });
        builder.create().show();
    }

    public static void GetVarietyServer(Context context) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Api.main_url+"/api/v1/variety")
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String data = response.body().string();
                Log.d( "GetVariety: ", data);
                SavePreferenceData("variety", "variety", data, context);
            }
        });

    }

    public static List<String> getVariety(Context context) {
        List<String> variety = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences("variety", Context.MODE_PRIVATE);
       String data = sharedPreferences.getString("variety", "null");
        try {
            JSONObject object = new JSONObject(data);
        JSONArray array = object.getJSONArray("data");
        for (int i = 0; i < array.length(); i++) {
            JSONObject o = array.getJSONObject(i);
            variety.add(o.getString("variety_name"));
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return variety;
    }

    public static List<VarietyVar> getVarietyByModel(Context context){
        List<VarietyVar> varietyVars = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences("variety", Context.MODE_PRIVATE);
        String data = sharedPreferences.getString("variety", "null");
        try {
            JSONObject object = new JSONObject(data);
            JSONArray array = object.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                varietyVars.add(new VarietyVar(o.getString("id"), o.getInt("active"), o.getString("variety_name"),
                        o.getString("platform_name"), o.getString("platform_region"), o.getString("platform_district"), o.getString("user_name")));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return varietyVars;
    }

    public static void MqttSub(Context context, String quote_id){
        try {
            MqttClient client = new MqttClient(Vars.MQTT_BROKER_ADDR, MqttClient.generateClientId(), new MemoryPersistence());
            client.connect();
            client.setCallback((MqttCallback) context);
            client.subscribe(quote_id);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public static QuoteModel getQuote(String quoteId, String token){
        OkHttpClient client = new OkHttpClient();
        QuoteModel model = new QuoteModel();
        Request request = new Request.Builder()
                .url(Api.main_url+"/api/v1/quote/seller")
                .addHeader("Authorization", "Bearer "+token)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            String data = response.body().string();
            JSONObject object = new JSONObject(data);
            JSONArray array = object.getJSONArray("data");
            for (int i=0; i<array.length(); i++){
                JSONObject o = array.getJSONObject(i);
                if (o.getString("id").equals(quoteId)) {
                    model.setId(o.getString("id"));
                    model.setTender_id(o.getString("tender_id"));
                    model.setSeller_id(o.getString("seller_id"));
                    model.setSupply_details(o.getString("supply_details"));
                    model.setSupply_quantity(o.getInt("supply_quantity"));
                    model.setSupply_price(o.getInt("supply_price"));
                    model.setActive(o.getInt("active"));
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return model;
    }


}

