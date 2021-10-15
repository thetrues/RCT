package com.rctapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.rctapp.adapter.CountrySpinnerAdapter;
import com.rctapp.database.UserPreference;
import com.rctapp.databinding.ActivityAuthBinding;
import com.rctapp.models.CountryModel;
import com.rctapp.models.RoleModel;
import com.rctapp.models.UserModel;
import com.rctapp.utils.Api;
import com.rctapp.utils.DataApi;
import com.rctapp.utils.Tools;
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
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Auth extends AppCompatActivity {
    OkHttpClient client;
    private CountrySpinnerAdapter adapter;
    private List<CountryModel> countryModelList;
    private List<String> countryStrings;
    ActivityAuthBinding binding;
    ProgressDialog dialog;
    Boolean waite_otp = false;
    long waiting_time;
    UserPreference userPreference;
    FirebaseAuth mAuth;
    DatabaseReference mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userPreference = new UserPreference(this);
        Tools.NetPolicy();
        client = new OkHttpClient.Builder()
                .connectTimeout (15, TimeUnit.MINUTES) // in seconds
                .readTimeout(15, TimeUnit.MINUTES)
                .sslSocketFactory(TrustAllSSL.createSSLSocketFactory())
                .hostnameVerifier(new TrustAllSSL.TrustAllHostnameVerifier())
                .build();
        dialog = new ProgressDialog(Auth.this);
        dialog.setMessage("Loading ...");
        getCountry();
        binding.getOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Objects.requireNonNull(binding.phone.getText()).toString().trim().isEmpty()){
                    try {
                        binding.phone.setEnabled(false);
                        signIn(binding.phone.getText().toString().trim(), binding.code.getText().toString().trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        binding.verifyToken.setOnClickListener(view -> {
            String token = binding.token.getText().toString().trim();

            if (token.length() == 0){
                binding.token.setError("Make sure token is complete");
            }
            generateToken(token);
        });
        binding.createAccount.setOnClickListener(view -> {
            String name =  binding.fullName.getText().toString().trim();
            if (!name.isEmpty()) {
                try {
                    completeRegistration(binding.phone.getText().toString().trim(), binding.code.getText().toString().trim(), name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                binding.fullName.setError("Please add name");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (userPreference.getLoggedIn()){
            startActivity(new Intent(getApplicationContext(), MainActivity2.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userPreference.getLoggedIn()){
            startActivity(new Intent(getApplicationContext(), MainActivity2.class));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (userPreference.getLoggedIn()){
            startActivity(new Intent(getApplicationContext(), MainActivity2.class));
        }
    }

    public void getCountry(){

        dialog.show();
        Request request = new Request.Builder()
                .url(Api.main_url+Api.country)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        Log.d( "run: ", e.getMessage());
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
            runOnUiThread(() -> {
                try {
                    dialog.dismiss();
                    String data = response.body().string();
                    JSONObject object = new JSONObject(data);
                    JSONArray array = object.getJSONArray("data");
                    countryModelList = new ArrayList<>();
                    countryModelList.add(new CountryModel("255", "Tanzania, United Rep", "255", "Tanzania Shillings", "TZS", "TZS"));
                    countryStrings = new ArrayList<>();
                    countryStrings.add("Tanzania, United Rep");
                    for (int i=0; i<array.length(); i++){
                        JSONObject c = array.getJSONObject(i);
                        countryModelList.add(new CountryModel(c.getString("code"), c.getString("country_name"), c.getString("dial_code"),
                                c.getString("currency_name"), c.getString("currency_symbol"), c.getString("currency_code")));
                        countryStrings.add(c.getString("country_name"));
                    }
                   adapter = new CountrySpinnerAdapter(Auth.this, R.layout.spinner_list, countryModelList);
                   // ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(Auth.this, android.R.layout.simple_spinner_item, countryStrings);
                    binding.countrySpinner.setAdapter(adapter);
                    binding.countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                          /*  CountryModel country = countryModelList.get(i);
                            binding.code.setText(country.getDial());
                            Log.d("onItemSelected: ", country.getName());*/
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            });
            }
        });
    }

    public void signIn(String phone, String code) throws JSONException {
        dialog.show();
        JSONObject object = new JSONObject();
        object.put("dial_code", code);
        object.put("phone_number", phone);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, String.valueOf(object));
        Request request = new Request.Builder()
                .post(body)
                .url(Api.main_url+Api.signin)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> {
                    dialog.dismiss();
                    e.printStackTrace();
                    Log.d( "run: ", e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dialog.dismiss();
                                String data = response.body().string();
                                Log.d("run: ", data);
                                JSONObject s = new JSONObject(data);
                                if (s.getString("data").equals("Login Successfuly")){
                                    createOtp(phone, code);
                                    binding.firstStep.setVisibility(View.GONE);
                                    binding.secondStep.setVisibility(View.GONE);
                                    binding.thirdStep.setVisibility(View.VISIBLE);
                                }else if (s.getString("data").equals("Account Created")){
                                    binding.firstStep.setVisibility(View.GONE);
                                    binding.secondStep.setVisibility(View.VISIBLE);
                                    binding.thirdStep.setVisibility(View.GONE);

                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            }
        });
    }

    public void createOtp(String phone, String code) throws JSONException {
        dialog.show();
        JSONObject object = new JSONObject();
        object.put("dial_code", code);
        object.put("phone_number", phone);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, String.valueOf(object));
        Request request = new Request.Builder()
                .post(body)
                .url(Api.main_url+Api.createOtp)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> {
                    dialog.dismiss();
                    e.printStackTrace();
                    Log.d( "run: ", e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(() -> {
                    try {
                        dialog.dismiss();
                        String data = response.body().string();
                        JSONObject s = new JSONObject(data);
                        JSONObject d = s.getJSONObject("data");
                        waite_otp = d.getBoolean("status");
                        waiting_time = d.getLong("expire");
                        Log.d("run: ", data);
                        if (d.getBoolean("status")) {
                            binding.firstStep.setVisibility(View.GONE);
                            binding.thirdStep.setVisibility(View.GONE);
                            binding.thirdStep.setVisibility(View.VISIBLE);
                        }else{
                            binding.phone.setEnabled(true);
                            binding.phone.setError("Invalid Phone number");
                            Toast.makeText(getApplicationContext(), "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }


    public void completeRegistration(String phone, String code, String name) throws JSONException {
        dialog.show();
        JSONObject object = new JSONObject();
        object.put("dial_code", code);
        object.put("phone_number", phone);
        object.put("name", name);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, String.valueOf(object));
        Request request = new Request.Builder()
                .put(body)
                .url(Api.main_url+Api.complete_registration)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> {
                    dialog.dismiss();
                    e.printStackTrace();
                    Log.d( "run: ", e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            dialog.dismiss();
                            String data = response.body().string();
                            JSONObject s = new JSONObject(data);
                            Log.d( "run: ", data);
                           // if (s.getString("data").equals("Account Updated")){
                                createOtp(phone, code);
                                binding.firstStep.setVisibility(View.GONE);
                                binding.secondStep.setVisibility(View.GONE);
                                binding.thirdStep.setVisibility(View.VISIBLE);
                          //  }else {
                                Toast.makeText(getApplicationContext(), "Fail try again", Toast.LENGTH_SHORT).show();
                        //    }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void generateToken(String token){
        dialog.show();
        FormBody body = new FormBody.Builder()
                .add("token", token)
                .build();
        Request request = new Request.Builder()
                .url(Api.main_url+Api.generateToken+token)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> {
                    dialog.dismiss();
                    e.printStackTrace();
                    Log.d( "runGenerate: ", e.getMessage());

                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(() -> {
                    try {
                        dialog.dismiss();
                        dialog.setMessage("Getting user information");
                        dialog.show();
                        String data = response.body().string();
                        JSONObject object1 = new JSONObject(data);
                        Log.d( "onResponse: ", data);
                        JSONObject object = object1.getJSONObject("data");
                        DataApi.SavePreferenceData("token", "token", object.getString("token"), getApplicationContext());
                        DataApi.SavePreferenceData("refreshToken", "refreshToken", object.getString("refreshToken"), getApplicationContext());
                        SaveUserInformation(DataApi.getUserInformation(object.getString("token"), getApplicationContext()), object.getString("token"));
                       // startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public void SaveUserInformation(String data, String token) throws JSONException {
        UserPreference userPre = new UserPreference(this);
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
       if(!img.isEmpty()){
           userPre.setImage(img);
       }
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

        if(role.length() == 2) {
            userPre.setRole("both");
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

        }
      //  firebaseAuth(token);
        startActivity(new Intent(getApplicationContext(), MainActivity2.class));
    }

    public void firebaseAuth(String token){
        mAuth.signInWithCustomToken(token)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d("onComplete: " , user.getUid());
                    }else{
                        Log.d("onComplete: ", task.getException().toString());
                        Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}